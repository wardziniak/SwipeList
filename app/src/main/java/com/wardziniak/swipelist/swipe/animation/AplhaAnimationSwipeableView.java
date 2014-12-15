package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public class AplhaAnimationSwipeableView extends SwipeableViewAnimation {

    private float currentAlpha = 0.0f;

    @Override
    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        currentAlpha = itemSwipeListView.getCurrentAlpha();
        super.onStartMonitoring(itemSwipeListView, motionX);
    }

    @Override
    public void move(ItemSwipeListView itemSwipeListView, float fraction) {
        currentAlpha += fraction;
        itemSwipeListView.setSwipeAlpha(currentAlpha);
    }

    @Override
    public List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, float x, int motionPosition, AnimationType animationType) {
        return null;
    }

    @Override
    public float getFractionOfNewPosition(ItemSwipeListView itemSwipeListView, float currentPosition, float change) {
        return 0;
    }
}
