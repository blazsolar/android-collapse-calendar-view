package com.wefika.calendar.manager;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MonthTest {

    LocalDate mToday;
    Month mMonth;

    @Before
    public void setUp() throws Exception {

        mToday = LocalDate.parse("2014-10-08");
        mMonth = new Month(mToday, mToday, null, null);

    }

    @Test
    public void testHasNextNull() throws Exception {
        assertTrue(mMonth.hasNext());
    }

    @Test
    public void testHasNextFalse() throws Exception {

        LocalDate max = LocalDate.parse("2014-10-31");

        Month week = new Month(mToday, mToday, null, max);
        assertFalse(week.hasNext());

    }

    @Test
    public void testHasNextTrue() throws Exception {

        LocalDate max = LocalDate.parse("2014-11-01");

        Month week = new Month(mToday, mToday, null, max);
        assertTrue(week.hasNext());

    }

    @Test
    public void testHasNextYear() throws Exception {

        LocalDate max = LocalDate.parse("2015-10-08");

        Month week = new Month(mToday, mToday, null, max);
        assertTrue(week.hasNext());

    }

    @Test
    public void testHasPrevNull() throws Exception {
        assertTrue(mMonth.hasPrev());
    }

    @Test
    public void testHasPreFalse() throws Exception {

        LocalDate min = LocalDate.parse("2014-10-01");

        Month week = new Month(mToday, mToday, min, null);

        assertFalse(week.hasPrev());

    }

    @Test
    public void testHasPrevTrue() throws Exception {

        LocalDate min = LocalDate.parse("2014-09-30");

        Month week = new Month(mToday, mToday, min, null);

        assertTrue(week.hasPrev());

    }

    @Test
    public void testHasPrevYear() throws Exception {

        LocalDate min = LocalDate.parse("2013-10-08");

        Month week = new Month(mToday, mToday, min, null);

        assertTrue(week.hasPrev());

    }

    @Test
    public void testNextFalse() throws Exception {

        Month month = new Month(mToday, mToday, null, mToday);
        assertFalse(month.next());

        assertEquals(LocalDate.parse("2014-10-01"), month.getFrom());
        assertEquals(LocalDate.parse("2014-10-31"), month.getTo());

    }

    @Test
    public void testNext() throws Exception {

        assertTrue(mMonth.next());

        assertEquals(LocalDate.parse("2014-11-01"), mMonth.getFrom());
        assertEquals(LocalDate.parse("2014-11-30"), mMonth.getTo());

        assertEquals(LocalDate.parse("2014-10-27"), mMonth.getWeeks().get(0).getFrom());

    }

    @Test
    public void testPrevFalse() throws Exception {

        Month month = new Month(mToday, mToday, mToday, null);
        assertFalse(month.prev());

        assertEquals(LocalDate.parse("2014-10-01"), month.getFrom());
        assertEquals(LocalDate.parse("2014-10-31"), month.getTo());

        assertEquals(LocalDate.parse("2014-09-29"), month.getWeeks().get(0).getFrom());

    }

    @Test
    public void testPrev() throws Exception {

        assertTrue(mMonth.prev());

        assertEquals(LocalDate.parse("2014-09-01"), mMonth.getFrom());
        assertEquals(LocalDate.parse("2014-09-30"), mMonth.getTo());

        assertEquals(LocalDate.parse("2014-09-01"), mMonth.getWeeks().get(0).getFrom());
    }

    @Test
    public void testDeselectNull() throws Exception {
        mMonth.deselect(null);
        assertFalse(mMonth.isSelected());
    }

    @Test
    public void testDeselectNotSelected() throws Exception {
        mMonth.deselect(mToday);
        assertFalse(mMonth.isSelected());
    }

    @Test
    public void testDeselectNotIn() throws Exception {

        mMonth.select(mToday);
        mMonth.deselect(LocalDate.parse("2014-09-13"));

        assertTrue(mMonth.isSelected());

        List<Week> weeks = mMonth.getWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            Week week = weeks.get(i);
            if (i == 1) {
                assertTrue(week.isSelected());
            } else {
                assertFalse(week.isSelected());
            }
        }

    }

    @Test
    public void testDeselect() throws Exception {

        mMonth.select(mToday);
        mMonth.deselect(mToday);

        assertFalse(mMonth.isSelected());

        List<Week> weeks = mMonth.getWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            Week week = weeks.get(i);
            assertFalse(week.isSelected());
        }

    }

    @Test
    public void testDeselectNotInWeek() throws Exception {

        mMonth.select(mToday);
        mMonth.deselect(LocalDate.parse("2014-10-13"));

        assertTrue(mMonth.isSelected());

        List<Week> weeks = mMonth.getWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            Week week = weeks.get(i);
            if (i == 1) {
                assertTrue(week.isSelected());
            } else {
                assertFalse(week.isSelected());
            }
        }

    }

    @Test
    public void testSelectNull() throws Exception {

        assertFalse(mMonth.select(null));
        assertFalse(mMonth.isSelected());

    }

    @Test
    public void testSelectNotIn() throws Exception {

        assertFalse(mMonth.select(LocalDate.parse("2014-09-13")));
        assertFalse(mMonth.isSelected());

        for (Week week : mMonth.getWeeks()) {
            assertFalse(week.isSelected());
        }

    }

    @Test
    public void testSelect() throws Exception {

        assertTrue(mMonth.select(mToday));
        assertTrue(mMonth.isSelected());

        List<Week> weeks = mMonth.getWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            Week week = weeks.get(i);
            if (i == 1) {
                assertTrue(week.isSelected());
            } else {
                assertFalse(week.isSelected());
            }
        }

    }

    @Test
    public void testGetSelectedIndexUnselected() throws Exception {
        assertEquals(-1, mMonth.getSelectedIndex());
    }

    @Test
    public void testGetSelectedIndexDeselect() throws Exception {
        mMonth.select(mToday);
        mMonth.deselect(mToday);
        assertEquals(-1, mMonth.getSelectedIndex());
    }

    @Test
    public void testGetSelectedIndex() throws Exception {

        mMonth.select(mToday);
        assertEquals(1, mMonth.getSelectedIndex());

    }

    @Test
    public void testBuild() throws Exception {

        mMonth.build();

        List<Week> weeks = mMonth.getWeeks();

        LocalDate base = LocalDate.parse("2014-09-29");

        assertEquals(5, weeks.size());

        for (int i = 0; i < weeks.size(); i++) {

            assertEquals(base.plusWeeks(i), weeks.get(i).getFrom());
            assertEquals(base.plusWeeks(i).withDayOfWeek(DateTimeConstants.SUNDAY), weeks.get(i).getTo());

        }

    }

    @Test
    public void testGetFirstDayOfCurrentMonthNull() throws Exception {
        assertNull(mMonth.getFirstDateOfCurrentMonth(null));
    }

    @Test
    public void testGetFirstDayOfCurrentMonthNotIn() throws Exception {
        assertNull(mMonth.getFirstDateOfCurrentMonth(LocalDate.parse("2014-09-30")));
    }

    @Test
    public void testGetFirstDayOfCurrentMonthNotInYear() throws Exception {
        assertNull(mMonth.getFirstDateOfCurrentMonth(LocalDate.parse("2015-10-13")));
    }

    @Test
    public void testGetFirstDateOfCurrentMonth() throws Exception {
        assertEquals(LocalDate.parse("2014-10-01"), mMonth.getFirstDateOfCurrentMonth(mToday));
    }
}
