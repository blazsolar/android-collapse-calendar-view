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
import org.jetbrains.annotations.Nullable;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Created by Blaz Solar on 24/05/14.
 */
public abstract class RangeUnit extends CalendarUnit {

    @Nullable private LocalDate mMinDate;
    @Nullable private LocalDate mMaxDate;

    protected RangeUnit(@NotNull LocalDate from, @NotNull LocalDate to, @NotNull String headerPattern,
                        @NotNull LocalDate today, @Nullable LocalDate minDate, @Nullable LocalDate maxDate) {
        super(from, to, headerPattern, today);

        mMinDate = minDate;
        mMaxDate = maxDate;
    }

    @Nullable
    public LocalDate getMinDate() {
        return mMinDate;
    }

    @Nullable
    public LocalDate getMaxDate() {
        return mMaxDate;
    }

    public int getFirstWeek(LocalDate currentMonth) {
        LocalDate from = getFrom();
        if (mMinDate != null && mMinDate.isAfter(from)) { // TODO check if same month
            return getWeekInMonth(mMinDate);
        } else {
            return getWeekInMonth(currentMonth);
        }
    }

    LocalDate getFirstEnabled() {
        LocalDate from = getFrom();
        if (mMinDate != null && from.isBefore(mMinDate)) {
            return mMinDate;
        } else {
            return from;
        }
    }

    @Nullable
    abstract LocalDate getFirstDateOfCurrentMonth(@NotNull LocalDate currentMonth);

    protected int getWeekInMonth(@NotNull LocalDate date) {
        if (date != null) {
            LocalDate first = date.withDayOfMonth(1).withDayOfWeek(1);
            Days days = Days.daysBetween(first, date);
            return days.dividedBy(7).getDays();
        } else {
            return 0;
        }
    }
}
