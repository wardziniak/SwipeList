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

    public boolean onTouchDown(MotionEvent motionEvent) {
        motionX = (int) motionEvent.getRawX();
        motionY = (int) motionEvent.getRawY();
        Log.d("DUPA", "SwipeListViewTouchListener:onTouchDown:" + motionX + ":" + motionY);
        touchState = TOUCH_STATE_NONE;
        recycleVelocityTracker();
        initVelocityTrackerIfNotExists();
        velocityTracker.addMovement(motionEvent);
        velocityTracker.computeCurrentVelocity(1000);
        checkMotionEventLocation(motionEvent);
        // we cancel only if touch is on frontView
        if (isOnFrontView && containsViewAnimation(swipeView)) {
            // If touch view was animated than we cancel animation and set that we in X scrolling mode
            cancelAllItemAnimations(swipeView);
            touchState = TOUCH_STATE_SCROLLING_X;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        // We should return true if we handle that event

        // Use raw data, because when we check if event was dispatched to frontView, we need to compare to getLocationOnScreen
        final int x = (int) motionEvent.getRawX();
        final int y = (int) motionEvent.getRawY();
        Log.d("DUPA", "SwipeListViewTouchListener:onTouch:" + motionEvent.getAction() + ":" + touchState + "::::" + x + ":" + y);
        // Check touch action and decide, what to do
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /* Do not handle ACTION_DOWN. Event was passed through onTouchDown(...)
                    It is not handle here, because sometimes it can be invoked even if SwipeListViewTouchListener don't needed -
                    None of SwipeListView handle it. In that situation (e.x. Y Scrolling) default ListView::onTouchEvent(...) should handle it
                 */

                return false;
            case MotionEvent.ACTION_MOVE:
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X || !isOnFrontView)
                    return false;
                //Log.d("DUPA", "SwipeListViewTouchListener:onTouch:swipeView" + swipeView.getX());
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                swipeView.moveFrontView(x - motionX);
                motionX = x;//(int) motionEvent.getX();
                motionY = y;//(int) motionEvent.getY();
                return true;
            case MotionEvent.ACTION_UP:
                if (getMotion(motionEvent) != TOUCH_STATE_SCROLLING_X || !isOnFrontView)
                    return false;
                velocityTracker.addMovement(motionEvent);
                velocityTracker.computeCurrentVelocity(1000);
                final float velocityX = velocityTracker.getXVelocity();
                recycleVelocityTracker();


                makeSwipe(velocityX);
                return true;
        }
        return false;
    }

    private void checkMotionEventLocation(MotionEvent motionEvent) {
        Log.d("DUPA", "checkMotionEventLocation::" + motionX + ":" + motionY);
        motionPosition = swipeListView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        swipeView = (ItemSwipeListView) swipeListView.getChildAt(motionPosition - swipeListView.getFirstVisiblePosition());
        isOnFrontView = false;
        if (swipeView != null)
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
        return touchState;
    }

    private void cancelAllItemAnimations(ItemSwipeListView itemSwipeListView) {
        swipeListView.cancelItemSwipeListViewAnimations(itemSwipeListView);
    }

    private boolean containsViewAnimation(ItemSwipeListView itemSwipeListView) {
        return swipeListView.containsViewAnimation(itemSwipeListView);
    }

    private void makeSwipe(float velocityX) {
        //final float velocityX = velocityTracker.getXVelocity();
        Log.d("DUPA", "makeSwipe:" + velocityX + ":::" + swipeView.getX() + ":" + swipeView.getFrontViewX());
        float x;
        AnimationType animationType;
        if (Math.abs(velocityX) < MIN_VELOCITY_TO_SWIPE) {
            // Make back animation
            x = 0.0f;
            animationType = AnimationType.FRONT;
        }
        else if (Math.signum(velocityX) != Math.signum(swipeView.getFrontViewX())) {
            // swipe ends with other direction than view x
            x = 0.0f;
            animationType = AnimationType.FRONT;
        }
        else {
            // Perform swipe animation
            x = swipeView.getFrontViewX() > 0.0f ? swipeView.getWidth() : -swipeView.getWidth();
            animationType = swipeView.getFrontViewX() > 0.0f ? AnimationType.RIGHT : AnimationType.LEFT;
        }

        animationType = Math.abs(velocityX) < MIN_VELOCITY_TO_SWIPE || Math.signum(velocityX) != Math.signum(swipeView.getFrontViewX()) ?
                AnimationType.FRONT : swipeView.getAnimationTypeAccordingToX();
        swipeListView.startItemSwipeListViewAnimations(swipeView, animationType, motionPosition);

        //startAnimation(x);
    }

    private void startAnimation(float x) {
        Log.d("DUPA", "performAnimation:" + x);
        swipeListView.startItemSwipeListViewAnimations(swipeView, x, motionPosition);
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