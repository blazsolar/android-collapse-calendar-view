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

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Day {

    private static final DateTimeFormatter mFormatter = DateTimeFormat.forPattern("d");

    @NotNull private final LocalDate mDate;
    private boolean mToday;
    private boolean mSelected;
    private boolean mEnabled;
    private boolean mCurrent;

    public Day(@NotNull LocalDate date, boolean today) {
        mDate = date;
        mToday = today;
        mEnabled = true;
        mCurrent = true;
    }

    @NotNull
    public LocalDate getDate() {
        return mDate;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public void setCurrent(boolean current) {
        mCurrent = current;
    }

    public boolean isToday() {
        return mToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        if (mEnabled != day.mEnabled) return false;
        if (mSelected != day.mSelected) return false;
        if (mToday != day.mToday) return false;
        if (!mDate.isEqual(day.mDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mDate.hashCode();
        result = 31 * result + (mToday ? 1 : 0);
        result = 31 * result + (mSelected ? 1 : 0);
        result = 31 * result + (mEnabled ? 1 : 0);
        return result;
    }

    @NotNull
    public String getText() {
        return mDate.toString(mFormatter);
    }
}
