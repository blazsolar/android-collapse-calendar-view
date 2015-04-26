package com.wefika.calendar.models;

import android.view.View;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AbstractViewHolderTest {

    AbstractViewHolder mViewHolder;

    @Before
    public void setUp() throws Exception {

        mViewHolder = new SizeViewHolder(0, 10);

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

}
