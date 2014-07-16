package com.wefika.calendar.widget;

import android.test.AndroidTestCase;

import com.wefika.calendar.R;

import org.apache.commons.lang3.ArrayUtils;

public class DayViewTest extends AndroidTestCase {

    DayView mView;

    public void setUp() throws Exception {
        mView = new DayView(getContext());
    }


    public void testSetCurrent() throws Exception {

        assertFalse(mView.isCurrent());

        mView.setCurrent(true);

        assertTrue(mView.isCurrent());
        assertTrue(ArrayUtils.contains(mView.getDrawableState(), R.attr.state_current));

    }


    public void testSetCurrentSame() throws Exception {

        mView.setCurrent(false);
        assertFalse(mView.isCurrent());

        assertFalse(ArrayUtils.contains(mView.getDrawableState(), R.attr.state_current));

    }


    public void testOnCreateDrawableState() throws Exception {
        assertFalse(ArrayUtils.contains(mView.onCreateDrawableState(0), R.attr.state_current));
    }


    public void testOnCreateDrawableStateCurrent() throws Exception {

        mView.setCurrent(true);
        assertTrue(ArrayUtils.contains(mView.onCreateDrawableState(0), R.attr.state_current));

    }
}