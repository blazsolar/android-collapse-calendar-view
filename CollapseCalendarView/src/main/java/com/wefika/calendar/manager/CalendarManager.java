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
                setActiveMonth(date);
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

        setActiveMonth(mUnit.getFrom());

        return next;
    }

    public boolean prev() {

        boolean prev = mUnit.prev();
        mUnit.select(mSelected);

        setActiveMonth(mUnit.getTo());

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

    private void setActiveMonth(LocalDate activeMonth) {
        mActiveMonth = activeMonth.withDayOfMonth(1);
    }

    private void toggleFromMonth() {

        // if same month as selected
        if (mUnit.isInView(mSelected)) {
            toggleFromMonth(mSelected);

            setActiveMonth(mSelected);
        } else {
            setActiveMonth(mUnit.getFrom());
            toggleFromMonth(mUnit.getFirstDateOfCurrentMonth(mActiveMonth));
        }
    }

    void toggleToWeek(int weekInMonth) {
        LocalDate date = mUnit.getFrom().plusDays(weekInMonth * 7);
        toggleFromMonth(date);
    }

    private void toggleFromMonth(LocalDate date) {
        mUnit = new Week(date, mToday, mMinDate, mMaxDate);
        mUnit.select(mSelected);
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
            if (mUnit.isIn(mSelected)) { // TODO not pretty
                return mUnit.getWeekInMonth(mSelected);
            } else if (mUnit.getFrom().isAfter(mSelected)) {
                return mUnit.getWeekInMonth(mUnit.getFrom());
            } else {
                return mUnit.getWeekInMonth(mUnit.getTo());
            }
        } else {
            return mUnit.getFirstWeek(mUnit.getFirstDateOfCurrentMonth(mActiveMonth)); // if not in this month first week should be selected
        }
    }

    public void init(@NotNull LocalDate date, @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        mSelected = date;
        setActiveMonth(date);
        mMinDate = minDate;
        mMaxDate = maxDate;

        init();
    }

    public enum State {
        MONTH,
        WEEK
    }

}
