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

package com.wefika.calendar.models;

import android.view.View;

/**
 * Created by Blaz Solar on 16/04/14.
 */
public abstract  class AbstractViewHolder {

    private View view;
    private float delay;
    private float duration;

    private boolean mAnimating;

    public void animate(float time) {
        if (shouldAnimate(time)) {
            mAnimating = true;
            onAnimate(getRelativeTime(time));
        } else if (mAnimating) {
            mAnimating = false;
            onFinish(Math.round(getRelativeTime(time)) >= 1);
        } else if (delay > time) { // FIXME this should only be called once
            onFinish(false);
        } else if (time > getEnd()) { // FIXME this should only be called once
            onFinish(true);
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public View getView() {
        return view;
    }

    public float getDelay() {
        return delay;
    }

    public float getDuration() {
        return duration;
    }

    public abstract void onFinish(boolean done);

    protected float getEnd() {
        return delay + duration;
    }

    protected boolean shouldAnimate(float time) {
        return delay <= time && time <= getEnd();
    }

    protected abstract void onAnimate(float time);

    protected float getRelativeTime(float time) {
        return  (time - getDelay()) * 1f / getDuration();
    }

}
