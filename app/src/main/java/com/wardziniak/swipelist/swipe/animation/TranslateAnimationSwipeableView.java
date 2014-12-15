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

    @Override
    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        currentTranslation = itemSwipeListView.getCurrentTranslationX();
        super.onStartMonitoring(itemSwipeListView, motionX);
    }

    @Override
    public void move(ItemSwipeListView itemSwipeListView, float fraction) {
        currentTranslation += fraction * itemSwipeListView.getWidth();
        itemSwipeListView.changeTranslation(currentTranslation);
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
