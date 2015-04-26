package com.wefika.calendar.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Created by Blaz Solar on 24/05/14.
 */
public abstract class RangeUnit extends CalendarUnit {

    @Nullable private LocalDate mMinDate;
    @Nullable private LocalDate mMaxDate;

    protected RangeUnit(@NonNull LocalDate from, @NonNull LocalDate to, @NonNull LocalDate today,
            @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        super(from, to, today);

        if (minDate != null && maxDate != null && minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("Min date should be before max date");
        }
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

    /**
     *
     *
     * @param firstDayOfMonth First day of current month in range unit
     * @return Week of month of first enabled date, 0 if no dates are enabled.
     */
    public int getFirstWeek(@Nullable LocalDate firstDayOfMonth) {
        LocalDate from = getFrom();
        LocalDate date;
        if (mMinDate != null && mMinDate.isAfter(from)) { // TODO check if same month
            date = mMinDate;
        } else {
            date = firstDayOfMonth;
        }

        return getWeekInMonth(date);
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
    abstract LocalDate getFirstDateOfCurrentMonth(@NonNull LocalDate currentMonth);

    protected int getWeekInMonth(@Nullable LocalDate date) {
        if (date != null) {
            LocalDate first = date.withDayOfMonth(1).withDayOfWeek(DateTimeConstants.MONDAY);
            Days days = Days.daysBetween(first, date);
            return days.dividedBy(DateTimeConstants.DAYS_PER_WEEK).getDays();
        } else {
            return 0;
        }
    }
}
