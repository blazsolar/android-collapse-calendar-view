package com.wefika.calendar.manager;

import android.test.suitebuilder.annotation.SmallTest;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SmallTest
public class DayTest {

    LocalDate mToday;
    Day mDay;

    @Before
    public void setUp() throws Exception {

        mToday = LocalDate.parse("2014-07-05");
        mDay = new Day(mToday, true);

    }

    @Test
    public void testInit() throws Exception {

        assertEquals(mToday, mDay.getDate());
        assertTrue(mDay.isToday());
        assertTrue(mDay.isEnabled());
        assertTrue(mDay.isCurrent());
        assertFalse(mDay.isSelected());
    }

    @Test
    public void testSetEnabled() throws Exception {

        mDay.setEnabled(false);
        assertFalse(mDay.isEnabled());

    }

    @Test
    public void testSetSelected() throws Exception {

        mDay.setSelected(true);
        assertTrue(mDay.isSelected());

    }

    @Test
    public void testSetCurrent() throws Exception {

        mDay.setCurrent(false);
        assertFalse(mDay.isCurrent());

    }

    @Test
    public void testEqualsNull() throws Exception {
        assertFalse(mDay.equals(null));
    }


    @Test
    public void testEqualsSame() throws Exception {
        assertTrue(mDay.equals(mDay));
    }

    @Test
    public void testEquals() throws Exception {
        Day day = new Day(mToday, true);
        assertTrue(mDay.equals(day));
        assertTrue(day.equals(mDay));

        assertEquals(day.hashCode(), mDay.hashCode());
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals("5", mDay.getText());
    }

}
