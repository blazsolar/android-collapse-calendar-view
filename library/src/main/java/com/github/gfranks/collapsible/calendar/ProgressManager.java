package com.github.gfranks.collapsible.calendar;

import android.widget.LinearLayout;

import com.github.gfranks.collapsible.calendar.viewholder.AbstractViewHolder;
import com.github.gfranks.collapsible.calendar.viewholder.SizeViewHolder;

abstract class ProgressManager {

    private static final String TAG = "ProgressManager";
    final int mActiveIndex;
    final boolean mFromMonth;
    protected CollapsibleCalendarView mCalendarView;
    protected LinearLayout mWeeksView;
    protected AbstractViewHolder[] mViews;
    protected SizeViewHolder mCalendarHolder;
    protected SizeViewHolder mWeeksHolder;
    private OnInitListener mListener;
    private boolean mInitialized = false;

    public ProgressManager(CollapsibleCalendarView calendarView, int activeWeek, boolean fromMonth) {
        mCalendarView = calendarView;
        mWeeksView = calendarView.getWeeksView();
        mActiveIndex = activeWeek;
        mFromMonth = fromMonth;
    }

    public void setListener(OnInitListener listener) {
        mListener = listener;
    }

    public void applyDelta(float delta) {
        float progress = getProgress(getDeltaInBounds(delta));
        apply(progress);
    }

    public void apply(float progress) {
        mCalendarHolder.animate(progress);
        mWeeksHolder.animate(progress);

        // animate views if necessary
        if (mViews != null) {
            for (AbstractViewHolder view : mViews) {
                view.animate(progress);
            }
        }

        // request layout
        mCalendarView.requestLayout();

    }

    public boolean isInitialized() {
        return mInitialized;
    }

    void setInitialized(boolean initialized) {
        mInitialized = initialized;
        if (mListener != null && initialized) {
            mListener.onInit();
        }
    }

    public int getCurrentHeight() {
        return mCalendarView.getLayoutParams().height - mCalendarHolder.getMinHeight();
    }

    public int getStartSize() {
        return 0;
    }

    public int getEndSize() {
        return mCalendarHolder.getHeight();
    }

    public abstract void finish(boolean expanded);

    public float getProgress(int distance) {
        return Math.max(0, Math.min(distance * 1f / mCalendarHolder.getHeight(), 1));
    }

    protected int getActiveIndex() {
        return mActiveIndex;
    }

    private int getDeltaInBounds(float delta) {
        if (mFromMonth) {
            return (int) Math.max(-mCalendarHolder.getHeight(), Math.min(0, delta)) + mCalendarHolder.getHeight();
        } else {
            return (int) Math.max(0, Math.min(mCalendarHolder.getHeight(), delta));
        }
    }

    public interface OnInitListener {
        void onInit();
    }
}
