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
import java.util.List;

/**
 * Created by wardziniak on 12/1/14.
 */
public class SwipeLayout extends FrameLayout implements View.OnClickListener {

    private static final float MIN_VELOOCITY_TO_SWIPE = 100.0f;
    private static final int SWIPEABLE_LEFT = 1;
    private static final int SWIPEABLE_RIGHT = 2;
    private static final int SWIPEABLE_NONE = 0;

    private boolean restartOnFinish;
    private boolean isLeftSwipable;
    private boolean isRightSwipable;



    private View frontView;
    private View swipeRightView;
    private View swipeLeftView;

    private OnRightSwipeListener onRightSwipeListener;
    private OnLeftSwipeListener onLeftSwipeListener;

    private float frontViewX;
    private VelocityTracker velocityTracker;

    public SwipeLayout(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
            this.restartOnFinish = styled.getBoolean(R.styleable.SwipeLayout_restartOnFinish, true);
            int swipeType = styled.getInt(R.styleable.SwipeLayout_swipeType, SWIPEABLE_NONE);
            this.isLeftSwipable = (swipeType & SWIPEABLE_LEFT) != 0;
            this.isRightSwipable = (swipeType & SWIPEABLE_RIGHT) != 0;
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


        frontView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (velocityTracker == null)
                            velocityTracker = VelocityTracker.obtain();
                        else
                            velocityTracker.clear();
                        frontViewX = motionEvent.getX();
                        velocityTracker.addMovement(motionEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float newXLocation = frontView.getX() + (motionEvent.getX() - frontViewX);
                        frontView.setTranslationX(newXLocation);

                        if (newXLocation < 0)
                            swipeRightView.setTranslationX(newXLocation);
                        if (newXLocation > 0 && swipeRightView.getX() < 0)
                            swipeRightView.setTranslationX(0);
                        frontViewX = motionEvent.getX();
                        //Log.d("DUPA", "" + frontView.getX() + ":" + frontViewX);
                        velocityTracker.addMovement(motionEvent);
                        velocityTracker.computeCurrentVelocity(1000);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //velocityTracker.addMovement(motionEvent);
                        //velocityTracker.computeCurrentVelocity(1000);
                        final float velocity = velocityTracker.getXVelocity();
                        float finalXLocation;
                        if (Math.abs(velocity) < MIN_VELOOCITY_TO_SWIPE) {
                            finalXLocation = 0;
                        }
                        else {
                            finalXLocation = Math.signum(frontView.getX()) != Math.signum(velocity) ? 0 :
                                    frontView.getX() > 0 ? frontView.getWidth() : -frontView.getWidth();
                        }
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(frontView, "x", finalXLocation);
                        objectAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                Log.d("DUPA", "onAnimationEnd");
                                if (restartOnFinish)
                                    resetViews();
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        ArrayList<Animator> animatorList = new ArrayList<Animator>();
                        animatorList.add(objectAnimator);
                        if (finalXLocation < 0) {
                            objectAnimator = ObjectAnimator.ofFloat(swipeRightView, "x", finalXLocation);
                            animatorList.add(objectAnimator);
                        }
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animatorList);
                        animatorSet.start();
                        //objectAnimator.start();
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

    public void setOnRightSwipeListener(OnRightSwipeListener onRightSwipeListener) {
        this.onRightSwipeListener = onRightSwipeListener;
    }

    public void setOnLeftSwipeListener(OnLeftSwipeListener onLeftSwipeListener) {
        this.onLeftSwipeListener = onLeftSwipeListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onLeftSwipeListener = onSwipeListener;
        this.onRightSwipeListener = onSwipeListener;
    }

    private void resetViews() {
        frontView.setTranslationX(0);
        swipeRightView.setTranslationX(0);
/*
        ObjectAnimator frontViewAnimator = ObjectAnimator.ofFloat(frontView, "x", 0);
        ObjectAnimator swipeRightViewAnimator = ObjectAnimator.ofFloat(swipeRightView, "x", 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(frontViewAnimator, swipeRightViewAnimator);
        animatorSet.start();
*/

    }

    @Override
    public void onClick(View view) {

    }
}
