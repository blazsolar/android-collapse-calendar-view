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
import android.view.ViewGroup;

/**
 * Created by Blaz Solar on 17/04/14.
 */
public class SizeViewHolder extends AbstractViewHolder {

    private int mMinHeight;
    private int mMaxHeight;

    public SizeViewHolder(int minHeight, int maxHeight) {
        mMinHeight = minHeight;
        mMaxHeight = maxHeight;
    }

    public int getHeight() {
        return mMaxHeight - mMinHeight;
    }

    public void setMinHeight(int minHeight) {
        mMinHeight = minHeight;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public int getMinHeight() {
        return mMinHeight;
    }

    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }

    @Override
    public void onFinish(boolean done) {
        if (done) {
            onShown();
        } else {
            onHidden();
        }
    }

    public void onShown() {
        getView().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getView().setVisibility(View.VISIBLE);
    }

    public void onHidden() {
        getView().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getView().setVisibility(View.GONE);
    }

    @Override
    protected void onAnimate(float time) {
        View view = getView();
        view.setVisibility(View.VISIBLE);
        view.getLayoutParams().height = (int) (getMinHeight() + getHeight() * time);
        view.requestLayout();
    }
}
