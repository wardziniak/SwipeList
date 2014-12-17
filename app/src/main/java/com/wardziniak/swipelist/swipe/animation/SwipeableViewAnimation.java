package com.wardziniak.swipelist.swipe.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import com.wardziniak.swipelist.SwipeableView;
import com.wardziniak.swipelist.swipe.AnimationType;
import com.wardziniak.swipelist.swipe.DefaultAnimatorListener;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;

import java.util.List;

/**
 * Created by wardziniak on 12/14/14.
 */
public abstract class SwipeableViewAnimation implements Animator.AnimatorListener {

    public final static int LEFT = 1;

    public final static int RIGHT = 2;

    private float motionXStart;

    private boolean leftSwipe = true;

    private boolean rightSwipe = true;

    protected ItemSwipeListView itemSwipeListView;

    private AnimationType animationType;

    private boolean animationCancel;

    public SwipeableViewAnimation(ItemSwipeListView itemSwipeListView) {
        this.itemSwipeListView = itemSwipeListView;
    }

    public abstract void move(ItemSwipeListView itemSwipeListView, float fraction);

    public void onStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        this.motionXStart = motionX;
    }

    public void onReStartMonitoring(ItemSwipeListView itemSwipeListView, float motionX) {
        this.motionXStart = motionX;
    }

    public abstract List<ObjectAnimator> createSwipeAnimation(ItemSwipeListView itemSwipeListView, final float x, final int motionPosition,
                final AnimationType animationType);

    public abstract float getFractionOfNewPosition(ItemSwipeListView itemSwipeListView, float currentPosition, float change);

    public void reset() {
        this.motionXStart = 0;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        this.animationCancel = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {
        this.animationCancel = true;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void startAnimation(int motionPosition, float velocityX) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(itemSwipeListView.getFrontView(), "x", 0);
        objectAnimator.addListener(this);
    }

    public boolean isMovePosibile() {
        switch (getSwipeType()) {
            case LEFT:
                return leftSwipe && isOutOfScope();
            case RIGHT:
                return rightSwipe && isOutOfScope();
            default:
                return isOutOfScope();
        }
    }

    public abstract SwipeType getSwipeType();

    public abstract boolean isOutOfScope();
}
