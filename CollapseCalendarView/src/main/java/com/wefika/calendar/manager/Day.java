package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Blaž Šolar on 24/02/14.
 */
public class Day {

    private static final DateTimeFormatter mFormatter = DateTimeFormat.forPattern("d");

    @NotNull private final LocalDate mDate;
    private boolean mToday;
    private boolean mSelected;
    private boolean mEnabled;

    public Day(@NotNull LocalDate date, boolean today) {
        mDate = date;
        mToday = today;
        mEnabled = true;
    }

    @NotNull
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

    @NotNull
    public String getText() {
        return mDate.toString(mFormatter);
    }
}
