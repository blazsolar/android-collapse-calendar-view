/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Blaž Šolar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wefika.calendar.manager;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.models.AbstractViewHolder;
import com.wefika.calendar.models.SizeViewHolder;
import com.wefika.calendar.models.StubViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class ProgressManagerImpl extends ProgressManager {

    public ProgressManagerImpl(@NotNull CollapseCalendarView calendarView, int activeWeek, boolean fromMonth) {
        super(calendarView, activeWeek, fromMonth);

        mCalendarView = calendarView;
        mWeeksView = calendarView.getWeeksView();

        if (!fromMonth) {
            initMonthView();
        } else {
            initWeekView();
        }

    }

    @Override
    public void finish(final boolean expanded) {

        mCalendarView.post(new Runnable() { // to prevent flickering
            @Override
            public void run() {
                mCalendarView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mWeeksView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

                for (AbstractViewHolder view : mViews) {
                    view.onFinish(true);
                }

                if (!expanded) {
                    CalendarManager manager = mCalendarView.getManager();
                    if (mFromMonth) {
                        manager.toggleView();
                    } else {
                        manager.toggleToWeek(mActiveIndex);
                    }
                    mCalendarView.populateLayout();
                }
            }
        });
    }

    private void initMonthView() {

        mCalendarHolder = new SizeViewHolder(mCalendarView.getHeight(), 0);
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(mWeeksView.getHeight(), 0);
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

        mCalendarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCalendarView.getViewTreeObserver().removeOnPreDrawListener(this);

                mCalendarHolder.setMaxHeight(mCalendarView.getHeight());
                mWeeksHolder.setMaxHeight(mWeeksView.getHeight());

                mCalendarView.getLayoutParams().height = mCalendarHolder.getMinHeight();
                mWeeksView.getLayoutParams().height = mCalendarHolder.getMinHeight();

                initializeChildren();

                setInitialized(true);

                return false;
            }
        });
    }

    private void initWeekView() {

        mCalendarHolder = new SizeViewHolder(0, mCalendarView.getHeight());
        mCalendarHolder.setView(mCalendarView);
        mCalendarHolder.setDelay(0);
        mCalendarHolder.setDuration(1);

        mWeeksHolder = new SizeViewHolder(0, mWeeksView.getHeight());
        mWeeksHolder.setView(mWeeksView);
        mWeeksHolder.setDelay(0);
        mWeeksHolder.setDuration(1);

        initializeChildren();

        mCalendarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCalendarView.getViewTreeObserver().removeOnPreDrawListener(this);

                mCalendarHolder.setMinHeight(mCalendarView.getHeight());
                mWeeksHolder.setMinHeight(mWeeksView.getHeight());

                mCalendarView.getLayoutParams().height = mCalendarHolder.getMaxHeight();
                mWeeksView.getLayoutParams().height = mCalendarHolder.getMaxHeight();

                setInitialized(true);

                return false;
            }
        });
    }

    private void initializeChildren() {

        int childCount = mWeeksView.getChildCount();

        // FIXME do not assume that all views are the same height
        mViews = new AbstractViewHolder[childCount];
        for (int i = 0; i < childCount; i++) {

            View view = mWeeksView.getChildAt(i);

            int activeIndex = getActiveIndex();

            AbstractViewHolder holder;
            if (i == activeIndex) {
                holder = new StubViewHolder();
            } else {
                SizeViewHolder tmpHolder = new SizeViewHolder(0, view.getHeight());

                final int duration = mWeeksHolder.getMaxHeight() - view.getHeight();

                if (i < activeIndex) {
                    tmpHolder.setDelay(view.getTop() * 1.0f / duration);
                } else {
                    tmpHolder.setDelay((view.getTop() - view.getHeight()) * 1.0f / duration);
                }
                tmpHolder.setDuration(view.getHeight() * 1.0f / duration);

                holder = tmpHolder;

                view.setVisibility(View.GONE);
            }

            holder.setView(view);

            mViews[i] = holder;
        }

    }

}
