package com.wefika.calendar.manager;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaž Šolar on 24/02/14.
 */
public class Week extends CalendarUnit {

    @NotNull
    private final List<Day> mDays = new ArrayList<>(7);

    public Week(@NotNull LocalDate date, @NotNull LocalDate today) {
        super(
                date.withDayOfWeek(1),
                date.withDayOfWeek(7),
                "'week' w",
                today
        );
        build();
    }

    @Override
    public boolean hasNext() {
        // currently no limit to for next week
        return true;
    }

    @Override
    public boolean hasPrev() {
        return getToday().isBefore(mDays.get(0).getDate());
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

    @Override
    public void deselect(@NotNull LocalDate date) {
        if (getFrom().compareTo(date) <= 0 &&
                getTo().compareTo(date) >= 0) {
            setSelected(false);

            for (Day day : mDays) {
                day.setSelected(false);
            }
        }
    }

    @Override
    public boolean select(@NotNull LocalDate date) {
        if (getFrom().compareTo(date) <= 0 &&
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

    @NotNull
    public List<Day> getDays() {
        return mDays;
    }

    @Override
    public void build() {

        mDays.clear();

        LocalDate date = getFrom();
        for(; date.compareTo(getTo()) <= 0; date = date.plusDays(1)) {
            mDays.add(new Day(date, date.equals(getToday())));
        }

    }

    void build(@NotNull LocalDate from, @NotNull LocalDate to) {

        build();

        for(Day day : mDays) {
            LocalDate date = day.getDate();
            day.setEnabled(!date.isBefore(from) && !to.isBefore(date));
        }

    }
}
