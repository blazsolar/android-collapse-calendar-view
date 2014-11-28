package com.wefika.calendar;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;

import com.wefika.calendar.manager.CalendarManager;
import com.wefika.calendar.manager.Week;
import com.wefika.calendar.mock.MockActivity;

import org.joda.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CollapseCalendarViewTest extends ActivityUnitTestCase<MockActivity> {

    CollapseCalendarView mView;
    CalendarManager mManager;


    public CollapseCalendarViewTest() {
        super(MockActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        mManager = mock(CalendarManager.class);
        when(mManager.getHeaderText()).thenReturn("Test");
        when(mManager.getState()).thenReturn(CalendarManager.State.WEEK);
        when(mManager.getUnits()).thenReturn(new Week(LocalDate.now(), LocalDate.now(), null, null));
        when(mManager.getSelectedDay()).thenReturn(LocalDate.now());
    }

    public void testInitNull() throws Exception {

        init();

        mView.init(null);
        assertNull(mView.getManager());

    }

    public void testInit() throws Exception {

        init();

        mView.init(mManager);

        assertSame(mManager, mView.getManager());
        verify(mManager).hasNext();
        verify(mManager).hasPrev();
        verify(mManager).getHeaderText();
        verify(mManager).getState();
        verify(mManager).getUnits();

    }

    public void testInitListener() throws Exception {

        init();

        CollapseCalendarView.OnDateSelect listener = mock(CollapseCalendarView.OnDateSelect.class);
        mView.setListener(listener);
        mView.init(mManager);

        verify(listener).onDateSelected(LocalDate.now());

    }

    public void testOnClickNullManager() throws Exception {

        init();

        mView.onClick(mock(View.class)); // should not crash

    }

    public void testOnClickPrev() throws Exception {

        init();
        mView.init(mManager);

        View prev = mock(View.class);
        when(prev.getId()).thenReturn(R.id.prev);

        mView.onClick(prev);
        verify(mManager).prev();
        verify(mManager).getHeaderText(); // shod only be called when in init()

    }

    public void testOnClickPrevTrue() throws Exception {

        when(mManager.prev()).thenReturn(true);

        init();
        mView.init(mManager);

        View prev = mock(View.class);
        when(prev.getId()).thenReturn(R.id.prev);

        mView.onClick(prev);
        verify(mManager).prev();
        verify(mManager, times(2)).getHeaderText(); // shod only be called when in init() and on button click

    }

    public void testOnClickNext() throws Exception {

        init();
        mView.init(mManager);

        View next = mock(View.class);
        when(next.getId()).thenReturn(R.id.next);

        mView.onClick(next);
        verify(mManager).next();
        verify(mManager).getHeaderText(); // shod only be called when in init()

    }

    public void testOnClickNextTrue() throws Exception {

        when(mManager.next()).thenReturn(true);

        init();
        mView.init(mManager);

        View next = mock(View.class);
        when(next.getId()).thenReturn(R.id.next);

        mView.onClick(next);
        verify(mManager).next();
        verify(mManager, times(2)).getHeaderText();// shod only be called when in init() and on button click

    }

    public void testOnClickInvalid() throws Exception {

        init();
        mView.init(mManager);
        reset(mManager);

        mView.onClick(mock(View.class));

        verifyNoMoreInteractions(mManager);

    }

//    public void testDispatchDraw() throws Exception {
//
//    }


    public void testGetStateNull() throws Exception {

        init();

        assertNull(mView.getState());
    }

    public void testGetState() throws Exception {

        init();
        mView.init(mManager);

        assertEquals(CalendarManager.State.WEEK, mManager.getState());
    }

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