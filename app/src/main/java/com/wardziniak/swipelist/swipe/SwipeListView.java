package com.wardziniak.swipelist.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by wardziniak on 12/6/14.
 */
public class SwipeListView extends ListView {

    public final static int TOUCH_STATE_REST = 0;
    public final static int TOUCH_STATE_SCROLLING_X = 1;
    public final static int TOUCH_STATE_SCROLLING_Y = 2;
    public final static int UNCHOSEN_POSITION = -1;

    private int touchSlop;

    private float lastMotionX;
    private float lastMotionY;

    private int mMotionPosition = UNCHOSEN_POSITION;

    private int touchState = TOUCH_STATE_REST;

    private SwipeListViewTouchListener swipeListViewTouchListener;


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

    private void initAttrs(AttributeSet attributeSet) {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = configuration.getScaledTouchSlop();
        swipeListViewTouchListener = new SwipeListViewTouchListener(this, touchSlop);
        setOnTouchListener(swipeListViewTouchListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Check if swipe mode is enable
        calculateMoveState(ev);
        Log.d("DUPA", "onInterceptTouchEvent:" +":" + ev.getAction() + ":" + touchState);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                super.onInterceptTouchEvent(ev);
                swipeListViewTouchListener.onTouch(this, ev);
                return false;
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

    public int getTouchState() {
        return touchState;
    }

    public int getMotionPosition() {
        return mMotionPosition;
    }

    public float getLastMotionX() {
        return lastMotionX;
    }

    public void setLastMotionX(float lastMotionX) {
        this.lastMotionX = lastMotionX;
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        Log.d("DUPA", "SwipeListView:onTouchEvent:" + b + ":" + ev.getAction());
        return b;
    }*/
}