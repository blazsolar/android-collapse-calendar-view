package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

/**
 * Created by Blaž Šolar on 24/05/14.
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

    public int getFirstWeek() {
        if (mMinDate != null && mMinDate.isAfter(getFrom())) { // TODO check if same month
            return getWeekInMonth(mMinDate);
        } else {
            return 0;
        }
    }

    LocalDate getTootleTo() {
        LocalDate from = getFrom();
        if (mMinDate != null && from.isBefore(mMinDate)) {
            return mMinDate;
        } else {
            return from;
        }
    }
}
