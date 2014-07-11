package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Blaz Solar on 27/02/14.
 */
public abstract class CalendarUnit {

    private final DateTimeFormatter mHeaderFormat;
    private final LocalDate mToday;
    private LocalDate mFrom;
    private LocalDate mTo;
    private boolean mSelected;

    protected CalendarUnit(@NotNull LocalDate from, @NotNull LocalDate to,
                           @NotNull String headerPattern, @NotNull LocalDate today) {
        mToday = today;
        mFrom = from;
        mTo = to;
        mHeaderFormat = DateTimeFormat.forPattern(headerPattern);
    }

    public LocalDate getToday() {
        return mToday;
    }

    public LocalDate getFrom() {
        return mFrom;
    }

    public LocalDate getTo() {
        return mTo;
    }

    protected void setFrom(@NotNull LocalDate from) {
        mFrom = from;
    }

    protected void setTo(@NotNull LocalDate to) {
        mTo = to;
    }

    protected void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public abstract boolean hasNext();

    public abstract boolean hasPrev();

    public abstract boolean next();

    public abstract boolean prev();

    public boolean isIn(@NotNull LocalDate date) {
        return !mFrom.isAfter(date) && !mTo.isBefore(date);
    }

    public boolean isInView(@NotNull LocalDate date) {
        return !mFrom.withDayOfWeek(DateTimeConstants.MONDAY).isAfter(date)
                && !mTo.plusWeeks(1).withDayOfWeek(DateTimeConstants.SUNDAY).isBefore(date);
    }

    public abstract void deselect(@NotNull LocalDate date);

    public abstract boolean select(@NotNull LocalDate date);

    public abstract void build();

    public String getHeaderText() {
        return mFrom.toString(mHeaderFormat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarUnit)) return false;

        CalendarUnit that = (CalendarUnit) o;

        if (mSelected != that.mSelected) return false;
        if (!mFrom.equals(that.mFrom)) return false;
        if (!mHeaderFormat.equals(that.mHeaderFormat)) return false;
        if (!mTo.equals(that.mTo)) return false;
        if (!mToday.equals(that.mToday)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mHeaderFormat.hashCode();
        result = 31 * result + mToday.hashCode();
        result = 31 * result + mFrom.hashCode();
        result = 31 * result + mTo.hashCode();
        result = 31 * result + (mSelected ? 1 : 0);
        return result;
    }
}
