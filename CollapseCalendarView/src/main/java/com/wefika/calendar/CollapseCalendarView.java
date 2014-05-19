package com.wefika.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wefika.calendar.manager.CalendarManager;
import com.wefika.calendar.manager.Day;
import com.wefika.calendar.manager.Month;
import com.wefika.calendar.manager.ResizeManager;
import com.wefika.calendar.manager.Week;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Blaž Šolar on 28/02/14.
 */
public class CollapseCalendarView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "CalendarView";

    @NotNull private final CalendarManager mManager;

    @NotNull private TextView mTitleView;
    @NotNull private ImageButton mPrev;
    @NotNull private ImageButton mNext;
    @NotNull private LinearLayout mWeeksView;

    @NotNull private final LayoutInflater mInflater;
    @NotNull private final RecycleBin mRecycleBin = new RecycleBin();

    @Nullable private OnDateSelect mListener;

    @NotNull private TextView mSelectionText;
    @NotNull private LinearLayout mHeader;

    @NotNull private ResizeManager mResizeManager;

    public CollapseCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public CollapseCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mManager = new CalendarManager(LocalDate.now(), CalendarManager.State.WEEK);
        mInflater = LayoutInflater.from(context);

        mResizeManager = new ResizeManager(this);

        inflate(context, R.layout.calendar_layout, this);
    }

    public void init(@NotNull LocalDate date) {
        mManager.init(date);
        populateLayout();
        if (mListener != null) {
            mListener.onDateSelected(date);
        }
    }

    @NotNull
    public CalendarManager getManager() {
        return mManager;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.prev) {
            if (mManager.prev()) {
                populateLayout();
            }
        } else if (id == R.id.next) {
            if (mManager.next()) {
                populateLayout();
            }
        }
    }

    @Override
    protected void dispatchDraw(@NotNull Canvas canvas) {
        mResizeManager.onDraw();

        super.dispatchDraw(canvas);
    }

    public CalendarManager.State getState() {
        return mManager.getState();
    }

    public void setListener(@Nullable OnDateSelect listener) {
        mListener = listener;
    }

    public void setTitle(@Nullable String text) {
        if (StringUtils.isEmpty(text)) {
            mHeader.setVisibility(View.VISIBLE);
            mSelectionText.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.GONE);
            mSelectionText.setVisibility(View.VISIBLE);
            mSelectionText.setText(text);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mResizeManager.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        super.onTouchEvent(event);

        return mResizeManager.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (TextView) findViewById(R.id.title);
        mPrev = (ImageButton) findViewById(R.id.prev);
        mNext = (ImageButton) findViewById(R.id.next);
        mWeeksView = (LinearLayout) findViewById(R.id.weeks);

        mHeader = (LinearLayout) findViewById(R.id.header);
        mSelectionText = (TextView) findViewById(R.id.selection_title);

        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);

        populateDays();
        populateLayout();
    }

    private void populateDays() {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("E");

        LinearLayout layout = (LinearLayout) findViewById(R.id.days);

        LocalDate date = LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY);
        for (int i = 0; i < 7; i++) {
            TextView textView = (TextView) layout.getChildAt(i);
            textView.setText(date.toString(formatter));

            date = date.plusDays(1);
        }

    }

    public void populateLayout() {

        mPrev.setEnabled(mManager.hasPrev());
        mNext.setEnabled(mManager.hasNext());

        mTitleView.setText(mManager.getHeaderText());

        if (mManager.getState() == CalendarManager.State.MONTH) {
            populateMonthLayout((Month) mManager.getUnits());
        } else {
            populateWeekLayout((Week) mManager.getUnits());
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

    private void populateWeekLayout(@NotNull Week week, @NotNull WeekView weekView) {

        List<Day> days = week.getDays();
        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            TextView dayView = (TextView) weekView.getChildAt(i);

            dayView.setText(day.getText());
            dayView.setEnabled(day.isEnabled());
            dayView.setSelected(day.isSelected());
            dayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalDate date = day.getDate();
                    if(mManager.selectDay(date)) {
                        populateLayout();
                        if (mListener != null) {
                            mListener.onDateSelected(date);
                        }
                    }
                }
            });
        }

    }

    @NotNull
    public LinearLayout getWeeksView() {
        return mWeeksView;
    }

    @NotNull
    private WeekView getWeekView(int index) {
        int cnt = mWeeksView.getChildCount();

        if(cnt < index + 1) {
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
        if(view != null) {
            mWeeksView.removeViewAt(index);
            mRecycleBin.addView(view);
        }
    }

    public LocalDate getSelectedDate() {
        return mManager.getSelectedDay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mResizeManager.recycle();
    }

    private class RecycleBin {

        private final Queue<View> mViews = new LinkedList<>();

        @Nullable
        public View recycleView() {
            return mViews.poll();
        }

        public void addView(@NotNull View view) {
            mViews.add(view);
        }

    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }

}
