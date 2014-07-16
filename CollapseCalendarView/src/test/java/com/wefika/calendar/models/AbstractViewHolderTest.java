package com.wefika.calendar.models;

import android.test.AndroidTestCase;
import android.view.View;

import static org.mockito.Mockito.mock;

public class AbstractViewHolderTest extends AndroidTestCase {

    AbstractViewHolder mViewHolder;

    public void setUp() throws Exception {

        mViewHolder = new SizeViewHolder(0, 10);

    }


    public void testAnimate() throws Exception {

    }


    public void testSetView() throws Exception {

        View view = mock(View.class);
        mViewHolder.setView(view);

        assertTrue(view == mViewHolder.getView());

    }


    public void testSetDelay() throws Exception {

        float delay = 20;
        mViewHolder.setDelay(delay);

        assertEquals(delay, mViewHolder.getDelay(), 0);

    }


    public void testSetDuration() throws Exception {

        float duration = 200;
        mViewHolder.setDuration(duration);

        assertEquals(duration, mViewHolder.getDuration(), 0);

    }


    public void testOnFinish() throws Exception {

    }


    public void testGetEnd() throws Exception {

    }


    public void testShouldAnimate() throws Exception {

    }


    public void testOnAnimate() throws Exception {

    }


    public void testGetRelativeTime() throws Exception {

    }
}