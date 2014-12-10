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

    private static final int SWIPEABLE_LEFT = 1;
    private static final int SWIPEABLE_RIGHT = 2;
    private static final int SWIPEABLE_NONE = 0;

    public final static int TOUCH_STATE_REST = 0;
    public final static int TOUCH_STATE_SCROLLING_X = 1;
    public final static int TOUCH_STATE_SCROLLING_Y = 2;
    public final static int UNCHOSEN_POSITION = -1;

    private float swipeLeftMargin = 0.0f;
    private float swipeRightMargin = 0.0f;
    private boolean isLeftSwipable = true;
    private boolean isRightSwipable = true;
    private boolean restartOnFinish = false;


    private int touchSlop;

    private float lastMotionX;
    private float lastMotionY;

    private int mMotionPosition = UNCHOSEN_POSITION;

    private int touchState = TOUCH_STATE_REST;

    private SwipeListViewTouchListener swipeListViewTouchListener;

    private OnItemLeftSwipeListener onItemLeftSwipeListener;
    private OnItemRightSwipeListener onItemRightSwipeListener;
    private SwipeListAnimatorSet swipeListAnimatorSet;
    private SwipeListAdapter swipeListAdapter;

    ListView listView;

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
            this.restartOnFinish = styled.getBoolean(R.styleable.SwipeListView_SwipeListViewrItemRestartOnFinish, false);
            final int swipeType = styled.getInt(R.styleable.SwipeListView_SwipeListViewsItemSwipeType, SWIPEABLE_NONE);
            this.isLeftSwipable = (swipeType & SWIPEABLE_LEFT) != 0;
            this.isRightSwipable = (swipeType & SWIPEABLE_RIGHT) != 0;
            this.swipeLeftMargin = styled.getDimension(R.styleable.SwipeListView_SwipeListViewItemLeftMargin, 0.0f);
            this.swipeRightMargin = styled.getDimension(R.styleable.SwipeListView_SwipeListViewItemRightMargin, 0.0f);

        }
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        swipeListAnimatorSet = new SwipeListAnimatorSet(this);
        touchSlop = configuration.getScaledTouchSlop();
        swipeListViewTouchListener = new SwipeListViewTouchListener(this, touchSlop);
        setOnTouchListener(swipeListViewTouchListener);
    }

    public void setAdapter(SwipeListAdapter swipeListAdapter) {
        super.setAdapter(swipeListAdapter);
        swipeListAdapter.setSwipeList(this);
        this.swipeListAdapter = (SwipeListAdapter) swipeListAdapter;
    }

    void onLeftSwipe(int position, ItemSwipeListView itemSwipeListView) {
        //swipeListAdapter.setViewState(position, AnimationType.LEFT);
        //itemSwipeListView.onAnimationFinished();
        if (onItemLeftSwipeListener != null)
            onItemLeftSwipeListener.onLeftSwipe(this, position, swipeListAdapter.getItemId(position));
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.LEFT);
    }

    void onRightSwipe(int position, ItemSwipeListView itemSwipeListView) {
        //swipeListAdapter.setViewState(position, AnimationType.RIGHT);
        if (onItemRightSwipeListener != null)
            onItemRightSwipeListener.onRightSwipe(this, position, swipeListAdapter.getItemId(position));
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.RIGHT);
    }

    void onFrontBack(int position, ItemSwipeListView itemSwipeListView) {
        //swipeListAdapter.setViewState(position, AnimationType.FRONT);
        itemSwipeListView.onAnimationFinished(swipeListAdapter, position, AnimationType.FRONT);
    }

//    public void startItemSwipeListViewAnimations(List<ObjectAnimator> objectAnimators) {
//        swipeListAnimatorSet.startAnimations(objectAnimators);
//    }

//    public void startItemSwipeListViewAnimations(ObjectAnimator objectAnimator, int motionPosition) {
//        swipeListAnimatorSet.startAnimation(objectAnimator, motionPosition);
//    }

    public void startItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView, AnimationType animationType, int motionPosition) {
        List<ObjectAnimator> objectAnimators = itemSwipeListView.createSwipeAnimation(motionPosition, animationType);
        swipeListAnimatorSet.startAnimations(objectAnimators, motionPosition, animationType);
    }

    public void startItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView, float x, int motionPosition) {
        // We have to start animation and mark entry at position motionPosition as swiped i proper site
        x = x > 0 ? x - swipeRightMargin : x + swipeLeftMargin;
        AnimationType animationType = x == 0 ? AnimationType.FRONT : x > 0 ? AnimationType.RIGHT : AnimationType.LEFT;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(itemSwipeListView.getFrontView(), "x", x);
        objectAnimator.setDuration(1000);
        swipeListAnimatorSet.startAnimation(objectAnimator, motionPosition, animationType);
    }

    public void setItemSwipeListViewAttributes(ItemSwipeListView itemSwipeListView) {
        itemSwipeListView.setRightSwipeable(isRightSwipable);
        itemSwipeListView.setLeftSwipeable(isLeftSwipable);
        itemSwipeListView.setSwipeLeftMargin(swipeLeftMargin);
        itemSwipeListView.setSwipeRightMargin(swipeRightMargin);
        itemSwipeListView.setRestartOnFinish(restartOnFinish);
    }

    public void cancelItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView) {
        swipeListAnimatorSet.cancelItemSwipeListViewAnimations(itemSwipeListView);
    }

    public void finishItemSwipeListViewAnimations(ItemSwipeListView itemSwipeListView) {
        // invoke, when view disappears from window. We should move view till end
    }

    public boolean containsViewAnimation(ItemSwipeListView itemSwipeListView) {
        return swipeListAnimatorSet.containsViewAnimation(itemSwipeListView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Check if swipe mode is enable
        calculateMoveState(ev);
        Log.d("DUPA", "onInterceptTouchEvent:" +":" + ev.getAction() + ":" + touchState);
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
                //return swipeListViewTouchListener.onTouch(this, ev);
                //return false;
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
        Log.d("DUPA", "calculateMoveState:" + touchSlop + ":" + xDiff + ":" + yDiff + ":::" + x + ":" + lastMotionX + "::" + y + ":" + lastMotionY);

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            lastMotionX = x;
            lastMotionY = y;
            touchState = TOUCH_STATE_REST;
            mMotionPosition = pointToPosition(x, y);
            return;
        }

        final boolean xMoved = xDiff > touchSlop;
        final boolean yMoved = yDiff > touchSlop;
        touchState = yMoved ? TOUCH_STATE_SCROLLING_Y : xMoved ? TOUCH_STATE_SCROLLING_X : touchState;
        Log.d("DUPA", "calculateMoveState::::" + touchState);
        if (touchState != TOUCH_STATE_REST) {
            lastMotionX = x;
            lastMotionY = y;
        }
    }

    public interface OnItemLeftSwipeListener {
        public void onLeftSwipe(SwipeListView swipeListView,  int position, long id);
    }

    public interface OnItemRightSwipeListener {
        public void onRightSwipe(SwipeListView swipeListView, int position, long id);
    }

    public interface OnItemSwipeListener extends OnItemRightSwipeListener, OnItemLeftSwipeListener {}

}