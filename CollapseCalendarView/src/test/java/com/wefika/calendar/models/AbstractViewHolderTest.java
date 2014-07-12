package com.wefika.calendar.models;

import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AbstractViewHolderTest {

    AbstractViewHolder mViewHolder;

    @Before
    public void setUp() throws Exception {

        mViewHolder = new SizeViewHolder(0, 10);

    }

    @Test
    public void testAnimate() throws Exception {

    }

    @Test
    public void testSetView() throws Exception {

        View view = mock(View.class);
        mViewHolder.setView(view);

        assertTrue(view == mViewHolder.getView());

    }

    @Test
    public void testSetDelay() throws Exception {

        float delay = 20;
        mViewHolder.setDelay(delay);

        assertEquals(delay, mViewHolder.getDelay(), 0);

    }

    @Test
    public void testSetDuration() throws Exception {

        float duration = 200;
        mViewHolder.setDuration(duration);

        assertEquals(duration, mViewHolder.getDuration(), 0);

    }

    @Test
    public void testOnFinish() throws Exception {

    }

    @Test
    public void testGetEnd() throws Exception {

    }

    @Test
    public void testShouldAnimate() throws Exception {

    }

    @Test
    public void testOnAnimate() throws Exception {

    }

    @Test
    public void testGetRelativeTime() throws Exception {

    }
}