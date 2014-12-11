package com.wardziniak.swipelist.swipe;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.wardziniak.swipelist.R;

import java.util.List;

/**
 * Created by wardziniak on 12/6/14.
 */
public class SwipeListView extends ListView {

    private static final String TAG = "SwipeList:SwipeListView";

    private static final int SWIPEABLE_LEFT = 1;
    private static final int SWIPEABLE_RIGHT = 2;
    private static final int SWIPEABLE_NONE = 0;
    private static final int DEFAULT_SWIPE_TYPE = SWIPEABLE_NONE;
    private static final float DEFAULT_MIN_VELOCITY_TO_SWIPE = 100.0f;
    private static final int DEFAULT_ANIMATION_DURATION = 1000;
    private static final float DEFAULT_LEFT_MARGIN = 0.0f;
    private static final float DEFAULT_RIGHT_MARGIN = 0.0f;
    private static final boolean DEFAULT_RESTART_ON_FINISH = false;

    public final static int TOUCH_STATE_NONE_SCROLLING = 0;
    public final static int TOUCH_STATE_SCROLLING_X = 1;
    public final static int TOUCH_STATE_SCROLLING_Y = 2;

    private float swipeLeftMargin;
    private float swipeRightMargin;
    private boolean isLeftSwipable;
    private boolean isRightSwipable;
    private boolean restartOnFinish;
    private long animationDuration;
    private float minVelocityToSwipe;


    private int touchSlop;

    private float lastMotionX;
    private float lastMotionY;

    private int touchState = SWIPEABLE_NONE;

    private SwipeListViewTouchListener swipeListViewTouchListener;

    private OnItemLeftSwipeListener onItemLeftSwipeListener;
    private OnItemRightSwipeListener onItemRightSwipeListener;
    private SwipeListAnimatorSet swipeListAnimatorSet;
    private SwipeListAdapter swipeListAdapter;

    public SwipeListView(Context context) {
        super(context);
        initAttrs(null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
    }

    public void setOnItemLeftSwipeListener(OnItemLeftSwipeListener onItemLeftSwipeListener) {
        this.onItemLeftSwipeListener = onItemLeftSwipeListener;
    }

    public void setOnItemRightSwipeListener(OnItemRightSwipeListener onItemRightSwipeListener) {
        this.onItemRightSwipeListener = onItemRightSwipeListener;
    }

    public void setOnItemSwipeListener(OnItemSwipeListener onItemSwipeListener) {
        this.onItemLeftSwipeListener = onItemSwipeListener;
        this.onItemRightSwipeListener = onItemSwipeListener;
    }


    private void initAttrs(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attributeSet, R.styleable.SwipeListView);
            this.restartOnFinish = styled.getBoolean(R.styleable.SwipeListView_SwipeListViewItemRestartOnFinish, DEFAULT_RESTART_ON_FINISH);
            final int swipeType = styled.getInt(R.styleable.SwipeListView_SwipeListViewItemSwipeType, DEFAULT_SWIPE_TYPE);
            this.isLeftSwipable = (swipeType & SWIPEABLE_LEFT) != 0;
            this.isRightSwipable = (swipeType & SWIPEABLE_RIGHT) != 0;
            this.swipeLeftMargin = styled.getDimension(R.styleable.SwipeListView_SwipeListViewItemLeftMargin, DEFAULT_LEFT_MARGIN);
            this.swipeRightMargin = styled.getDimension(R.styleable.SwipeListView_SwipeListViewItemRightMargin, DEFAULT_RIGHT_MARGIN);
            this.animationDuration = styled.getInt(R.styleable.SwipeListView_SwipeListViewAnimationDuration, DEFAULT_ANIMATION_DURATION);
            this.minVelocityToSwipe = styled.getFloat(R.styleable.SwipeListView_SwipeListViewMinVelocityToSwipe, DEFAULT_MIN_VELOCITY_TO_SWIPE);

        }
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        swipeListAnimatorSet = new SwipeListAnimatorSet(this);
        touchSlop = configuration.getScaledTouchSlop();
        swipeListViewTouchListener = new SwipeListViewTouchListener(this, touchSlop, minVelocityToSwipe);
        setOnTouchListener(swipeListViewTouchListener);
    }

    public void setAdapter(SwipeListAdapter swipeListAdapter) {
        super.setAdapter(swipeListAdapter);
        swipeListAdapter.setSwipeList(this);
        this.swipeListAdapter = swipeListAdapter;
    }

    void onLeftSwipe(int position, ItemSwipeListView itemSwipeListView) {
        Log.d(TAG, "onLeftSwipe:" + position);
        if (onItemLeftSwipeListener != null)
            onItemLeftSwipeListener.onLeftSwipe(this, itemSwipeListView, position, swipeListAdapter.getItemId(position));
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.LEFT);
    }

    void onRightSwipe(int position, ItemSwipeListView itemSwipeListView) {
        Log.d(TAG, "onRightSwipe:" + position);
        if (onItemRightSwipeListener != null)
            onItemRightSwipeListener.onRightSwipe(this, itemSwipeListView, position, swipeListAdapter.getItemId(position));
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.RIGHT);
    }

    void onFrontBack(int position, ItemSwipeListView itemSwipeListView) {
        Log.d(TAG, "onFrontBack:" + position);
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.FRONT);
    }

    public void startItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView, AnimationType animationType, int motionPosition) {
        List<ObjectAnimator> objectAnimators = itemSwipeListView.createSwipeAnimation(motionPosition, animationType);
        swipeListAnimatorSet.startAnimations(objectAnimators, motionPosition, animationType);
    }

    public void setItemSwipeListViewAttributes(ItemSwipeListView itemSwipeListView) {
        itemSwipeListView.setRightSwipeable(isRightSwipable);
        itemSwipeListView.setLeftSwipeable(isLeftSwipable);
        itemSwipeListView.setSwipeLeftMargin(swipeLeftMargin);
        itemSwipeListView.setSwipeRightMargin(swipeRightMargin);
        itemSwipeListView.setRestartOnFinish(restartOnFinish);
        itemSwipeListView.setAnimationDuration(animationDuration);
    }

    public void cancelItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView) {
        swipeListAnimatorSet.cancelItemSwipeListViewAnimations(itemSwipeListView);
    }

    public boolean containsViewAnimation(ItemSwipeListView itemSwipeListView) {
        return swipeListAnimatorSet.containsViewAnimation(itemSwipeListView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        calculateMoveState(ev);
        //Log.d(TAG, "onInterceptTouchEvent:" +":" + ev.getAction() + ":" + touchState);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /* ACTION_DOWN is passed directly to swipeListViewTouchListener.
                    It cannot be handle by SwipeListViewTouchListener::onTouch(..)
                    because it is not possible to check why it was passed to it.
                    More detail SwipeListViewTouchListener::onTouch(..)
                */
                if (swipeListViewTouchListener.onTouchDown(ev))
                    return true;
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                if (touchState == TOUCH_STATE_SCROLLING_Y)
                    return true;
                if (touchState == TOUCH_STATE_SCROLLING_X)
                    return true;
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                return touchState == TOUCH_STATE_SCROLLING_Y;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }

    public void calculateMoveState(MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        final int y = (int) motionEvent.getY();
        final int xDiff = (int) Math.abs(x - lastMotionX);
        final int yDiff = (int) Math.abs(y - lastMotionY);
        //Log.d(TAG, "calculateMoveState:" + touchSlop + ":" + xDiff + ":" + yDiff + ":::" + x + ":" + lastMotionX + "::" + y + ":" + lastMotionY);

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            lastMotionX = x;
            lastMotionY = y;
            touchState = TOUCH_STATE_NONE_SCROLLING;
            return;
        }

        final boolean xMoved = xDiff > touchSlop;
        final boolean yMoved = yDiff > touchSlop;
        touchState = yMoved ? TOUCH_STATE_SCROLLING_Y : xMoved ? TOUCH_STATE_SCROLLING_X : touchState;
        //Log.d(TAG, "calculateMoveState::::" + touchState);
        if (touchState != TOUCH_STATE_NONE_SCROLLING) {
            lastMotionX = x;
            lastMotionY = y;
        }
    }

    public interface OnItemLeftSwipeListener {
        public void onLeftSwipe(SwipeListView swipeListView, ItemSwipeListView itemSwipeListView, int position, long id);
    }

    public interface OnItemRightSwipeListener {
        public void onRightSwipe(SwipeListView swipeListView, ItemSwipeListView itemSwipeListView, int position, long id);
    }

    public interface OnItemSwipeListener extends OnItemRightSwipeListener, OnItemLeftSwipeListener {}

}