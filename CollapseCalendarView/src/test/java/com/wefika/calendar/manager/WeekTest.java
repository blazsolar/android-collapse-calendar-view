package com.wefika.calendar.manager;

import android.test.AndroidTestCase;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.List;

public class WeekTest extends AndroidTestCase {

    Week mWeek;

    public void setUp() throws Exception {

        LocalDate today = LocalDate.parse("2014-08-1");

        mWeek = new Week(today, today, null, null);
    }


    public void testHasNextNull() throws Exception {
        assertTrue(mWeek.hasNext());
    }


    public void testHasNextTrue() throws Exception {

        LocalDate today = LocalDate.now();

        Week week = new Week(today, today, null, today.plusWeeks(1));
        assertTrue(week.hasNext());

    }


    public void testHasNextFalse() throws Exception {

        LocalDate today = LocalDate.now();

        Week week = new Week(today, today, null, today.withDayOfWeek(DateTimeConstants.SUNDAY));
        assertFalse(week.hasNext());

    }


    public void testHasPrevNull() throws Exception {
        assertTrue(mWeek.hasPrev());
    }


    public void testHasPrevTrue() throws Exception {

        LocalDate today = LocalDate.now();

        Week week = new Week(today, today, today.minusWeeks(1), null);
        assertTrue(week.hasPrev());

    }


    public void testHasPrevFalse() throws Exception {

        LocalDate today = LocalDate.now();

        Week week = new Week(today, today, today.withDayOfWeek(DateTimeConstants.MONDAY), null);
        assertFalse(week.hasPrev());

    }


    public void testNext() throws Exception {

        LocalDate nextWeek = LocalDate.parse("2014-08-04");
        LocalDate nextWeekEnd = LocalDate.parse("2014-08-10");

        assertTrue(mWeek.next());

        assertEquals(nextWeek, mWeek.getFrom());
        assertEquals(nextWeekEnd, mWeek.getTo());

        assertEquals(nextWeek, mWeek.getDays().get(0).getDate());
        assertEquals(nextWeekEnd, mWeek.getDays().get(6).getDate());

    }


    public void testNextFalse() throws Exception {

        LocalDate today = LocalDate.parse("2014-08-01");
        LocalDate start = LocalDate.parse("2014-07-28");
        LocalDate end = LocalDate.parse("2014-08-3");

        Week week = new Week(today, today, today, today);

        assertFalse(week.next());

        assertEquals(start, week.getFrom());
        assertEquals(end, week.getTo());

        assertEquals(start, week.getDays().get(0).getDate());
        assertEquals(end, week.getDays().get(6).getDate());

    }


    public void testPrev() throws Exception {

        LocalDate start = LocalDate.parse("2014-07-21");
        LocalDate end = LocalDate.parse("2014-07-27");

        assertTrue(mWeek.prev());

        assertEquals(start, mWeek.getFrom());
        assertEquals(end, mWeek.getTo());

        assertEquals(start, mWeek.getDays().get(0).getDate());
        assertEquals(end, mWeek.getDays().get(6).getDate());

    }


    public void testPrevFalse() throws Exception {

        LocalDate today = LocalDate.parse("2014-08-01");
        LocalDate start = LocalDate.parse("2014-07-28");
        LocalDate end = LocalDate.parse("2014-08-3");

        Week week = new Week(today, today, today, today);

        assertFalse(week.prev());

        assertEquals(start, week.getFrom());
        assertEquals(end, week.getTo());

        assertEquals(start, week.getDays().get(0).getDate());
        assertEquals(end, week.getDays().get(6).getDate());

    }


    public void testDeselectNull() throws Exception {

        mWeek.deselect(null);
        assertFalse(mWeek.isSelected());

    }


    public void testDeselectNotIn() throws Exception {

        mWeek.select(LocalDate.parse("2014-08-01"));

        mWeek.deselect(LocalDate.parse("2014-08-14"));
        assertTrue(mWeek.isSelected());

    }


    public void testDeselect() throws Exception {

        mWeek.select(LocalDate.parse("2014-08-01"));

        mWeek.deselect(LocalDate.parse("2014-07-29"));
        assertFalse(mWeek.isSelected());

        for (Day day : mWeek.getDays()) {
            assertFalse(day.isSelected());
        }

    }


    public void testSelectNull() throws Exception {

        assertFalse(mWeek.select(null));

    }


    public void testSelectNotIn() throws Exception {

        assertFalse(mWeek.select(LocalDate.parse("2014-08-14")));
        assertFalse(mWeek.isSelected());

        for (Day day : mWeek.getDays()) {
            assertFalse(day.isSelected());
        }

    }


    public void testSelect() throws Exception {

        assertTrue(mWeek.select(LocalDate.parse("2014-07-31")));
        assertTrue(mWeek.isSelected());

        List<Day> days = mWeek.getDays();
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            if (i == 3) {
                assertTrue(day.isSelected());
            } else {
                assertFalse(day.isSelected());
            }
        }

    }


    public void testGetDays() throws Exception {

        List<Day> days = mWeek.getDays();

        assertEquals(7, days.size());

        LocalDate first = LocalDate.parse("2014-07-28");
        for (int i = 0; i < days.size(); i++) {

            Day day = days.get(i);
            assertEquals(first.plusDays(i), day.getDate());

            if (i == 4) {
                assertTrue(day.isToday());
            } else {
                assertFalse(day.isToday());
            }

            assertTrue(day.isEnabled());
        }

    }


    public void testBuild() throws Exception {

        LocalDate today = LocalDate.parse("2014-08-01");
        LocalDate min = LocalDate.parse("2014-07-29");
        LocalDate max = LocalDate.parse("2014-08-02");

        Week week = new Week(today, today, min, max);
        week.build();

        List<Day> days = week.getDays();
        assertEquals(7, days.size());

        LocalDate first = LocalDate.parse("2014-07-28");
        for (int i = 0; i < days.size(); i++) {

            Day day = days.get(i);
            assertEquals(first.plusDays(i), day.getDate());

            if (i == 4) {
                assertTrue(day.isToday());
            } else {
                assertFalse(day.isToday());
            }

            if (i > 0 && i < 6) {
                assertTrue(day.isEnabled());
            } else {
                assertFalse(day.isEnabled());
            }
        }

    }


    public void testGetFirstDateOfCurrentMonthNull() throws Exception {
        assertNull(mWeek.getFirstDateOfCurrentMonth(null));
    }


    public void testGetFirstDateOfCurrentMonthNotIn() throws Exception {
        assertNull(mWeek.getFirstDateOfCurrentMonth(LocalDate.parse("2014-09-01")));
    }


    public void testGetFirstDateOfCurrentMonthYear() throws Exception {
        assertNull(mWeek.getFirstDateOfCurrentMonth(LocalDate.parse("2015-08-01")));
    }


    public void testGetFirstDateOfCurrentMonth() throws Exception {
        assertEquals(LocalDate.parse("2014-08-01"), mWeek.getFirstDateOfCurrentMonth(LocalDate.parse("2014-08-23")));
    }


    public void testHeaderText() throws Exception {
        assertEquals("week 31", mWeek.getHeaderText());
    }
}