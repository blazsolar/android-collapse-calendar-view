package com.wefika.calendar;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.wefika.calendar.manager.CalendarManager;
import com.wefika.calendar.manager.Week;
import com.wefika.calendar.mock.MockActivity;

import org.joda.time.LocalDate;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CollapseCalendarViewTest extends ActivityUnitTestCase<MockActivity> {

    CollapseCalendarView mView;

    public CollapseCalendarViewTest() {
        super(MockActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    public void testInitNull() throws Exception {

        init();

        mView.init(null);
        assertNull(mView.getManager());

    }

    public void testInit() throws Exception {

        init();

        CalendarManager manager = mock(CalendarManager.class);
        when(manager.hasPrev()).thenReturn(false);
        when(manager.hasNext()).thenReturn(false);
        when(manager.getHeaderText()).thenReturn("Test");
        when(manager.getState()).thenReturn(CalendarManager.State.WEEK);
        when(manager.getUnits()).thenReturn(new Week(LocalDate.now(), LocalDate.now(), null, null));

        mView.init(manager);

        assertSame(manager, mView.getManager());
        verify(manager).hasNext();
        verify(manager).hasPrev();
        verify(manager).getHeaderText();
        verify(manager).getState();
        verify(manager).getUnits();

    }
//
//
//    public void testGetManager() throws Exception {
//
//    }
//
//
//    public void testOnClick() throws Exception {
//
//    }
//
//
//    public void testDispatchDraw() throws Exception {
//
//    }
//
//
//    public void testGetState() throws Exception {
//
//    }
//
//
//    public void testSetListener() throws Exception {
//
//    }
//
//
//
//    public void testSetTitle() throws Exception {
//
//    }
//
//
//
//    public void testOnInterceptTouchEvent() throws Exception {
//
//    }
//
//
//    public void testOnTouchEvent() throws Exception {
//
//    }
//
//
//    public void testOnFinishInflate() throws Exception {
//
//    }
//
//
//    public void testPopulateLayout() throws Exception {
//
//    }
//
//
//    public void testGetWeeksView() throws Exception {
//
//    }
//
//
//    public void testGetSelectedDate() throws Exception {
//
//    }
//
//
//    public void testOnDetachedFromWindow() throws Exception {
//
//    }

    private void init() {

        startActivity(new Intent(getInstrumentation().getTargetContext(), MockActivity.class), null, null);
        mView = getActivity().getCallendarView();

    }
}