package com.wardziniak.swipelist;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by wardziniak on 12/1/14.
 */
public abstract class AbsSwipeLayout extends FrameLayout implements View.OnClickListener {

    private static final float MIN_VELOOCITY_TO_SWIPE = 100.0f;
    private static final long ANIMATION_DURATION = 1000;
    private static final long ANIMATION_RESTART_DURATION = 200;
    private static final int SWIPEABLE_LEFT = 1;
    private static final int SWIPEABLE_RIGHT = 2;
    private static final int SWIPEABLE_NONE = 0;

    private boolean restartOnFinish;
    private boolean isLeftSwipable;
    private boolean isRightSwipable;

    private float swipeLeftMargin;
    private float swipeRightMargin;

    private AnimatorSet currentAnimation = new AnimatorSet();


    private View frontView;
    private View swipeRightView;
    private View swipeLeftView;

    private OnRightSwipeListener onRightSwipeListener;
    private OnLeftSwipeListener onLeftSwipeListener;

    private float frontViewX;
    private VelocityTracker velocityTracker;

    public AbsSwipeLayout(Context context) {
        super(context);
    }

    public AbsSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public AbsSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.AbsSwipeLayout);
            this.restartOnFinish = styled.getBoolean(R.styleable.AbsSwipeLayout_restartOnFinish, true);
            int swipeType = styled.getInt(R.styleable.AbsSwipeLayout_swipeType, SWIPEABLE_NONE);
            this.isLeftSwipable = (swipeType & SWIPEABLE_LEFT) != 0;
            this.isRightSwipable = (swipeType & SWIPEABLE_RIGHT) != 0;
            this.swipeLeftMargin = styled.getDimension(R.styleable.AbsSwipeLayout_swipeLeftMargin, 0);
            this.swipeRightMargin = styled.getDimension(R.styleable.AbsSwipeLayout_swipeRightMargin, 0);
            Log.d("DUPA", ":" + isLeftSwipable + ":" + isRightSwipable);
            Log.d("DUPA", ":" + swipeLeftMargin + ":" + swipeRightMargin);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        frontView = findViewById(R.id.front);
        swipeRightView = findViewById(R.id.swipeRight);
        swipeLeftView = findViewById(R.id.swipeLeft);
        frontView.setOnClickListener(this);
        swipeRightView.setOnClickListener(this);
        swipeLeftView.setOnClickListener(this);

        initAnimationListeners();

        frontView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentAnimation.cancel();
                        if (velocityTracker == null)
                            velocityTracker = VelocityTracker.obtain();
                        else
                            velocityTracker.clear();
                        frontViewX = motionEvent.getX();
                        velocityTracker.addMovement(motionEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float newXLocation = frontView.getX() + (motionEvent.getX() - frontViewX);
                        frontViewX = motionEvent.getX();
                        velocityTracker.addMovement(motionEvent);
                        velocityTracker.computeCurrentVelocity(1000);
                        if (!isRightSwipable && newXLocation > 0) {
                            frontView.setTranslationX(0);
                            break;
                        }
                        else if (!isLeftSwipable && newXLocation < 0) {
                            frontView.setTranslationX(0);
                            break;
                        }

                        frontView.setTranslationX(newXLocation);

                        if (newXLocation < 0)
                            swipeRightView.setTranslationX(newXLocation);
                        if (newXLocation > 0 && swipeRightView.getX() < 0)
                            swipeRightView.setTranslationX(0);


                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        perfromeAnimation(velocityTracker.getXVelocity());
                        velocityTracker.recycle();
                        velocityTracker = null;
                        break;
                    default:
                        return false;

                }
                return true;
            }
        });
    }

    private void perfromeAnimation(float velocity) {
        float finalXLocation;
        if (Math.abs(velocity) < MIN_VELOOCITY_TO_SWIPE) {
            finalXLocation = 0;
        }
        else {
            finalXLocation = Math.signum(frontView.getX()) != Math.signum(velocity) ? 0 :
                    frontView.getX() > 0 ? frontView.getWidth() : -frontView.getWidth();
        }
        Log.d("DUPA", "perfromeAnimation:" + finalXLocation + ":" + velocity);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frontView, "x", finalXLocation);
        long animationDuration = ANIMATION_RESTART_DURATION;
        if (finalXLocation < 0) {
            // left swipe
            objectAnimator.addListener(onLeftSwipeAnimationListener);
            animationDuration = ANIMATION_DURATION;
            finalXLocation += swipeLeftMargin;
        }
        else if (finalXLocation > 0) {
            // right swipe
            objectAnimator.addListener(onRightSwipeAnimationListener);
            animationDuration = ANIMATION_DURATION;
            finalXLocation -= swipeRightMargin;
        }
        //Log.d("DUPA", "perfromeAnimation:" + finalXLocation);
        objectAnimator.setFloatValues(finalXLocation);
        objectAnimator.setDuration(animationDuration);
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        animatorList.add(objectAnimator);
        if (finalXLocation < 0) {
            objectAnimator = ObjectAnimator.ofFloat(swipeRightView, "x", finalXLocation);
            objectAnimator.setDuration(animationDuration);
            animatorList.add(objectAnimator);
        }
        currentAnimation = new AnimatorSet();
        currentAnimation.playTogether(animatorList);
        currentAnimation.start();
    }

/*    public void setOnRightSwipeListener(OnRightSwipeListener onRightSwipeListener) {
        this.onRightSwipeListener = onRightSwipeListener;
    }

    public void setOnLeftSwipeListener(OnLeftSwipeListener onLeftSwipeListener) {
        this.onLeftSwipeListener = onLeftSwipeListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onLeftSwipeListener = onSwipeListener;
        this.onRightSwipeListener = onSwipeListener;
    }
*/

    private void resetViews() {
        frontView.setTranslationX(0);
        swipeRightView.setTranslationX(0);
        Log.d("DUPA", "resetViews");
    }

    protected SwipeAnimatorListener onLeftSwipeAnimationListener;

    protected SwipeAnimatorListener onRightSwipeAnimationListener;



/*    protected SwipeAnimatorListener onLeftSwipeAnimationListener = new SwipeAnimatorListener() {
        @Override
        public void performeSwipeAction() {
            if (onLeftSwipeListener != null)
                onLeftSwipeListener.onLeftSwipe();
        }
    };

    protected SwipeAnimatorListener onRightSwipeAnimationListener = new SwipeAnimatorListener() {
        @Override
        public void performeSwipeAction() {
            if (onRightSwipeListener != null)
                onRightSwipeListener.onRightSwipe();
        }
    };*/

    protected abstract class SwipeAnimatorListener implements Animator.AnimatorListener {

        private boolean isCanceled = false;

        public abstract void performeSwipeAction();

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.d("DUPA", "onAnimationEnd");
            if (isCanceled) {
                isCanceled = false;
                return;
            }
            performeSwipeAction();
            if (restartOnFinish)
                resetViews();
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.d("DUPA", "onAnimationCancel");
            this.isCanceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }

    @Override
    public void onClick(View view) {

    }

    protected abstract void initAnimationListeners();

    public interface OnSwipeListener extends OnLeftSwipeListener, OnRightSwipeListener { }

    public interface OnLeftSwipeListener {
        public void onLeftSwipe();
    }

    public interface OnRightSwipeListener {
        public void onRightSwipe();
    }

    public interface ItemOnLeftSwipeListener {
        public void onLeftSwipe(AbsSwipeLayout view, int position);
    }

    public interface ItemOnRightSwipeListener {
        public void onRightSwipe(AbsSwipeLayout view, int position);
    }

    public interface ItemOnSwipeListener extends ItemOnLeftSwipeListener, ItemOnRightSwipeListener {}
}
