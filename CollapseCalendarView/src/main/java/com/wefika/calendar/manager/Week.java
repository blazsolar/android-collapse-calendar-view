package com.wefika.calendar.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Week extends RangeUnit {

    @NonNull private final List<Day> mDays = new ArrayList<>(7);

    public Week(@NonNull LocalDate date, @NonNull LocalDate today, @Nullable LocalDate minDate,
                @Nullable LocalDate maxDate) {
        super(
                date.withDayOfWeek(1),
                date.withDayOfWeek(7),
                today,
                minDate,
                maxDate
        );

        build();
    }

    @Override
    public boolean hasNext() {
        LocalDate maxDate = getMaxDate();
        if (maxDate == null) {
            return true;
        } else {
            return maxDate.isAfter(mDays.get(6).getDate());
        }
    }

    @Override
    public boolean hasPrev() {
        LocalDate minDate = getMinDate();
        if (minDate == null) {
            return true;
        } else {
            return minDate.isBefore(mDays.get(0).getDate());
        }
    }

    @Override
    public boolean next() {
        if (hasNext()) {
            setFrom(getFrom().plusWeeks(1));
            setTo(getTo().plusWeeks(1));
            build();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean prev() {
        if (hasPrev()) {
            setFrom(getFrom().minusWeeks(1));
            setTo(getTo().minusWeeks(1));
            build();
            return true;
        } else {
            return false;
        }
    }

    @Override public int getType() {
        return TYPE_WEEK;
    }

    @Override
    public void deselect(@NonNull LocalDate date) {
        if (date != null && getFrom().compareTo(date) <= 0 &&
                getTo().compareTo(date) >= 0) {
            setSelected(false);

            for (Day day : mDays) {
                day.setSelected(false);
            }
        }
    }

    @Override
    public boolean select(@NonNull LocalDate date) {
        if (date != null && getFrom().compareTo(date) <= 0 &&
                getTo().compareTo(date) >= 0) {
            setSelected(true);

            for (Day day : mDays) {
                day.setSelected(day.getDate().isEqual(date));
            }
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    public List<Day> getDays() {
        return mDays;
    }

    @Override
    public void build() {

        mDays.clear();

        LocalDate date = getFrom();
        for(; date.compareTo(getTo()) <= 0; date = date.plusDays(1)) {
            Day day = new Day(date, date.equals(getToday()));
            day.setEnabled(isDayEnabled(date));
            mDays.add(day);
        }

    }

    private boolean isDayEnabled(@NonNull LocalDate date) {

        LocalDate minDate = getMinDate();
        if (minDate != null && date.isBefore(minDate)) {
            return false;
        }

        LocalDate maxDate = getMaxDate();
        if (maxDate != null && date.isAfter(maxDate)) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    LocalDate getFirstDateOfCurrentMonth(@NonNull LocalDate currentMonth) {

        if (currentMonth != null) {
            int year = currentMonth.getYear();
            int month = currentMonth.getMonthOfYear();

            LocalDate date = getFrom();
            for (; date.compareTo(getTo()) <= 0; date = date.plusDays(1)) {
                int fromYear = date.getYear();
                int fromMonth = date.getMonthOfYear();

                if (year == fromYear && month == fromMonth) {
                    return date;
                }
            }
        }

        return null;
    }
}
