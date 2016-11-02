package com.github.gfranks.collapsible.calendar.model;

import org.joda.time.LocalDate;

public interface Formatter {

    String getDayName(LocalDate date);

    String getHeaderText(int type, LocalDate from, LocalDate to);

}
