package com.wefika.calendar.manager;

import android.test.AndroidTestCase;

import org.joda.time.LocalDate;

public class DayTest extends AndroidTestCase {

    LocalDate mToday;
    Day mDay;

    public void setUp() throws Exception {

        mToday = LocalDate.parse("2014-07-05");
        mDay = new Day(mToday, true);

    }


    public void testInit() throws Exception {

        assertEquals(mToday, mDay.getDate());
        assertTrue(mDay.isToday());
        assertTrue(mDay.isEnabled());
        assertTrue(mDay.isCurrent());
        assertFalse(mDay.isSelected());
    }


    public void testSetEnabled() throws Exception {

        mDay.setEnabled(false);
        assertFalse(mDay.isEnabled());

    }


    public void testSetSelected() throws Exception {

        mDay.setSelected(true);
        assertTrue(mDay.isSelected());

    }


    public void testSetCurrent() throws Exception {

        mDay.setCurrent(false);
        assertFalse(mDay.isCurrent());

    }


    public void testEqualsNull() throws Exception {
        assertFalse(mDay.equals(null));
    }



    public void testEqualsSame() throws Exception {
        assertTrue(mDay.equals(mDay));
    }


    public void testEquals() throws Exception {
        Day day = new Day(mToday, true);
        assertTrue(mDay.equals(day));
        assertTrue(day.equals(mDay));

        assertEquals(day.hashCode(), mDay.hashCode());
    }


    public void testGetText() throws Exception {
        assertEquals("5", mDay.getText());
    }
}