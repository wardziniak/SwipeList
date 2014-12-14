package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public abstract class SwipeableViewAnimation {

    public abstract void move(ItemSwipeListView itemSwipeListView, float x);

    public abstract List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, final float x, final int motionPosition,
                final AnimationType animationType);

    public abstract float getNewViewPosition(ItemSwipeListView itemSwipeListView, float x);
}
