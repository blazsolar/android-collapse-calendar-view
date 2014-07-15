package com.wefika.calendar.widget;

import android.app.Activity;
import android.view.LayoutInflater;

import com.wefika.calendar.R;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.RoboLayoutInflater;

import static org.junit.Assert.*;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DayViewTest {

    DayView mView;

    @Before
    public void setUp() throws Exception {
        mView = new DayView(Robolectric.application);
    }

    @Test
    public void testSetCurrent() throws Exception {

        assertFalse(mView.isCurrent());

        mView.setCurrent(true);

        assertTrue(mView.isCurrent());
        assertTrue(ArrayUtils.contains(mView.getDrawableState(), R.attr.state_current));

    }

    @Test
    public void testSetCurrentSame() throws Exception {

        mView.setCurrent(false);
        assertFalse(mView.isCurrent());

        assertFalse(ArrayUtils.contains(mView.getDrawableState(), R.attr.state_current));

    }

    @Test
    public void testOnCreateDrawableState() throws Exception {
        assertFalse(ArrayUtils.contains(mView.onCreateDrawableState(0), R.attr.state_current));
    }

    @Test
    public void testOnCreateDrawableStateCurrent() throws Exception {

        mView.setCurrent(true);
        assertTrue(ArrayUtils.contains(mView.onCreateDrawableState(0), R.attr.state_current));

    }
}