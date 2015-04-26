package com.wefika.calendar.manager;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultFormatterTest {

    DefaultFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new DefaultFormatter();
    }
    
    @Test
    public void testGetDayName() throws Exception {
        String dayName = formatter.getDayName(new LocalDate(2015, 4, 26));
        assertEquals("Sun", dayName);
    }

    @Test
    public void testGetHeaderTextWeek() throws Exception {
        String headerText = formatter.getHeaderText(CalendarUnit.TYPE_WEEK, new LocalDate(2015, 4, 20), new LocalDate(2015, 4, 26));
        assertEquals("week 17", headerText);
    }

    @Test
    public void testGetHeaderTextMonth() throws Exception {
        String headerText = formatter.getHeaderText(CalendarUnit.TYPE_MONTH, new LocalDate(2015, 4, 1), new LocalDate(2015, 4, 30));
        assertEquals("April 2015", headerText);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetHeaderTextInvalidType() throws Exception {
        formatter.getHeaderText(3, new LocalDate(2015, 4, 1), new LocalDate(2015, 4, 30));

    }

}
