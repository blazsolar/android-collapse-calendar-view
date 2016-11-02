package com.github.gfranks.collapsible.calendar.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.gfranks.collapsible.calendar.R;

public class DayView extends LinearLayout {

    private static final int[] STATE_CURRENT = {R.attr.state_current};

    private boolean mCurrent;
    private boolean mHasEvent;
    private int mEventIndicatorColor = Color.DKGRAY;

    private TextView mDayViewText;
    private ImageView mEventIndicator;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDayViewText = (TextView) findViewById(R.id.day_view_text);
        mEventIndicator = (ImageView) findViewById(R.id.day_view_indicator);

        updateEventIndicatorDrawable();
        mEventIndicator.setVisibility(View.INVISIBLE);
    }

    public void setSelected(boolean selected, int selectedBackgroundColor, int selectedTextColor, int normalTextColor) {
        super.setSelected(selected);
        GradientDrawable drawable = (GradientDrawable) getBackground();
        if (selected) {
            setTextColor(selectedTextColor);
            drawable.setColor(selectedBackgroundColor);
        } else {
            setTextColor(normalTextColor);
            drawable.setColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public void setText(String text) {
        mDayViewText.setText(text);
    }

    public void setText(int resId) {
        mDayViewText.setText(resId);
    }

    public void setTextColor(int color) {
        mDayViewText.setTextColor(color);
    }

    public void setEventIndicatorColor(int color) {
        mEventIndicatorColor = color;
        updateEventIndicatorDrawable();
    }

    public boolean isHsEvent() {
        return mHasEvent;
    }

    public void setHasEvent(boolean hasEvent) {
        mHasEvent = hasEvent;
        getChildAt(1).setVisibility(mHasEvent ? View.VISIBLE : View.INVISIBLE);
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public void setCurrent(boolean current) {
        if (mCurrent != current) {
            mCurrent = current;
            refreshDrawableState();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] state = super.onCreateDrawableState(extraSpace + 1);

        if (mCurrent) {
            mergeDrawableStates(state, STATE_CURRENT);
        }

        return state;
    }

    private void updateEventIndicatorDrawable() {
        GradientDrawable drawable;
        if (mEventIndicator.getDrawable() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = (GradientDrawable) getContext().getDrawable(R.drawable.bg_indicator);
            } else {
                drawable = (GradientDrawable) getResources().getDrawable(R.drawable.bg_indicator);
            }
        } else {
            drawable = (GradientDrawable) mEventIndicator.getDrawable();
        }

        drawable.setColor(mEventIndicatorColor);
        mEventIndicator.setImageDrawable(drawable);
    }
}
