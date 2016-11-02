package com.github.gfranks.collapsible.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.gfranks.collapsible.calendar.model.CollapsibleCalendarEvent;
import com.github.gfranks.collapsible.calendar.model.CollapsibleState;
import com.github.gfranks.collapsible.calendar.model.Day;
import com.github.gfranks.collapsible.calendar.model.Formatter;
import com.github.gfranks.collapsible.calendar.model.Month;
import com.github.gfranks.collapsible.calendar.model.Week;
import com.github.gfranks.collapsible.calendar.widget.DayView;
import com.github.gfranks.collapsible.calendar.widget.WeekView;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CollapsibleCalendarView extends LinearLayout implements View.OnClickListener, View.OnTouchListener {

    private final LayoutInflater mInflater;
    private final RecycleBin mRecycleBin = new RecycleBin();

    private CalendarManager mManager;
    private TextView mTitleView;
    private TextView mSelectionTitleView;
    private ImageButton mPrev;
    private ImageButton mNext;
    private LinearLayout mWeeksView;
    private int mArrowColor = -1;
    private Drawable mPrevArrowRes;
    private Drawable mNextArrowRes;
    private int mHeaderTextColor = Color.DKGRAY;
    private boolean mHeaderBold = false;
    private int mWeekDayTextColor = Color.DKGRAY;
    private boolean mWeekBold = false;
    private int mDayTextColor = Color.DKGRAY;
    private int mEventIndicatorColor = Color.RED;
    private int mSelectedDayTextColor = Color.WHITE;
    private int mSelectedDayBackgroundColor = Color.DKGRAY;
    private boolean mSmallHeader = false;
    private boolean mNoHeader = false;
    private boolean mShowInactiveDays = true;
    private boolean mAllowStateChange = true;
    private boolean mDisableSwipe = true;
    private Listener mListener;
    private LinearLayout mHeader;
    private ResizeManager mResizeManager;
    private boolean mInitialized;
    private final GestureDetector mGestureDetector;
    CollapsibleState startingState = CollapsibleState.MONTH;
    public CollapsibleCalendarView(Context context) {
        this(context, null);
    }

    public CollapsibleCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public CollapsibleCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CollapsibleCalendarView, 0, 0);
            try {
                startingState = CollapsibleState.values()[typedArray.getInt(R.styleable.CollapsibleCalendarView_ccv_state, startingState.ordinal())];
                mArrowColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_arrowColor, mArrowColor);
                mPrevArrowRes = typedArray.getDrawable(R.styleable.CollapsibleCalendarView_ccv_prevArrowSrc);
                mNextArrowRes = typedArray.getDrawable(R.styleable.CollapsibleCalendarView_ccv_nextArrowSrc);
                mHeaderTextColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_headerTextColor, mHeaderTextColor);
                mHeaderBold = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_boldHeaderText, mHeaderBold);
                mWeekDayTextColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_weekDayTextColor, mWeekDayTextColor);
                mWeekBold = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_boldWeekDayText, mWeekBold);
                mDayTextColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_dayTextColor, mDayTextColor);
                mEventIndicatorColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_eventIndicatorColor, mEventIndicatorColor);
                mSelectedDayTextColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_selectedDayTextColor, mSelectedDayTextColor);
                mSelectedDayBackgroundColor = typedArray.getColor(R.styleable.CollapsibleCalendarView_ccv_selectedDayBackgroundColor, mSelectedDayBackgroundColor);
                mSmallHeader = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_smallHeader, mSmallHeader);
                mNoHeader = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_noHeader, mNoHeader);
                mShowInactiveDays = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_showInactiveDays, mShowInactiveDays);
                mAllowStateChange = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_allowStateChange, mAllowStateChange);
                mDisableSwipe = typedArray.getBoolean(R.styleable.CollapsibleCalendarView_ccv_disableSwipe, mDisableSwipe);
            } finally {
                typedArray.recycle();
            }
        }

        mManager = new CalendarManager(LocalDate.now(), startingState, LocalDate.now(), LocalDate.now().plusYears(1));
        mInflater = LayoutInflater.from(context);
        mResizeManager = new ResizeManager(this);
        inflate(context, R.layout.calendar_layout, this);
        setOrientation(VERTICAL);

        mGestureDetector = new GestureDetector(context, new GestureListener());
        setOnTouchListener(this);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        mResizeManager.onDraw();
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mResizeManager.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);
        return mResizeManager.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDisableSwipe) {
            return true;
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (TextView) findViewById(R.id.title);
        if (mHeaderBold) {
            mTitleView.setTypeface(null, Typeface.BOLD);
        }
        mPrev = (ImageButton) findViewById(R.id.prev);
        mNext = (ImageButton) findViewById(R.id.next);
        mWeeksView = (LinearLayout) findViewById(R.id.weeks);

        mHeader = (LinearLayout) findViewById(R.id.header);
        if (mNoHeader) {
            mHeader.setVisibility(View.GONE);
        }
        mSelectionTitleView = (TextView) findViewById(R.id.selection_title);
        if (mHeaderBold) {
            mSelectionTitleView.setTypeface(null, Typeface.BOLD);
        }

        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mTitleView.setOnClickListener(this);
        mSelectionTitleView.setOnClickListener(this);

        setTitleColor(mHeaderTextColor);
        setSmallHeader(mSmallHeader);

        if (mArrowColor != -1) {
            setArrowColor(mArrowColor);
        }

        if (mPrevArrowRes != null) {
            setPrevArrowImageDrawable(mPrevArrowRes);
        }

        if (mNextArrowRes != null) {
            setNextArrowImageDrawable(mNextArrowRes);
        }

        populateLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mResizeManager.recycle();
    }

    @Override
    public void onClick(View v) {
        if (mManager != null) {
            int id = v.getId();
            if (id == R.id.prev) {
                prev();
            } else if (id == R.id.next) {
                next();
            } else if (id == R.id.title || id == R.id.selection_title) {
                if (mListener != null) {
                    mListener.onHeaderClick();
                }
            }
        }
    }

    public void setEvents(List<? extends CollapsibleCalendarEvent> events) {
        mManager.setEvents(events);
        populateLayout();

        if (mListener != null) {
            mListener.onDateSelected(getSelectedDate(), mManager.getEventsForDate(getSelectedDate()));
        }
    }

    public void addEvents(List<? extends CollapsibleCalendarEvent> events) {
        mManager.addEvents(events);
        populateLayout();

        if (mListener != null) {
            mListener.onDateSelected(getSelectedDate(), mManager.getEventsForDate(getSelectedDate()));
        }
    }

    public void addEvent(CollapsibleCalendarEvent event) {
        mManager.addEvent(event);
        populateLayout();

        if (mListener != null) {
            mListener.onDateSelected(getSelectedDate(), mManager.getEventsForDate(getSelectedDate()));
        }
    }

    public void removeEvent(CollapsibleCalendarEvent event) {
        mManager.removeEvent(event);
        populateLayout();

        if (mListener != null) {
            mListener.onDateSelected(getSelectedDate(), mManager.getEventsForDate(getSelectedDate()));
        }
    }

    public List<? extends CollapsibleCalendarEvent> getEventsForDate(LocalDate date) {
        return mManager.getEventsForDate(date);
    }

    public LocalDate getMinDate() {
        return getManager().getMinDate();
    }

    public void setMinDate(LocalDate minDate) {
        getManager().setMinDate(minDate);
        reload_manager();
    }

    public LocalDate getMaxDate() {
        return getManager().getMaxDate();
    }

    public void setMaxDate(LocalDate maxDate) {
        getManager().setMaxDate(maxDate);
        reload_manager();
    }

    public void setTitle(String text) {
        if (mNoHeader) {
            return;
        }

        if (text == null || text.length() == 0) {
            mHeader.setVisibility(View.VISIBLE);
            mSelectionTitleView.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.GONE);
            mSelectionTitleView.setVisibility(View.VISIBLE);
            mSelectionTitleView.setText(text);
        }
    }

    public void setTitleColor(int color) {
        mHeaderTextColor = color;
        mTitleView.setTextColor(mHeaderTextColor);
        mSelectionTitleView.setTextColor(mHeaderTextColor);
    }

    public void setBoldHeaderText(boolean headerBold) {
        mHeaderBold = headerBold;
        populateLayout();
    }

    public void setArrowColor(int color) {
        mArrowColor = color;
        mPrev.setColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN);
        mNext.setColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN);
    }

    public void setPrevArrowImageResource(int resId) {
        mPrev.setImageResource(resId);
    }

    public void setPrevArrowImageDrawable(Drawable drawable) {
        mPrev.setImageDrawable(drawable);
    }

    public void setNextArrowImageResource(int resId) {
        mNext.setImageResource(resId);
    }

    public void setNextArrowImageDrawable(Drawable drawable) {
        mNext.setImageDrawable(drawable);
    }

    public void setWeekDayTextColor(int color) {
        mWeekDayTextColor = color;
        populateLayout();
    }

    public void setBoldWeeDayText(boolean weekBold) {
        mWeekBold = weekBold;
        populateLayout();
    }

    public void setDayTextColor(int color) {
        mDayTextColor = color;
        populateLayout();
    }

    public void setEventIndicatorColor(int color) {
        mEventIndicatorColor = color;
        populateLayout();
    }

    public void setSelectedDayTextColor(int color) {
        mSelectedDayTextColor = color;
        populateLayout();
    }

    public void setSelectedDayBackgroundColor(int color) {
        mSelectedDayBackgroundColor = color;
        populateLayout();
    }

    public void setSmallHeader(boolean smallHeader) {
        mSmallHeader = smallHeader;
        int textSize = 20;
        if (mSmallHeader) {
            textSize = 14;
        }

        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        mSelectionTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setShowInactiveDays(boolean showInactiveDays) {
        mShowInactiveDays = showInactiveDays;
        populateLayout();
    }

    public boolean isAllowStateChange() {
        return mAllowStateChange;
    }

    public void setAllowStateChange(boolean allowStateChange) {
        mAllowStateChange = allowStateChange;
    }

    public void disableSwipe(boolean disableSwipe) {
        mDisableSwipe = disableSwipe;
    }

    public CalendarManager getManager() {
        return mManager;
    }

    public void setFormatter(Formatter formatter) {
        mManager.setFormatter(formatter);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public CollapsibleState getState() {
        return mManager.getState();
    }

    public String getHeaderText() {
        return mManager.getHeaderText();
    }

    public void next() {
        if (mManager.next()) {
            populateLayout();

            if (mListener != null) {
                mListener.onMonthChanged(mManager.getActiveMonth());
            }
        }
    }

    public void prev() {
        if (mManager.prev()) {
            populateLayout();

            if (mListener != null) {
                mListener.onMonthChanged(mManager.getActiveMonth());
            }
        }
    }

    public void selectDate(LocalDate date) {
        boolean period = mManager.selectPeriod(date);
        boolean day = mManager.selectDay(date);

        if (period || day) {
            populateLayout();
        }

        if (day && mListener != null) {
            mListener.onDateSelected(date, mManager.getEventsForDate(date));
        }
    }

    public LocalDate getSelectedDate() {
        return mManager.getSelectedDay();
    }

    public void toggle() {
        mResizeManager.toggle();
    }

    public void populateLayout() {
        if (mManager != null) {
            populateDays();

            mPrev.setEnabled(mManager.hasPrev());
            mNext.setEnabled(mManager.hasNext());

            mTitleView.setText(mManager.getHeaderText());

            if (mManager.getState() == CollapsibleState.MONTH) {
                populateMonthLayout((Month) mManager.getUnits());
            } else {
                populateWeekLayout((Week) mManager.getUnits());
            }
        }
    }

    private void populateMonthLayout(Month month) {

        List<Week> weeks = month.getWeeks();
        int cnt = weeks.size();
        for (int i = 0; i < cnt; i++) {
            WeekView weekView = getWeekView(i);
            populateWeekLayout(weeks.get(i), weekView);
        }

        int childCnt = mWeeksView.getChildCount();
        if (cnt < childCnt) {
            for (int i = cnt; i < childCnt; i++) {
                cacheView(i);
            }
        }

    }

    private void populateWeekLayout(Week week) {
        WeekView weekView = getWeekView(0);
        populateWeekLayout(week, weekView);

        int cnt = mWeeksView.getChildCount();
        if (cnt > 1) {
            for (int i = cnt - 1; i > 0; i--) {
                cacheView(i);
            }
        }
    }

    private void populateWeekLayout(Week week, WeekView weekView) {
        List<Day> days = week.getDays();
        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            DayView dayView = (DayView) weekView.getChildAt(i);

            dayView.setText(day.getText());
            if (day.getDate().getValue(1) != mManager.getActiveMonth().getValue(1) && getState() == CollapsibleState.MONTH) {
                if (mShowInactiveDays) {
                    dayView.setAlpha(0.5f);
                    dayView.setVisibility(View.VISIBLE);
                } else {
                    dayView.setVisibility(View.INVISIBLE);
                }
            } else {
                dayView.setAlpha(1f);
                dayView.setVisibility(View.VISIBLE);
            }
            dayView.setSelected(day.isSelected(), mSelectedDayBackgroundColor, mSelectedDayTextColor, mDayTextColor);
            dayView.setCurrent(day.isCurrent());
            dayView.setHasEvent(mManager.dayHasEvent(day));
            dayView.setEventIndicatorColor(mEventIndicatorColor);

            boolean enabled = day.isEnabled();
            dayView.setEnabled(enabled);

            if (enabled && (getState() == CollapsibleState.WEEK || day.getDate().getValue(1) == mManager.getActiveMonth().getValue(1))) {
                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDate date = day.getDate();
                        if (mManager.selectDay(date)) {
                            populateLayout();
                            if (mListener != null) {
                                mListener.onDateSelected(date, mManager.getEventsForDate(date));
                            }
                        }
                    }
                });
            } else {
                dayView.setOnClickListener(null);
            }
        }
    }

    private void populateDays() {
        if (!mInitialized) {
            if (mManager != null) {
                Formatter formatter = mManager.getFormatter();

                LinearLayout layout = (LinearLayout) findViewById(R.id.days);

                LocalDate date = LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY);
                for (int i = 0; i < 7; i++) {
                    TextView textView = (TextView) layout.getChildAt(i);
                    textView.setText(formatter.getDayName(date));
                    textView.setTextColor(mWeekDayTextColor);
                    if (mWeekBold) {
                        textView.setTypeface(null, Typeface.BOLD);
                    }
                    date = date.plusDays(1);
                }

                mInitialized = true;
            }
        }
    }

    private WeekView getWeekView(int index) {
        int cnt = mWeeksView.getChildCount();

        if (cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                View view = getView();
                mWeeksView.addView(view);
            }
        }

        return (WeekView) mWeeksView.getChildAt(index);
    }

    private View getView() {
        View view = mRecycleBin.recycleView();
        if (view == null) {
            view = mInflater.inflate(R.layout.week_layout, this, false);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void cacheView(int index) {
        View view = mWeeksView.getChildAt(index);
        if (view != null) {
            mWeeksView.removeViewAt(index);
            mRecycleBin.addView(view);
        }
    }

    LinearLayout getWeeksView() {
        return mWeeksView;
    }

    public interface Listener<T extends CollapsibleCalendarEvent> {
        void onDateSelected(LocalDate date, List<T> events);

        void onMonthChanged(LocalDate date);

        void onHeaderClick();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) {
                        prev();
                    } else {
                        next();
                    }
                    return true;
                }
                return false;
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return false;
        }
    }


    private void reload_manager(){
        if (mManager != null) {
            mManager = new CalendarManager(LocalDate.now(), startingState, getManager().getMinDate(), getManager().getMaxDate());

        }
    }

    private class RecycleBin {
        private final Queue<View> mViews = new LinkedList<>();

        public View recycleView() {
            return mViews.poll();
        }

        public void addView(View view) {
            mViews.add(view);
        }
    }
}
