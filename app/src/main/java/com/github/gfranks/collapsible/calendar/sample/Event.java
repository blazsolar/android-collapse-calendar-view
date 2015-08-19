package com.github.gfranks.collapsible.calendar.sample;

import com.github.gfranks.collapsible.calendar.model.CollapsibleCalendarEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Event extends CollapsibleCalendarEvent {

    private String mTitle;
    private long mDate;

    public Event(String title, long date) {
        mTitle = title;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public DateTime getListCellTime() {
        return new DateTime(mDate);
    }

    @Override
    public LocalDate getCollapsibleEventLocalDate() {
        return new LocalDate(mDate);
    }
}