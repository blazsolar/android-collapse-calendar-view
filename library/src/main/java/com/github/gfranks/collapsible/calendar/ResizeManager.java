package com.github.gfranks.collapsible.calendar;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.github.gfranks.collapsible.calendar.model.CollapsibleState;

class ResizeManager {

    private final int mTouchSlop;
    private final int mMinFlingVelocity;
    private final int mMaxFlingVelocity;
    private final Scroller mScroller;
    private CollapsibleCalendarView mCalendarView;
    private float mDownY;
    private float mDragStartY;
    private State mState = State.IDLE;
    private VelocityTracker mVelocityTracker;
    private ProgressManager mProgressManager;

    public ResizeManager(CollapsibleCalendarView calendarView) {
        mCalendarView = calendarView;
        mScroller = new Scroller(calendarView.getContext());
        ViewConfiguration viewConfig = ViewConfiguration.get(mCalendarView.getContext());
        mTouchSlop = viewConfig.getScaledTouchSlop();
        mMinFlingVelocity = viewConfig.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = viewConfig.getScaledMaximumFlingVelocity();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (!mCalendarView.isAllowStateChange()) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return onDownEvent(ev);
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                return checkForResizing(ev);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                finishMotionEvent();
                return false;
        }

        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        if (!mCalendarView.isAllowStateChange()) {
            return true;
        }

        if (action == MotionEvent.ACTION_MOVE) {
            mVelocityTracker.addMovement(event);
        }

        if (mState == State.DRAGGING) {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    int deltaY = calculateDistanceForDrag(event);
                    mProgressManager.applyDelta(deltaY);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    finishMotionEvent();
                    break;
            }

        } else if (action == MotionEvent.ACTION_MOVE) {
            checkForResizing(event);
        }

        return true;
    }

    /**
     * Triggered
     *
     * @param event Down event
     */
    private boolean onDownEvent(MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) != MotionEvent.ACTION_DOWN) {
            throw new IllegalStateException("Has to be down event!");
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }

        mDownY = event.getY();

        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
            if (mScroller.getFinalY() == 0) {
                mDragStartY = mDownY + mScroller.getStartY() - mScroller.getCurrY();
            } else {
                mDragStartY = mDownY - mScroller.getCurrY();
            }
            mState = State.DRAGGING;
            return true;
        } else {
            return false;
        }

    }

    public void recycle() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public boolean checkForResizing(MotionEvent ev) {
        if (mState == State.DRAGGING) {
            return true;
        }

        final int yDIff = calculateDistance(ev);

        if (Math.abs(yDIff) > mTouchSlop) {
            mState = State.DRAGGING;
            mDragStartY = ev.getY();

            startResizing();

            return true;
        }

        return false;
    }

    private void startResizing() {
        if (mProgressManager == null) {

            CalendarManager manager = mCalendarView.getManager();
            CollapsibleState state = manager.getState();

            int weekOfMonth = manager.getWeekOfMonth();

            if (state == CollapsibleState.WEEK) { // always animate in month view
                manager.toggleView();
                mCalendarView.populateLayout();
            }

            mProgressManager = new ProgressManagerImpl(mCalendarView, weekOfMonth,
                    state == CollapsibleState.MONTH);
        }
    }

    private void finishMotionEvent() {
        if (mProgressManager != null && mProgressManager.isInitialized()) {
            startScrolling();
        }
    }

    private void startScrolling() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
        int velocity = (int) mVelocityTracker.getYVelocity();

        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }

        int progress = mProgressManager.getCurrentHeight();
        int end;
        if (Math.abs(velocity) > mMinFlingVelocity) {

            if (velocity > 0) {
                end = mProgressManager.getEndSize() - progress;
            } else {
                end = -progress;
            }

        } else {

            int endSize = mProgressManager.getEndSize();
            if (endSize / 2 <= progress) {
                end = endSize - progress;
            } else {
                end = -progress;
            }

        }

        mScroller.startScroll(0, progress, 0, end);
        mCalendarView.postInvalidate();

        mState = State.SETTLING;

    }

    private int calculateDistance(MotionEvent event) {
        return (int) (event.getY() - mDownY);
    }

    private int calculateDistanceForDrag(MotionEvent event) {
        return (int) (event.getY() - mDragStartY);
    }

    public void onDraw() {
        if (!mScroller.isFinished()) {
            mScroller.computeScrollOffset();

            float position = mScroller.getCurrY() * 1f / mProgressManager.getEndSize();
            mProgressManager.apply(position);
            mCalendarView.postInvalidate();
        } else if (mState == State.SETTLING) {
            mState = State.IDLE;
            float position = mScroller.getCurrY() * 1f / mProgressManager.getEndSize();
            mProgressManager.finish(position > 0);
            mProgressManager = null;
        }
    }

    public void toggle() {
        if (mProgressManager == null) {
            startResizing();
        }

        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }

        if (!mProgressManager.isInitialized()) {
            mProgressManager.setListener(new ProgressManager.OnInitListener() {
                @Override
                public void onInit() {
                    testFinish();
                    mProgressManager.setListener(null);
                }
            });
        } else {
            testFinish();
        }
    }

    private void testFinish() {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }

        int progress = mProgressManager.getCurrentHeight();
        int end = 0;
        int endSize = mProgressManager.getEndSize();
        if (endSize / 2 > progress) {
            end += endSize;
        }
        end -= progress;

        mScroller.startScroll(0, progress, 0, end);
        mCalendarView.postInvalidate();

        mState = State.SETTLING;
    }

    private enum State {
        IDLE,
        DRAGGING,
        SETTLING
    }
}
