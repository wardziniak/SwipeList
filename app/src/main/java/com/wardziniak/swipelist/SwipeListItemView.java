package com.wardziniak.swipelist;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wardziniak on 12/2/14.
 */
public class SwipeListItemView extends  AbsSwipeLayout {

    private ItemOnRightSwipeListener itemOnRightSwipeListener;
    private ItemOnLeftSwipeListener itemOnLeftSwipeListener;

    private int position;

    public SwipeListItemView(Context context) {
        super(context);
    }

    public SwipeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setItemOnRightSwipeListener(ItemOnRightSwipeListener itemOnRightSwipeListener) {
        this.itemOnRightSwipeListener = itemOnRightSwipeListener;
    }

    public void setItemOnLeftSwipeListener(ItemOnLeftSwipeListener itemOnLeftSwipeListener) {
        this.itemOnLeftSwipeListener = itemOnLeftSwipeListener;
    }

    public void setItemOnSwipeListener(ItemOnSwipeListener itemOnSwipeListener) {
        this.itemOnLeftSwipeListener = itemOnSwipeListener;
        this.itemOnRightSwipeListener = itemOnSwipeListener;
    }

    @Override
    protected void initAnimationListeners() {
        onLeftSwipeAnimationListener = new SwipeAnimatorListener() {
            @Override
            public void performeSwipeAction() {
                if (itemOnLeftSwipeListener != null)
                    itemOnLeftSwipeListener.onLeftSwipe(SwipeListItemView.this, position);
            }
        };

        onRightSwipeAnimationListener = new SwipeAnimatorListener() {
            @Override
            public void performeSwipeAction() {
                if (itemOnRightSwipeListener != null)
                    itemOnRightSwipeListener.onRightSwipe(SwipeListItemView.this, position);
            }
        };
    }
}
