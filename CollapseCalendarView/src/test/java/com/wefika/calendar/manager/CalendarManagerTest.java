package com.wefika.calendar.manager;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CalendarManagerTest {

    CalendarManager mCalendarManager;

    @Before
    public void setUp() throws Exception {

        mCalendarManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(3));

    }

    @Test
    public void testSelectDay() throws Exception {

        LocalDate select = LocalDate.now().plusDays(1);

        boolean result = mCalendarManager.selectDay(select);

        assertTrue(result);
        assertTrue(mCalendarManager.getUnits().isSelected());

    }

    @Test
    public void testSelectDayOutOfRange() throws Exception {


        LocalDate select = LocalDate.now().plusMonths(2);

        boolean result = mCalendarManager.selectDay(select);

        assertTrue(result);
        assertFalse(mCalendarManager.getUnits().isSelected());

    }

    @Test
    public void testSelectDayWeek() throws Exception {

        mCalendarManager.toggleView();

        LocalDate select = LocalDate.now().plusMonths(2);

        boolean result = mCalendarManager.selectDay(select);
        assertTrue(result);
        assertFalse(mCalendarManager.getUnits().isSelected());
        assertEquals(select.withDayOfMonth(1), mCalendarManager.getActiveMonth());


    }

    @Test
    public void testSelectDaySame() throws Exception {

        LocalDate selected = mCalendarManager.getSelectedDay();

        boolean result = mCalendarManager.selectDay(selected);

        assertFalse(result);
        assertEquals(selected, mCalendarManager.getSelectedDay());
        assertTrue(mCalendarManager.getUnits().isSelected());

    }

    @Test
    public void testGetSelectedDay() throws Exception {

    }

    @Test
    public void testGetHeaderText() throws Exception {

    }

    @Test
    public void testHasNext() throws Exception {

    }

    @Test
    public void testHasPrev() throws Exception {

    }

    @Test
    public void testNext() throws Exception {

    }

    @Test
    public void testPrev() throws Exception {

    }

    @Test
    public void testToggleView() throws Exception {

    }

    @Test
    public void testGetState() throws Exception {

    }

    @Test
    public void testGetUnits() throws Exception {

    }

    @Test
    public void testGetActiveMonth() throws Exception {

    }

    @Test
    public void testToggleToWeek() throws Exception {

    }

    @Test
    public void testGetWeekOfMonth() throws Exception {

    }

    @Test
    public void testInit() throws Exception {

        LocalDate now = LocalDate.now();
        LocalDate min = now.minusDays(3);
        LocalDate max = min.plusMonths(3);

        mCalendarManager.init(now, min, max);

        assertEquals(now, mCalendarManager.getSelectedDay());
        assertEquals(min, mCalendarManager.getMinDate());
        assertEquals(max, mCalendarManager.getMaxDate());
        assertEquals(now.withDayOfMonth(1), mCalendarManager.getActiveMonth());

        CalendarUnit unit = new Month(now, now, min, max);
        unit.setSelected(true);

        assertEquals(unit, mCalendarManager.getUnits());

    }

    @Test
    public void testInitWeek() throws Exception {

        LocalDate now = LocalDate.now();
        LocalDate min = now.minusDays(3);
        LocalDate max = min.plusMonths(3);

        CalendarManager manager = new CalendarManager(now,
                CalendarManager.State.WEEK, min, max);

        CalendarUnit unit = new Week(now, now, min, max);
        unit.setSelected(true);

        assertEquals(unit, manager.getUnits());

    }
}