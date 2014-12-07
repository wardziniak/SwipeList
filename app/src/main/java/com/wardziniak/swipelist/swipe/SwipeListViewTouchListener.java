package com.wardziniak.swipelist.swipe;

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

    private VelocityTracker velocityTracker;

    public SwipeListViewTouchListener(SwipeListView swipeListView, int touchSlop) {
        this.swipeListView = swipeListView;
        this.touchSlop = touchSlop;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        //final int touchState = swipeListView.getTouchState();
        Log.d("DUPA", "SwipeListViewTouchListener:onTouch:" + motionEvent.getAction() + ":" + touchState);

        // Check touch action and decide, what to do
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.motionX = (int) motionEvent.getX();
                this.motionY = (int) motionEvent.getY();
                touchState = TOUCH_STATE_NONE;
                recycleVelocityTracker();
                initVelocityTrackerIfNotExists();
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                return false;
            case MotionEvent.ACTION_MOVE:
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X)
                    return false;
                Log.d("DUPA", "SwipeListViewTouchListener:onTouch:swipeView" + swipeView.getX());
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                swipeView.moveFrontView(motionEvent.getX() - motionX);
                motionX = (int) motionEvent.getX();
                motionY = (int) motionEvent.getY();
                return true;
            case MotionEvent.ACTION_UP:
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X)
                    return false;
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                makeSwipe();
                return true;
        }
        return false;
    }

    public int getMotion(MotionEvent motionEvent) {
        // We do not change touch type;
        Log.d("DUPA", "getMotion:" + touchState);
        if (touchState != TOUCH_STATE_NONE)
            return touchState;
        int diffX = (int) Math.abs(motionEvent.getX() - motionX);
        int diffY = (int) Math.abs(motionEvent.getY() - motionY);
        touchState = diffY > touchSlop ? TOUCH_STATE_SCROLLING_Y : diffX > touchSlop ? TOUCH_STATE_SCROLLING_X : touchState;
        if (touchState == TOUCH_STATE_SCROLLING_X) {
            motionPosition = swipeListView.pointToPosition(motionX, motionY);
            swipeView = (ItemSwipeListView) swipeListView.getChildAt(swipeListView.getMotionPosition() - swipeListView.getFirstVisiblePosition());
        }
        return touchState;
    }

    private void makeSwipe() {
        final float velocityX = velocityTracker.getXVelocity();
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
        performAnimation(x);
    }

    private void performAnimation(float x) {
        Log.d("DUPA", "performAnimation:" + x);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(swipeView.getFrontView(), "translationX", x);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
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