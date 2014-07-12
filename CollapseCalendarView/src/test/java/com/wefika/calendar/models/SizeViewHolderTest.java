
package com.wefika.calendar.models;

import android.view.View;

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
public class SizeViewHolderTest {

    SizeViewHolder mViewHolder;

    @Before
    public void setUp() throws Exception {

        mViewHolder = new SizeViewHolder(0, 100);
        mViewHolder.setDelay(10);
        mViewHolder.setDuration(100);
        mViewHolder.setView(mock(View.class));

    }

    @Test
    public void testGetHeight() throws Exception {

        assertEquals(100, mViewHolder.getHeight());
        assertEquals(0, mViewHolder.getMinHeight());
        assertEquals(100, mViewHolder.getMaxHeight());

    }

    @Test
    public void testSetHeight() throws Exception {

        mViewHolder.setMinHeight(25);
        mViewHolder.setMaxHeight(325);

        assertEquals(300, mViewHolder.getHeight());
        assertEquals(25, mViewHolder.getMinHeight());
        assertEquals(325, mViewHolder.getMaxHeight());

    }
}