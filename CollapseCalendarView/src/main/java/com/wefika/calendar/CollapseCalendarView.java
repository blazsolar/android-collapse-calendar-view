package com.wefika.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wefika.calendar.manager.CalendarManager;
import com.wefika.calendar.manager.Day;
import com.wefika.calendar.manager.Formatter;
import com.wefika.calendar.manager.Month;
import com.wefika.calendar.manager.ResizeManager;
import com.wefika.calendar.manager.Week;
import com.wefika.calendar.widget.DayView;
import com.wefika.calendar.widget.WeekView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Blaz Solar on 28/02/14.
 */
public class CollapseCalendarView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "CalendarView";

    @Nullable private CalendarManager mManager;

    @NonNull private TextView mTitleView;
    @NonNull private ImageButton mPrev;
    @NonNull private ImageButton mNext;
    @NonNull private LinearLayout mWeeksView;

    @NonNull private final LayoutInflater mInflater;
    @NonNull private final RecycleBin mRecycleBin = new RecycleBin();

    @Nullable private OnDateSelect mListener;

    @NonNull private TextView mSelectionText;
    @NonNull private LinearLayout mHeader;

    @NonNull private ResizeManager mResizeManager;

    private boolean initialized;

    public CollapseCalendarView(Context context) {
        this(context, null);
    }

    public CollapseCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public CollapseCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mInflater = LayoutInflater.from(context);

        mResizeManager = new ResizeManager(this);

        inflate(context, R.layout.calendar_layout, this);

        setOrientation(VERTICAL);
    }

    public void init(@NonNull CalendarManager manager) {
        if (manager != null) {

            mManager = manager;

            populateLayout();

            if (mListener != null) {
                mListener.onDateSelected(mManager.getSelectedDay());
            }

        }
    }

    @Nullable
    public CalendarManager getManager() {
        return mManager;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "On click");
        if (mManager != null) {
            int id = v.getId();
            if (id == R.id.prev) {
                if (mManager.prev()) {
                    populateLayout();
                }
            } else if (id == R.id.next) {
                Log.d(TAG, "next");
                if (mManager.next()) {
                    Log.d(TAG, "populate");
                    populateLayout();
                }
            }

        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        mResizeManager.onDraw();

        super.dispatchDraw(canvas);
    }

    @Nullable
    public CalendarManager.State getState() {
        if (mManager != null) {
           return mManager.getState();
        } else {
            return null;
        }
    }

    public void setListener(@Nullable OnDateSelect listener) {
        mListener = listener;
    }

    /**
     * @deprecated This will be removed
     */
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
    public boolean onTouchEvent(@NonNull MotionEvent event) {
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

        populateLayout();
    }

    private void populateDays() {

        if (!initialized) {
            CalendarManager manager = getManager();

            if (manager != null) {
                Formatter formatter = manager.getFormatter();

                LinearLayout layout = (LinearLayout) findViewById(R.id.days);

                LocalDate date = LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY);
                for (int i = 0; i < 7; i++) {
                    TextView textView = (TextView) layout.getChildAt(i);
                    textView.setText(formatter.getDayName(date));

                    date = date.plusDays(1);
                }

                initialized = true;
            }
        }

    }

    public void populateLayout() {

        if (mManager != null) {

            populateDays();

            mPrev.setEnabled(mManager.hasPrev());
            mNext.setEnabled(mManager.hasNext());

            mTitleView.setText(mManager.getHeaderText());

            if (mManager.getState() == CalendarManager.State.MONTH) {
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

    private void populateWeekLayout(@NonNull Week week, @NonNull WeekView weekView) {

        List<Day> days = week.getDays();
        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            DayView dayView = (DayView) weekView.getChildAt(i);

            dayView.setText(day.getText());
            dayView.setSelected(day.isSelected());
            dayView.setCurrent(day.isCurrent());

            boolean enables = day.isEnabled();
            dayView.setEnabled(enables);

            if (enables) {
                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDate date = day.getDate();
                        if (mManager.selectDay(date)) {
                            populateLayout();
                            if (mListener != null) {
                                mListener.onDateSelected(date);
                            }
                        }
                    }
                });
            } else {
                dayView.setOnClickListener(null);
            }
        }

    }

    @NonNull
    public LinearLayout getWeeksView() {
        return mWeeksView;
    }

    @NonNull
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

        public void addView(@NonNull View view) {
            mViews.add(view);
        }

    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }

}
