
package com.wefika.calendar.models;

import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void testOnFinishDone() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.onFinish(true);

        verify(view).setVisibility(View.VISIBLE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }

    @Test
    public void testOnFinishNotDone() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.onFinish(false);

        verify(view).setVisibility(View.GONE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }

    @Test
    public void testAnimateStart() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(11);

        verify(view).setVisibility(View.VISIBLE);
        assertEquals(1f / 100 * 100, params.height, 0);
        verify(view).requestLayout();

    }

    @Test
    public void testAnimateFinishExpand() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(11);

        reset(view);
        reset(params);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(112);

        verify(view).setVisibility(View.VISIBLE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }

    @Test
    public void testAnimateFinishCollapse() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(11);

        reset(view);
        reset(params);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(0);

        verify(view).setVisibility(View.GONE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }

    @Test
    public void testAnimateDirectFinishExpand() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(112);

        verify(view).setVisibility(View.VISIBLE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }

    @Test
    public void testAnimateDirectFinishedCollapse() throws Exception {

        View view = mViewHolder.getView();
        ViewGroup.LayoutParams params = mock(ViewGroup.LayoutParams.class);
        when(view.getLayoutParams()).thenReturn(params);

        mViewHolder.animate(0);

        verify(view).setVisibility(View.GONE);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, params.height);

    }
}
