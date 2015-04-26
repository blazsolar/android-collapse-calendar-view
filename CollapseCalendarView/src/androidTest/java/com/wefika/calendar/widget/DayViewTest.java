package com.wefika.calendar.widget;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wefika.calendar.R;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DayViewTest {

    DayView mView;

    @Before
    public void setUp() throws Exception {
        mView = new DayView(InstrumentationRegistry.getTargetContext());
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
