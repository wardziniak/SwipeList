package com.wardziniak.swipelist.swipe;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.wardziniak.swipelist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 12/7/14.
 */
public class ItemSwipeListView extends FrameLayout {

    private View frontView;
    private View swipeRightView;
    private View swipeLeftView;

    private long animationDuration = 1000;
    private float swipeLeftMargin = 100.0f;
    private float swipeRightMargin = 100.0f;
    private boolean isLeftSwipeable;
    private boolean isRightSwipeable;
    private boolean restartOnFinish;


    public ItemSwipeListView(Context context) {
        super(context);
    }

    public ItemSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemSwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onFinishInflateInit();
    }

    private void onFinishInflateInit() {
        frontView = findViewById(R.id.frontView);
        swipeRightView = findViewById(R.id.swipeRightView);
        swipeLeftView = findViewById(R.id.swipeLeftView);
    }

    void setSwipeLeftMargin(float swipeLeftMargin) {
        this.swipeLeftMargin = swipeLeftMargin;
    }

    void setSwipeRightMargin(float swipeRightMargin) {
        this.swipeRightMargin = swipeRightMargin;
    }

    void setLeftSwipeable(boolean isLeftSwipeable) {
        this.isLeftSwipeable = isLeftSwipeable;
    }

    void setRightSwipeable(boolean isRightSwipeable) {
        this.isRightSwipeable = isRightSwipeable;
    }

    void setRestartOnFinish(boolean restartOnFinish) {
        this.restartOnFinish = restartOnFinish;
    }

    public void checkView() {
        if (isLeftSwipeable && swipeLeftView == null)
            throw new IllegalArgumentException("ItemSwipeListView doesn't have child with swipeLeftView id");
        if (isRightSwipeable && swipeRightView == null)
            throw new IllegalArgumentException("ItemSwipeListView doesn't have child with swipeRightView id");
    }

    public void moveView(float x) {
        float newLocation = frontView.getX() + x;
        if (newLocation >= 0 && isRightSwipeable) {
            newLocation = newLocation > frontView.getWidth()- swipeRightMargin ? frontView.getWidth() - swipeRightMargin : newLocation;
        }
        else if (newLocation < 0 && isLeftSwipeable) {
            newLocation = newLocation < -frontView.getWidth() + swipeLeftMargin ? -frontView.getWidth() + swipeLeftMargin : newLocation;
        }
        else
            newLocation = 0.0f;
        frontView.setTranslationX(newLocation);
        //if (newLocation <= 0) {
            if (swipeRightView != null)
                swipeRightView.setTranslationX(newLocation < 0 ? newLocation : 0);
        //}
        Log.d("DUPA", "moveFrontView:");
    }

    public AnimationType getAnimationTypeAccordingToX() {
        return frontView.getX() > 0 ? AnimationType.RIGHT : AnimationType.LEFT;
    }

    public void onAnimationFinished(SwipeListAdapter swipeListAdapter, int position, AnimationType animationType) {
        if (restartOnFinish) {
            frontView.setTranslationX(0.0f);
        }
        else {
            swipeListAdapter.setViewState(position, animationType);
        }
    }

    public List<ObjectAnimator> createSwipeAnimation(final int motionPosition, final AnimationType animationType) {
        List<ObjectAnimator> objectAnimators = new ArrayList<ObjectAnimator>();
        float finalLocation = 0.0f;
        switch (animationType) {
            case FRONT:
                finalLocation = 0.0f;
                break;
            case LEFT:
                finalLocation = -frontView.getWidth() + swipeLeftMargin;
                if (swipeRightView != null) {
                    ObjectAnimator rightViewAnimator = ObjectAnimator.ofFloat(swipeRightView, "x", finalLocation);
                    rightViewAnimator.setDuration(animationDuration);
                    objectAnimators.add(rightViewAnimator);
                }
                break;
            case RIGHT:
                finalLocation = frontView.getWidth() - swipeRightMargin;
                break;
        }
        ObjectAnimator frontAnimator = ObjectAnimator.ofFloat(frontView, "x", finalLocation);
        frontAnimator.setDuration(animationDuration);
        final SwipeListView swipeListView = (SwipeListView) getParent();
        frontAnimator.addListener(new DefaultAnimatorListener() {

            private boolean canceled = false;
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled) {
                    ((View) ((ObjectAnimator) animation).getTarget()).getParent();
                    animationType.onAnimationEnd(swipeListView, motionPosition,
                            ItemSwipeListView.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                canceled = true;
            }
        });
        objectAnimators.add(frontAnimator);
        return objectAnimators;
    }

    public void prapreView(AnimationType animationType) {
        float finalLocation = 0.0f;
        switch (animationType) {
            case FRONT:
                finalLocation = 0.0f;
                break;
            case RIGHT:
                finalLocation = frontView.getWidth() - swipeRightMargin;
                break;
            case LEFT:
                finalLocation = -frontView.getWidth() + swipeLeftMargin;
                break;
        }
        if (restartOnFinish)
            frontView.setTranslationX(0.0f);
        else
            frontView.setTranslationX(finalLocation);
    }

    public boolean isFrontViewContains(int x, int y) {
        int [] location = new int[2];
        frontView.getLocationOnScreen(location);
        Log.d("DUPA", "isFrontViewContains:::" + x + ":" + y + "::" + location[0] + ":" + location[1] + ":" + (location[0] + frontView.getWidth())
            + ":" + (location[1] + frontView.getHeight()));
        Rect rect = new Rect(location[0], location[1], location[0] + frontView.getWidth(), location[1] + frontView.getHeight());
        return rect.contains(x, y);
    }

    public float getFrontViewX() {
        return frontView.getX();
    }

    public View getFrontView() {
        return frontView;
    }

}
