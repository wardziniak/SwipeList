package com.wardziniak.swipelist.swipe;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.wardziniak.swipelist.R;
import com.wardziniak.swipelist.swipe.animation.AplhaAnimationSwipeableView;
import com.wardziniak.swipelist.swipe.animation.SwipeableViewAnimation;
import com.wardziniak.swipelist.swipe.animation.TranslateAnimationSwipeableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 12/7/14.
 */
public class ItemSwipeListView extends FrameLayout {

    private static String TAG = "SwipeList:ItemSwipeListView";

    private View frontView;
    private View swipeRightView;
    private View swipeLeftView;

    private long animationDuration;
    private float swipeLeftMargin;
    private float swipeRightMargin;
    private boolean isLeftSwipeable;
    private boolean isRightSwipeable;
    private boolean restartOnFinish;

    private boolean reseted = true;

    private SwipeableViewAnimation animation = new AplhaAnimationSwipeableView();


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

    void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public boolean isReseted() {
        return reseted;
    }

    public void checkView() {
        if (isLeftSwipeable && swipeLeftView == null)
            throw new IllegalArgumentException("ItemSwipeListView doesn't have child with swipeLeftView id");
        if (isRightSwipeable && swipeRightView == null)
            throw new IllegalArgumentException("ItemSwipeListView doesn't have child with swipeRightView id");
        if (frontView == null)
            throw new IllegalArgumentException("ItemSwipeListView doesn't have child with frontView id");
    }

    public float getCurrentAlpha() {
        return frontView.getAlpha();
    }

    public float getCurrentTranslationX() {
        return frontView.getTranslationX();
    }

    public void startMonitoring(float motionX) {
        animation.onStartMonitoring(this, motionX);
    }

    public void onSwipeView(float x) {
        float fraction = getFractionOfSwipe(x);
        animation.move(this, fraction);
    }

    public void changeTranslation(float currentTranslation) {
        //Log.d("DUPA", "changeTranslation:" + fraction + ":::" + frontView.getX());
        //float newLocation = frontView.getX() + frontView.getWidth() * fraction;
        if (currentTranslation >= 0 && isRightSwipeable) {
            currentTranslation = currentTranslation > frontView.getWidth() ? frontView.getWidth()  : currentTranslation;
        }
        else if (currentTranslation < 0 && isLeftSwipeable) {
            currentTranslation = currentTranslation < -frontView.getWidth() ? -frontView.getWidth() : currentTranslation;
        }
        else
            currentTranslation = 0.0f;
        setComponentsLocation(currentTranslation);
    }

    public void setSwipeAlpha(float newAlpha) {
        //float newAlpha = frontView.getAlpha() + farction;
        newAlpha -= 1;
        Log.d("DUPA", "setSwipeAlpha:" + newAlpha);
        if (newAlpha >= 0 && isRightSwipeable) {
            newAlpha = Math.min(newAlpha, 1.0f);
        }
        else if (newAlpha < 0 && isLeftSwipeable) {
            newAlpha = Math.min(-1*newAlpha, 1.0f);
        }
        else
            newAlpha = 0.0f;
        frontView.setAlpha(newAlpha);
    }


    private void setComponentsLocation(float newLocation) {
        frontView.setTranslationX(newLocation);
        if (swipeRightView != null)
            swipeRightView.setTranslationX(newLocation < 0 ? newLocation : 0.0f);
    }

    public float getFractionOfSwipe(float x) {
        return (x/frontView.getWidth());
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
        setComponentsLocation(newLocation);
        Log.d(TAG, "moveFrontView:");
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
                    objectAnimators.add(createViewObjectAnitmator(swipeRightView, finalLocation, animationDuration));
                }
                break;
            case RIGHT:
                finalLocation = frontView.getWidth() - swipeRightMargin;
                break;
        }
        ObjectAnimator frontViewAnimator = createViewObjectAnitmator(frontView, finalLocation, animationDuration);
        final SwipeListView swipeListView = (SwipeListView) getParent();
        frontViewAnimator.addListener(new DefaultAnimatorListener() {

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

        objectAnimators.add(frontViewAnimator);
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
        if (restartOnFinish) {
            finalLocation = 0.0f;
        }
        reseted = true;
        setComponentsLocation(finalLocation);
    }

    public boolean isFrontViewContains(int x, int y) {
        int [] location = new int[2];
        frontView.getLocationOnScreen(location);
        Log.d(TAG, "isFrontViewContains:::" + x + ":" + y + "::" + location[0] + ":" + location[1] + ":" + (location[0] + frontView.getWidth())
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

    private static ObjectAnimator createViewObjectAnitmator(View view,  float x, long animationDuration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "x", x);
        objectAnimator.setDuration(animationDuration);
        return  objectAnimator;
    }

}
