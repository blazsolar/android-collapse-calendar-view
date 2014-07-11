package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Created by Blaz Solar on 24/05/14.
 */
public abstract class RangeUnit extends CalendarUnit {

    @Nullable private LocalDate mMinDate;
    @Nullable private LocalDate mMaxDate;

    protected RangeUnit(@NotNull LocalDate from, @NotNull LocalDate to, @NotNull String headerPattern,
                        @NotNull LocalDate today, @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        super(from, to, headerPattern, today);

        mMinDate = minDate;
        mMaxDate = maxDate;
    }

    @Nullable
    public LocalDate getMinDate() {
        return mMinDate;
    }

    @Nullable
    public LocalDate getMaxDate() {
        return mMaxDate;
    }

    public int getFirstWeek(LocalDate currentMonth) {
        LocalDate from = getFrom();
        if (mMinDate != null && mMinDate.isAfter(from)) { // TODO check if same month
            return getWeekInMonth(mMinDate);
        } else {
            return getWeekInMonth(currentMonth);
        }
    }

    LocalDate getFirstEnabled() {
        LocalDate from = getFrom();
        if (mMinDate != null && from.isBefore(mMinDate)) {
            return mMinDate;
        } else {
            return from;
        }
    }

    @Nullable
    abstract LocalDate getFirstDateOfCurrentMonth(@NotNull LocalDate currentMonth);

    protected int getWeekInMonth(@NotNull LocalDate date) {
        if (date != null) {
            LocalDate first = date.withDayOfMonth(1).withDayOfWeek(1);
            Days days = Days.daysBetween(first, date);
            return days.dividedBy(7).getDays();
        } else {
            return 0;
        }
    }
}
