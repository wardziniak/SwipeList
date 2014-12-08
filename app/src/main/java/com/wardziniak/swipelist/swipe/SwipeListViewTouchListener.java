package com.wardziniak.swipelist.swipe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by wardziniak on 12/6/14.
 */
public class SwipeListViewTouchListener implements View.OnTouchListener {

    private final static int TOUCH_STATE_NONE = 0;
    private final static int TOUCH_STATE_SCROLLING_X = 1;
    private final static int TOUCH_STATE_SCROLLING_Y = 2;

    private final static float MIN_VELOCITY_TO_SWIPE = 100.0f;

    private SwipeListView swipeListView;

    private int motionX;
    private int motionY;

    private int touchSlop;
    private int touchState;
    private int motionPosition;
    private ItemSwipeListView swipeView;
    private boolean isOnFrontView = false;

    private AnimatorSet currentAnimation = new AnimatorSet();


    private VelocityTracker velocityTracker;

    public SwipeListViewTouchListener(SwipeListView swipeListView, int touchSlop) {
        this.swipeListView = swipeListView;
        this.touchSlop = touchSlop;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        // We should return true if we handle that event
        Log.d("DUPA", "SwipeListViewTouchListener:onTouch:" + motionEvent.getAction() + ":" + touchState);

        // Use raw data, because when we check if event was dispatched to frontView, we need to compare to getLocationOnScreen
        final int x = (int) motionEvent.getRawX();
        final int y = (int) motionEvent.getRawY();
        // Check touch action and decide, what to do
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                motionX = x;//(int) motionEvent.getX();
                motionY = y;//(int) motionEvent.getY();
                touchState = TOUCH_STATE_NONE;
                initVelocityTrackerIfNotExists();
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                checkMotionEventLocation(motionEvent);
                // we cancel only if touch is on frontView
                if (isOnFrontView)
                    cancelAllItemAnimations(swipeView);
                return false;
            case MotionEvent.ACTION_MOVE:
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X || !isOnFrontView)
                    return false;
                Log.d("DUPA", "SwipeListViewTouchListener:onTouch:swipeView" + swipeView.getX());
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                swipeView.moveFrontView(x - motionX);
                motionX = x;//(int) motionEvent.getX();
                motionY = y;//(int) motionEvent.getY();
                return true;
            case MotionEvent.ACTION_UP:
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                final float velocityX = velocityTracker.getXVelocity();
                recycleVelocityTracker();
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X || !isOnFrontView)
                    return false;

                makeSwipe(velocityX);
                return true;
        }
        return false;
    }

    private void checkMotionEventLocation(MotionEvent motionEvent) {
        Log.d("DUPA", "checkMotionEventLocation::" + motionX + ":" + motionY);
        motionPosition = swipeListView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        swipeView = (ItemSwipeListView) swipeListView.getChildAt(motionPosition - swipeListView.getFirstVisiblePosition());
        isOnFrontView = swipeView.isFrontViewContains(motionX, motionY);
    }

    public int getMotion(MotionEvent motionEvent) {
        // We do not change touch type;
        Log.d("DUPA", "getMotion:" + touchState);
        if (touchState != TOUCH_STATE_NONE)
            return touchState;
        int diffX = (int) Math.abs(motionEvent.getRawX() - motionX);
        int diffY = (int) Math.abs(motionEvent.getRawY() - motionY);
        touchState = diffY > touchSlop ? TOUCH_STATE_SCROLLING_Y : diffX > touchSlop ? TOUCH_STATE_SCROLLING_X : touchState;
//        if (touchState == TOUCH_STATE_SCROLLING_X) {
//            motionPosition = swipeListView.pointToPosition(motionX, motionY);
//            swipeView = (ItemSwipeListView) swipeListView.getChildAt(swipeListView.getMotionPosition() - swipeListView.getFirstVisiblePosition());
//        }
        return touchState;
    }

    private void resetAnimation() {
        for (Animator animator: currentAnimation.getChildAnimations()) {
            if (((ObjectAnimator) animator).getTarget().equals(swipeView.getFrontView())) {
                animator.cancel();
                Log.d("DUPA", "resetAnimation:");
                break;
            }
        }
        //currentAnimation.cancel();
    }

    private void cancelAllItemAnimations(ItemSwipeListView itemSwipeListView) {
        swipeListView.cancelItemSwipeListViewAnimations(itemSwipeListView);
    }

    private void makeSwipe(float velocityX) {
        //final float velocityX = velocityTracker.getXVelocity();
        Log.d("DUPA", "makeSwipe:" + velocityX + ":::" + swipeView.getX());
        float x;
        if (Math.abs(velocityX) < MIN_VELOCITY_TO_SWIPE) {
            // Make back animation
            x = 0.0f;
        }
        else if (Math.signum(velocityX) != Math.signum(swipeView.getFrontViewX())) {
            // swipe ends in other direction than view x
            x = 0.0f;
        }
        else {
            // Perform swipe animation
            x = swipeView.getFrontViewX() > 0.0f ? swipeView.getWidth() : -swipeView.getWidth();
        }
        startAnimation(x);
    }

    private void startAnimation(float x) {
        Log.d("DUPA", "performAnimation:" + x);
        currentAnimation = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(swipeView.getFrontView(), "translationX", x);
        objectAnimator.setDuration(1000);
        swipeListView.startItemSwipeListViewAnimations(objectAnimator);
        //currentAnimation.playTogether(objectAnimator);
        //currentAnimation.start();
    }

    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

}