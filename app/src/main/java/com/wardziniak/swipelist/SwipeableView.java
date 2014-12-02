package com.wardziniak.swipelist;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wardziniak on 12/2/14.
 */
public class SwipeableView extends AbsSwipeLayout {

    private OnRightSwipeListener onRightSwipeListener;
    private OnLeftSwipeListener onLeftSwipeListener;

    protected void initAnimationListeners() {
        onLeftSwipeAnimationListener = new SwipeAnimatorListener() {
            @Override
            public void performeSwipeAction() {
                if (onLeftSwipeListener != null)
                    onLeftSwipeListener.onLeftSwipe();
            }
        };

        onRightSwipeAnimationListener = new SwipeAnimatorListener() {
            @Override
            public void performeSwipeAction() {
                if (onRightSwipeListener != null)
                    onRightSwipeListener.onRightSwipe();
            }
        };
    }

    public SwipeableView(Context context) {
        super(context);
    }

    public SwipeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
}
