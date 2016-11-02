package com.github.gfranks.collapsible.calendar.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Day {

    private static final DateTimeFormatter mFormatter = DateTimeFormat.forPattern("d");

    private final LocalDate mDate;
    private boolean mToday;
    private boolean mSelected;
    private boolean mEnabled;
    private boolean mCurrent;

    public Day(LocalDate date, boolean today) {
        mDate = date;
        mToday = today;
        mEnabled = true;
        mCurrent = true;
    }

    public LocalDate getDate() {
        return mDate;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public void setCurrent(boolean current) {
        mCurrent = current;
    }

    public boolean isToday() {
        return mToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        if (mEnabled != day.mEnabled) return false;
        if (mSelected != day.mSelected) return false;
        if (mToday != day.mToday) return false;
        if (!mDate.isEqual(day.mDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mDate.hashCode();
        result = 31 * result + (mToday ? 1 : 0);
        result = 31 * result + (mSelected ? 1 : 0);
        result = 31 * result + (mEnabled ? 1 : 0);
        return result;
    }

    public String getText() {
        return mDate.toString(mFormatter);
    }
}
