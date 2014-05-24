package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaž Šolar on 24/02/14.
 */
public class Month extends CalendarUnit {

    @NotNull private final List<Week> mWeeks = new ArrayList<>();
    private int mSelectedIndex = -1;

    @Nullable private LocalDate mMinDate;
    @Nullable private LocalDate mMaxDate;

    protected Month(@NotNull LocalDate date, @NotNull LocalDate today, @Nullable LocalDate minDate,
                    @Nullable LocalDate maxDate) {
        super(
                date.withDayOfMonth(1),
                date.withDayOfMonth(date.dayOfMonth().getMaximumValue()),
                "MMMM yyyy",
                today
        );

        mMinDate = minDate;
        mMaxDate = maxDate;

        build();
    }

    @Override
    public boolean hasNext() {

        if (mMaxDate == null) {
            return true;
        } else {

            LocalDate to = getTo();
            int year = mMaxDate.getYear();
            int yearTo = to.getYear();

            int month = mMaxDate.getMonthOfYear();
            int monthTo = to.getMonthOfYear();

            return year > yearTo ||
                    (year == yearTo && month > monthTo);

        }
    }

    @Override
    public boolean  hasPrev() {

        if (mMinDate == null) {
            return true;
        } else {

            LocalDate from = getFrom();
            int year = mMinDate.getYear();
            int yearFrom = from.getYear();

            int month = mMinDate.getMonthOfYear();
            int monthFrom = from.getMonthOfYear();

            return year < yearFrom ||
                    (year == yearFrom && month < monthFrom);

        }
    }

    @Override
    public boolean next() {

        if (hasNext()) {
            setFrom(getTo().plusDays(1));
            setTo(getFrom().withDayOfMonth(getFrom().dayOfMonth().getMaximumValue()));

            build();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean prev() {

        if (hasPrev()) {
            setFrom(getFrom().minusDays(1).withDayOfMonth(1));
            setTo(getFrom().withDayOfMonth(getFrom().dayOfMonth().getMaximumValue()));

            build();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void deselect(@NotNull LocalDate date) {
        if (isSelected()) {
            mSelectedIndex = -1;
            for (Week week : mWeeks) {
                setSelected(false);
                week.deselect(date);
            }
        }
    }

    @Override
    public boolean select(@NotNull LocalDate date) {
        int cnt = mWeeks.size();
        for (int i = 0; i < cnt; i++) {
            Week week = mWeeks.get(i);
            if (week.select(date)) {
                mSelectedIndex = i;
                setSelected(true);
                return true;
            }
        }
        return false;
    }

    @NotNull
    public List<Week> getWeeks() {
        return mWeeks;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public void build() {

        setSelected(false);
        mWeeks.clear();

        LocalDate date = getFrom().withDayOfWeek(1);
        for (int i = 0; i == 0 || getTo().compareTo(date) >= 0; i++) {
            mWeeks.add(new Week(date, getToday(), mMinDate, mMaxDate));
            date = date.plusWeeks(1);
        }

    }
}
