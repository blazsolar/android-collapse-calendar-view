package com.wefika.calendar.manager;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalendarManagerTest {

    Formatter formatter;
    CalendarManager mCalendarManager;

    @Before
    public void setUp() throws Exception {

        formatter = mock(Formatter.class);

        mCalendarManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(3), formatter);

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
    public void testGetHeaderText() throws Exception {

        LocalDate date = LocalDate.now();
        RangeUnit unit = mock(RangeUnit.class);
        when(unit.getType()).thenReturn(CalendarUnit.TYPE_WEEK);
        when(unit.getFrom()).thenReturn(date);
        when(unit.getTo()).thenReturn(date);

        when(formatter.getHeaderText(eq(CalendarUnit.TYPE_WEEK), eq(date), eq(date))).thenReturn("header");
        mCalendarManager.setUnit(unit);

        String header = mCalendarManager.getHeaderText();
        assertEquals("header", header);

    }

    @Test
    public void testSetUnit() throws Exception {

        RangeUnit unit = mock(RangeUnit.class);
        mCalendarManager.setUnit(unit);

        assertTrue(unit == mCalendarManager.getUnits());

    }

    @Test
    public void testSetUnitNull() throws Exception {

        CalendarUnit unit = mCalendarManager.getUnits();
        mCalendarManager.setUnit(null);

        assertTrue(unit == mCalendarManager.getUnits());

    }

    @Test
    public void testHasNext() throws Exception {

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.hasNext()).thenReturn(true);

        mCalendarManager.setUnit(unit);

        boolean result = mCalendarManager.hasNext();
        assertTrue(result);

        verify(unit).hasNext();

    }

    @Test
    public void testHasPrev() throws Exception {

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.hasPrev()).thenReturn(true);

        mCalendarManager.setUnit(unit);

        boolean result = mCalendarManager.hasPrev();
        assertTrue(result);

        verify(unit).hasPrev();

    }

    @Test
    public void testNext() throws Exception {

        LocalDate from = LocalDate.now().plusWeeks(1);

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.next()).thenReturn(true);
        when(unit.getFrom()).thenReturn(from);

        mCalendarManager.setUnit(unit);

        boolean result = mCalendarManager.next();

        assertTrue(result);

        InOrder order = inOrder(unit);
        order.verify(unit).next();
        order.verify(unit).select(mCalendarManager.getSelectedDay());

        assertEquals(from.withDayOfMonth(1), mCalendarManager.getActiveMonth());

    }

    @Test
    public void testPrev() throws Exception {

        LocalDate to = LocalDate.now().plusWeeks(1);

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.prev()).thenReturn(true);
        when(unit.getTo()).thenReturn(to);

        mCalendarManager.setUnit(unit);

        boolean result = mCalendarManager.prev();

        assertTrue(result);

        InOrder order = inOrder(unit);
        order.verify(unit).prev();
        order.verify(unit).select(mCalendarManager.getSelectedDay());

        assertEquals(to.withDayOfMonth(1), mCalendarManager.getActiveMonth());

    }

    @Test
    public void testToggleViewMonthInView() throws Exception {

        LocalDate selected = mCalendarManager.getSelectedDay();

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(selected)).thenReturn(true);
        mCalendarManager.setUnit(unit);

        mCalendarManager.toggleView();

        Week week = new Week(selected, LocalDate.now(), mCalendarManager.getMinDate(), mCalendarManager.getMaxDate());
        week.select(selected);

        assertEquals(week, mCalendarManager.getUnits());
        assertEquals(CalendarManager.State.WEEK, mCalendarManager.getState());
        assertEquals(selected.withDayOfMonth(1), mCalendarManager.getActiveMonth());

    }

    @Test
    public void testToggleViewMonthNotInView() throws Exception {

        LocalDate selected = mCalendarManager.getSelectedDay();
        LocalDate from = selected.plusMonths(1);

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(selected)).thenReturn(false);
        when(unit.getFrom()).thenReturn(from);
        when(unit.getFirstDateOfCurrentMonth(any(LocalDate.class))).thenReturn(from);
        mCalendarManager.setUnit(unit);

        mCalendarManager.toggleView();

        Week week = new Week(from, LocalDate.now(), mCalendarManager.getMinDate(), mCalendarManager.getMaxDate());
        week.select(selected);

        assertEquals(week, mCalendarManager.getUnits());
        assertEquals(from.withDayOfMonth(1), mCalendarManager.getActiveMonth());

    }

    @Test
    public void testToggleViewWeekInView() throws Exception {

        mCalendarManager.toggleView(); // initial state week

        LocalDate currentMonth = mCalendarManager.getActiveMonth();
        LocalDate selected = mCalendarManager.getSelectedDay();

        mCalendarManager.toggleView();

        Month month = new Month(currentMonth, LocalDate.now(), mCalendarManager.getMinDate(), mCalendarManager.getMaxDate());
        month.select(selected);

        assertEquals(month, mCalendarManager.getUnits());
        assertEquals(CalendarManager.State.MONTH, mCalendarManager.getState());

    }

    @Test
    public void testToggleToWeek() throws Exception {

        LocalDate from = mCalendarManager.getUnits().getFrom();

        mCalendarManager.toggleToWeek(2);

        Week week = new Week(from.plusDays(2 * 7), LocalDate.now(), mCalendarManager.getMinDate(), mCalendarManager.getMaxDate());
        week.select(mCalendarManager.getSelectedDay());

        assertEquals(week, mCalendarManager.getUnits());

    }

    @Test
    public void testGetWeekInViewInRange() throws Exception {

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(any(LocalDate.class))).thenReturn(true);
        when(unit.isIn(any(LocalDate.class))).thenReturn(true);
        when(unit.getWeekInMonth(mCalendarManager.getSelectedDay())).thenReturn(12);

        mCalendarManager.setUnit(unit);

        assertEquals(12, mCalendarManager.getWeekOfMonth());

    }

    @Test
    public void testGetWeekInViewAfter() throws Exception {

        LocalDate from = mCalendarManager.getSelectedDay().plusDays(1);

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(any(LocalDate.class))).thenReturn(true);
        when(unit.isIn(any(LocalDate.class))).thenReturn(false);
        when(unit.getFrom()).thenReturn(from);
        when(unit.getWeekInMonth(from)).thenReturn(12);

        mCalendarManager.setUnit(unit);

        assertEquals(12, mCalendarManager.getWeekOfMonth());

    }

    @Test
    public void testGetWeekInViewBefore() throws Exception {

        LocalDate from = mCalendarManager.getSelectedDay().minusDays(1);
        LocalDate to = mCalendarManager.getSelectedDay().plusDays(1);

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(any(LocalDate.class))).thenReturn(true);
        when(unit.isIn(any(LocalDate.class))).thenReturn(false);
        when(unit.getFrom()).thenReturn(from);
        when(unit.getTo()).thenReturn(to);
        when(unit.getWeekInMonth(to)).thenReturn(12);

        mCalendarManager.setUnit(unit);

        assertEquals(12, mCalendarManager.getWeekOfMonth());

    }

    @Test
    public void testGetWeekOfMonthNotInView() throws Exception {

        LocalDate now = LocalDate.now();

        RangeUnit unit = mock(RangeUnit.class);
        when(unit.isInView(any(LocalDate.class))).thenReturn(false);
        when(unit.getFirstDateOfCurrentMonth(mCalendarManager.getActiveMonth())).thenReturn(now);
        when(unit.getFirstWeek(now)).thenReturn(43);

        mCalendarManager.setUnit(unit);

        assertEquals(43, mCalendarManager.getWeekOfMonth());

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


    @Test
    public void testSetMinDate() throws Exception {

        LocalDate min = LocalDate.now().minusMonths(1);
        mCalendarManager.setMinDate(min);

        assertEquals(min, mCalendarManager.getMinDate());

    }

    @Test
    public void testSetMaxDate() throws Exception {

        LocalDate max = LocalDate.now().plusMonths(1);
        mCalendarManager.setMaxDate(max);

        assertEquals(max, mCalendarManager.getMaxDate());

    }
}
