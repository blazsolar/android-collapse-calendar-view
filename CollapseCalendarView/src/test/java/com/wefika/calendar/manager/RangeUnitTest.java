package com.wefika.calendar.manager;

import android.test.AndroidTestCase;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class RangeUnitTest extends AndroidTestCase {

    RangeUnit mRangeUnit;

    public void setUp() throws Exception {
        LocalDate today = LocalDate.now();
        mRangeUnit = new Month(today, today, today.minusMonths(2), today.plusMonths(2));
    }


    public void testInit() throws Exception {

        LocalDate today = LocalDate.now();

        assertEquals(today.minusMonths(2), mRangeUnit.getMinDate());
        assertEquals(today.plusMonths(2), mRangeUnit.getMaxDate());

    }


    public void testIllegalRange() throws Exception {

        LocalDate today = LocalDate.now();

        try {
            new Month(today, today, today.plusDays(1), today);
            fail();
        } catch (IllegalArgumentException e) {
            // should happen
        }

    }


    public void testMinMaxNull() throws Exception {

        LocalDate today = LocalDate.now();
        RangeUnit unit = new Month(today, today, null, null);

        assertNull(unit.getMinDate());
        assertNull(unit.getMaxDate());

    }


    public void testGetFirstWeekNull() throws Exception {

        LocalDate today = LocalDate.now();
        RangeUnit unit = new Month(today, today, null, null);

        assertEquals(0, unit.getFirstWeek(null));

    }


    public void testGetFirstWeekCurrentMonth() throws Exception {

        LocalDate today = LocalDate.now();

        LocalDate firstMonday = today.withDayOfMonth(1).withDayOfWeek(DateTimeConstants.MONDAY);
        int week = Days.daysBetween(firstMonday, today).dividedBy(DateTimeConstants.DAYS_PER_WEEK).getDays();

        assertEquals(week, mRangeUnit.getFirstWeek(today));

    }


    public void testGetFirstWeekMinDate() throws Exception {

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusMonths(2);
        LocalDate date = minDate.minusWeeks(1);
        RangeUnit unit = new Week(date, today, minDate, today);

        LocalDate firstMonday = minDate.withDayOfMonth(1).withDayOfWeek(DateTimeConstants.MONDAY);
        int week = Days.daysBetween(firstMonday, minDate).dividedBy(DateTimeConstants.DAYS_PER_WEEK).getDays();

        assertEquals(week, unit.getFirstWeek(date));

    }


    public void testGetFirstWeekBetween() throws Exception {

        LocalDate date = LocalDate.parse("2014-08-01");

        RangeUnit unit = new Month(date, date, date.plusDays(2), date.plusWeeks(2));

        assertEquals(0, unit.getFirstWeek(date.plusDays(12)));

    }


    public void testGetFirstWeekOutOfView() throws Exception {

        LocalDate date = LocalDate.parse("2014-08-01");

        RangeUnit unit = new Week(date, date, date, date.plusWeeks(2));

        assertEquals(0, unit.getFirstWeek(date.plusDays(12)));

    }


    public void testGetFirstWeekDifferentMonth() throws Exception {

        LocalDate date = LocalDate.parse("2014-08-01");

        RangeUnit unit = new Month(date, date, date.plusDays(2), date.plusMonths(3));

        assertEquals(0, unit.getFirstWeek(date.plusMonths(1).plusWeeks(1)));

    }


    public void testGetFirstEnabledNullMin() throws Exception {

        LocalDate today = LocalDate.now();
        RangeUnit unit = new Week(today, today, null, null);

        assertEquals(today.withDayOfWeek(DateTimeConstants.MONDAY), unit.getFirstEnabled());

    }


    public void testGetFirstEnabledBeforeMin() throws Exception {

        LocalDate today = LocalDate.now();
        RangeUnit unit = new Week(today, today, today.plusDays(1), null);

        assertEquals(today.plusDays(1), unit.getFirstEnabled());

    }


    public void testGetFirstEnabled() throws Exception {
        assertEquals(mRangeUnit.getFrom(), mRangeUnit.getFirstEnabled());
    }
}