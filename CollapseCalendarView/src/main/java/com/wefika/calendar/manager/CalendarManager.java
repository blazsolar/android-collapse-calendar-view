package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Created by Blaž Šolar on 27/02/14.
 */
public class CalendarManager {

    @NotNull private State mState;
    @NotNull private CalendarUnit mUnit;
    @NotNull private LocalDate mSelected;
    @NotNull private final LocalDate mToday;

    private LocalDate mActiveMonth;

    public CalendarManager(@NotNull LocalDate selected, @NotNull State state) {
        mToday = LocalDate.now();
        mState = state;

        init(selected);
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
            mUnit = new Week(mSelected, mToday);
            mUnit.select(mSelected);

            mActiveMonth = mSelected;
        } else {
            mActiveMonth = mUnit.getFrom();
            mUnit = new Week(mUnit.getFrom(), mToday);
        }

        mState = State.WEEK;
    }

    private void toggleFromWeek() {

        mUnit = new Month(mActiveMonth, mToday);
        mUnit.select(mSelected);

        mState = State.MONTH;
    }

    private void init() {
        if (mState == State.MONTH) {
            mUnit = new Month(mSelected, mToday);
        } else {
            mUnit = new Week(mSelected, mToday);
        }
        mUnit.select(mSelected);
    }

    public int getWeekOfMonth() {
        if(mUnit.isInView(mSelected)) {
            LocalDate first = mUnit.getFrom().withDayOfMonth(1).withDayOfWeek(1);
            Days days = Days.daysBetween(first, mSelected);
            return days.dividedBy(7).getDays();
        } else {
            return 0; // if not in this month first week should be selected
        }
    }

    public void init(LocalDate date) {
        mSelected = date;
        mActiveMonth = date;

        init();
    }

    public enum State {
        MONTH,
        WEEK
    }

}
