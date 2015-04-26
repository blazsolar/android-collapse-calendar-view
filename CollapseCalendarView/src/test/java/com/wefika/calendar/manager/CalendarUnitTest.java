package com.wefika.calendar.manager;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalendarUnitTest {

    CalendarUnit mUnit;

    @Before
    public void setUp() throws Exception {

        LocalDate today = LocalDate.now();
        mUnit = new Week(today, today, today.minusWeeks(1), today.plusWeeks(2));

    }

    @Test
    public void testInit() throws Exception {

        LocalDate today = LocalDate.now();

        assertEquals(today, mUnit.getToday());
        assertEquals(today.withDayOfWeek(DateTimeConstants.MONDAY), mUnit.getFrom());
        assertEquals(today.withDayOfWeek(DateTimeConstants.SUNDAY), mUnit.getTo());

    }

    @Test
    public void testSetDate() throws Exception {

        LocalDate nextWeek = LocalDate.now().plusWeeks(1);

        mUnit.setFrom(nextWeek.withDayOfWeek(DateTimeConstants.MONDAY));
        mUnit.setTo(nextWeek.withDayOfWeek(DateTimeConstants.SUNDAY));

        assertEquals(nextWeek.withDayOfWeek(DateTimeConstants.MONDAY), mUnit.getFrom());
        assertEquals(nextWeek.withDayOfWeek(DateTimeConstants.SUNDAY), mUnit.getTo());

    }

    @Test
    public void testSetSelected() throws Exception {

        assertFalse(mUnit.isSelected());

        mUnit.setSelected(true);

        assertTrue(mUnit.isSelected());

    }

    @Test
    public void testIsIn() throws Exception {

        LocalDate today = LocalDate.now();

        assertTrue(mUnit.isIn(today.withDayOfWeek(DateTimeConstants.MONDAY)));
        assertTrue(mUnit.isIn(today.withDayOfWeek(DateTimeConstants.WEDNESDAY)));
        assertTrue(mUnit.isIn(today.withDayOfWeek(DateTimeConstants.SUNDAY)));

    }

    @Test
    public void testIsInBefore() throws Exception {

        LocalDate prevWeek = LocalDate.now().minusWeeks(1);
        assertFalse(mUnit.isIn(prevWeek.withDayOfWeek(DateTimeConstants.SUNDAY)));

    }

    @Test
    public void testIsInAfter() throws Exception {

        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        assertFalse(mUnit.isIn(nextWeek.withDayOfWeek(DateTimeConstants.MONDAY)));

    }

    @Test
    public void testIsInView() throws Exception {

        LocalDate today = LocalDate.now();

        assertTrue(mUnit.isInView(today.withDayOfWeek(DateTimeConstants.MONDAY)));
        assertTrue(mUnit.isInView(today.withDayOfWeek(DateTimeConstants.WEDNESDAY)));
        assertTrue(mUnit.isInView(today.withDayOfWeek(DateTimeConstants.SUNDAY)));

    }

    @Test
    public void testIsInViewBefore() throws Exception {

        LocalDate prevWeek = LocalDate.now().minusWeeks(1);
        assertFalse(mUnit.isInView(prevWeek.withDayOfWeek(DateTimeConstants.SUNDAY)));

    }

    @Test
    public void testIsInViewAfter() throws Exception {

        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        assertFalse(mUnit.isInView(nextWeek.withDayOfWeek(DateTimeConstants.MONDAY)));

    }

    @Test
    public void testEqualsNull() throws Exception {
        assertFalse(mUnit.equals(null));
    }

    @Test
    public void testEqualsSelf() throws Exception {
        assertTrue(mUnit.equals(mUnit));
    }
}
