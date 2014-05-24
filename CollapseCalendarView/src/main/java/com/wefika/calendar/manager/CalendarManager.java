package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

/**
 * Created by Blaž Šolar on 27/02/14.
 */
public class CalendarManager {

    @NotNull private State mState;
    @NotNull private RangeUnit mUnit;
    @NotNull private LocalDate mSelected;
    @NotNull private final LocalDate mToday;
    @Nullable private LocalDate mMinDate;
    @Nullable private LocalDate mMaxDate;

    private LocalDate mActiveMonth;

    public CalendarManager(@NotNull LocalDate selected, @NotNull State state, @Nullable LocalDate minDate,
                           @Nullable LocalDate maxDate) {
        mToday = LocalDate.now();
        mState = state;

        init(selected, minDate, maxDate);
    }

    public boolean selectDay(@NotNull LocalDate date) {
        if (!mSelected.isEqual(date)) {
            mUnit.deselect(mSelected);
            mSelected = date;
            mUnit.select(mSelected);

            if (mState == State.WEEK) {
                mActiveMonth = date;
            }
            return true;
        } else {
            return false;
        }
    }

    @NotNull
    public LocalDate getSelectedDay() {
        return mSelected;
    }

    @NotNull
    public String getHeaderText() {
        return mUnit.getHeaderText();
    }

    public boolean hasNext() {
        return mUnit.hasNext();
    }

    public boolean hasPrev() {
        return mUnit.hasPrev();
    }

    public boolean next() {

        boolean next = mUnit.next();
        mUnit.select(mSelected);

        mActiveMonth = mUnit.getFrom();

        return next;
    }

    public boolean prev() {

        boolean prev = mUnit.prev();
        mUnit.select(mSelected);

        mActiveMonth = mUnit.getTo();

        return prev;
    }

    /**
     *
     * @return index of month to focus to
     */
    public void toggleView() {

        if(mState == State.MONTH) {
            toggleFromMonth();
        } else {
            toggleFromWeek();
        }

    }

    @NotNull
    public State getState() {
        return mState;
    }

    public CalendarUnit getUnits() {
        return mUnit;
    }

    public LocalDate getActiveMonth() {
        return mActiveMonth;
    }

    private void toggleFromMonth() {

        // if same month as selected
        if (mUnit.isInView(mSelected)) {
            mUnit = new Week(mSelected, mToday, mMinDate, mMaxDate);
            mUnit.select(mSelected);

            mActiveMonth = mSelected;
        } else {
            mActiveMonth = mUnit.getFrom();
            mUnit = new Week(mUnit.getTootleTo(), mToday, mMinDate, mMaxDate);
        }

        mState = State.WEEK;
    }

    private void toggleFromWeek() {

        mUnit = new Month(mActiveMonth, mToday, mMinDate, mMaxDate);
        mUnit.select(mSelected);

        mState = State.MONTH;
    }

    private void init() {
        if (mState == State.MONTH) {
            mUnit = new Month(mSelected, mToday, mMinDate, mMaxDate);
        } else {
            mUnit = new Week(mSelected, mToday, mMinDate, mMaxDate);
        }
        mUnit.select(mSelected);
    }

    public int getWeekOfMonth() {
        if(mUnit.isInView(mSelected)) {
            return mUnit.getWeekInMonth(mSelected);
        } else {
            return mUnit.getFirstWeek(); // if not in this month first week should be selected
        }
    }

    public void init(@NotNull LocalDate date, @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        mSelected = date;
        mActiveMonth = date;
        mMinDate = minDate;
        mMaxDate = maxDate;

        init();
    }

    public enum State {
        MONTH,
        WEEK
    }

}
