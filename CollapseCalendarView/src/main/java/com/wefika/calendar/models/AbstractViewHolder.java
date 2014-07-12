package com.wefika.calendar.models;

import android.view.View;

/**
 * Created by Blaz Solar on 16/04/14.
 */
public abstract class AbstractViewHolder {

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
