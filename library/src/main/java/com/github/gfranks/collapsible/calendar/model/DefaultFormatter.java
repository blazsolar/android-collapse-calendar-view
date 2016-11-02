package com.github.gfranks.collapsible.calendar.model;

import com.github.gfranks.collapsible.calendar.model.CalendarUnit.CalendarType;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DefaultFormatter implements Formatter {

    private DateTimeFormatter dayFormatter;
    private DateTimeFormatter weekHeaderFormatter;
    private DateTimeFormatter monthHeaderFormatter;

    public DefaultFormatter() {
        this("E", "MMMM yyyy", "MMMM yyyy");
    }

    public DefaultFormatter(String dayPattern, String weekPattern, String monthPattern) {
        dayFormatter = DateTimeFormat.forPattern(dayPattern);
        weekHeaderFormatter = DateTimeFormat.forPattern(weekPattern);
        monthHeaderFormatter = DateTimeFormat.forPattern(monthPattern);
    }

    @Override
    public String getDayName(LocalDate date) {
        return date.toString(dayFormatter);
    }

    @Override
    public String getHeaderText(@CalendarType int type, LocalDate from, LocalDate to) {
        switch (type) {
            case CalendarUnit.TYPE_WEEK:
                return from.toString(weekHeaderFormatter);
            case CalendarUnit.TYPE_MONTH:
                return from.toString(monthHeaderFormatter);
            default:
                throw new IllegalStateException("Unknown calendar type");
        }
    }
}
