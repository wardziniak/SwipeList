package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public class AplhaAnimationSwipeableView extends SwipeableViewAnimation {

    // If greater than 0 right swipe. otherwise left
    private float currentAlphaBeforeNormalization = 0.0f;

    public AplhaAnimationSwipeableView(ItemSwipeListView itemSwipeListView) {
        super(itemSwipeListView);
    }

    @Override
    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        currentAlphaBeforeNormalization = 1.0f;//itemSwipeListView.getCurrentAlpha();
        super.onStartMonitoring(itemSwipeListView, motionX);
    }

    @Override
    public void onReStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        super.onReStartMonitoring(itemSwipeListView, motionX);
        currentAlphaBeforeNormalization = currentAlphaBeforeNormalization > 0 ? (1 - itemSwipeListView.getCurrentAlpha()) :
                -1 * (1 - itemSwipeListView.getCurrentAlpha());
    }

    @Override
    public void move(ItemSwipeListView itemSwipeListView, float fraction) {
        currentAlphaBeforeNormalization += fraction;
        if (isMovePosibile())
            itemSwipeListView.setSwipeAlpha(currentAlphaBeforeNormalization);
        else
            currentAlphaBeforeNormalization -= fraction;
    }

    @Override
    public List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, float x, int motionPosition, AnimationType animationType) {
        return null;
    }

    @Override
    public float getFractionOfNewPosition(ItemSwipeListView itemSwipeListView, float currentPosition, float change) {
        return 0;
    }

    @Override
    public void reset() {
        super.reset();
        currentAlphaBeforeNormalization = 0.0f;
    }

    @Override
    public void startAnimation(int motionPosition, float velocityX) {
        super.startAnimation(motionPosition, velocityX);
    }

    @Override
    public SwipeType getSwipeType() {
        return currentAlphaBeforeNormalization > 0 ? SwipeType.RIGHT : currentAlphaBeforeNormalization < 0 ? SwipeType.LEFT : SwipeType.NONE;
    }

    @Override
    public boolean isOutOfScope() {
        return Math.abs(currentAlphaBeforeNormalization) > 1;
    }
}
