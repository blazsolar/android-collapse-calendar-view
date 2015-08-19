package com.github.gfranks.collapsible.calendar.model;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public abstract class RangeUnit extends CalendarUnit {

    private LocalDate mMinDate;
    private LocalDate mMaxDate;

    protected RangeUnit(LocalDate from,
                        LocalDate to,
                        LocalDate today,
                        LocalDate minDate,
                        LocalDate maxDate) {
        super(from, to, today);

        if (minDate != null && maxDate != null && minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("Min date should be before max date");
        }
        mMinDate = minDate;
        mMaxDate = maxDate;
    }

    public LocalDate getMinDate() {
        return mMinDate;
    }

    public LocalDate getMaxDate() {
        return mMaxDate;
    }

    @Override
    public boolean hasDate(LocalDate date) {
        boolean min = true;
        boolean max = true;

        LocalDate maxDate = getMaxDate();
        if (maxDate != null) {
            max = !date.isAfter(maxDate);
        }

        LocalDate minDate = getMinDate();
        if (minDate != null) {
            min = !date.isBefore(minDate);
        }

        return min && max;
    }

    /**
     * @param firstDayOfMonth First day of current month in range unit
     * @return Week of month of first enabled date, 0 if no dates are enabled.
     */
    public int getFirstWeek(LocalDate firstDayOfMonth) {
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

    public abstract LocalDate getFirstDateOfCurrentMonth(LocalDate currentMonth);

    public int getWeekInMonth(LocalDate date) {
        if (date != null) {
            LocalDate first = date.withDayOfMonth(1).withDayOfWeek(DateTimeConstants.MONDAY);
            Days days = Days.daysBetween(first, date);
            return days.dividedBy(DateTimeConstants.DAYS_PER_WEEK).getDays();
        } else {
            return 0;
        }
    }
}
