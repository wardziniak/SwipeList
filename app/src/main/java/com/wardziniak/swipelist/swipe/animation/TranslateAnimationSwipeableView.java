package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public class TranslateAnimationSwipeableView extends SwipeableViewAnimation {

    private float currentTranslation = 0.0f;

    public TranslateAnimationSwipeableView(ItemSwipeListView itemSwipeListView) {
        super(itemSwipeListView);
    }

    @Override
    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        currentTranslation = itemSwipeListView.getCurrentTranslationX();
        super.onStartMonitoring(itemSwipeListView, motionX);
    }

    @Override
    public void move(ItemSwipeListView itemSwipeListView, float fraction) {
        currentTranslation += fraction * itemSwipeListView.getWidth();
        if (isMovePosibile())
            itemSwipeListView.changeTranslation(currentTranslation);
        else
            currentTranslation -= fraction * itemSwipeListView.getWidth();
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
    public SwipeType getSwipeType() {
        return currentTranslation > 0 ? SwipeType.RIGHT : currentTranslation < 0 ? SwipeType.LEFT : SwipeType.NONE;
    }

    @Override
    public boolean isOutOfScope() {
        return Math.abs(currentTranslation) > itemSwipeListView.getWidth();
    }
}
