package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaž Šolar on 24/02/14.
 */
public class Month extends CalendarUnit {

    @NotNull private final List<Week> mWeeks = new ArrayList<>();
    private int mSelectedIndex = -1;

    protected Month(@NotNull LocalDate date, @NotNull LocalDate today) {
        super(
                date.withDayOfMonth(1),
                date.withDayOfMonth(date.dayOfMonth().getMaximumValue()),
                "MMMM yyyy",
                today
        );
        build();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public boolean  hasPrev() {
        int year = getToday().getYear();
        int yearFrom = getFrom().getYear();

        int month = getToday().getMonthOfYear();
        int monthFrom = getFrom().getMonthOfYear();

        return year < yearFrom ||
                (year == yearFrom && month < monthFrom);
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
            mWeeks.add(new Week(date, getToday()));
            date = date.plusWeeks(1);
        }

    }
}
