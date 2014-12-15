package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public abstract class SwipeableViewAnimation {

    private float motionXStart;

    public abstract void move(ItemSwipeListView itemSwipeListView, float fraction);

    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        this.motionXStart = motionX;
    }

    public abstract List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, final float x, final int motionPosition,
                final AnimationType animationType);

    public abstract float getFractionOfNewPosition(ItemSwipeListView itemSwipeListView, float currentPosition, float change);
}
