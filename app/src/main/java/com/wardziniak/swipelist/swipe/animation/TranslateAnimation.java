package com.wardziniak.swipelist.swipe.animation;

import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public class TranslateAnimation extends SwipeableViewAnimation {
    @Override
    public void move(ItemSwipeListView itemSwipeListView, float x) {

    }

    @Override
    public List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, float x, int motionPosition, AnimationType animationType) {
        return null;
    }

    @Override
    public float getNewViewPosition(ItemSwipeListView itemSwipeListView, float x) {
        return 0;
    }
}
