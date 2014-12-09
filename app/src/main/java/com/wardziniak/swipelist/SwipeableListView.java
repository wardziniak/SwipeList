package com.wardziniak.swipelist;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by wardziniak on 12/4/14.
 */
public class SwipeableListView extends ListView {

    private static final int SWIPEABLE_LEFT = 1;
    private static final int SWIPEABLE_RIGHT = 2;
    private static final int SWIPEABLE_NONE = 0;

    private boolean isLeftSwipeEnable = false;
    private boolean isRightSwipeEnable = false;
    private boolean restartOnFinish = false;

    private float swipeLeftMargin = 0.0f;
    private float swipeRightMargin = 0.0f;

    private OnRightSwipeListener onRightSwipeListener;
    private OnLeftSwipeListener onLeftSwipeListener;

    public SwipeableListView(Context context) {
        super(context);
    }

    public SwipeableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public SwipeableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
    }

    public void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
/*            final TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeableListView);
            this.restartOnFinish = styled.getBoolean(R.styleable.SwipeableListView_restartOnFinish1, true);
            int swipeType = styled.getInt(R.styleable.SwipeableListView_swipeType1, SWIPEABLE_NONE);
            this.isLeftSwipeEnable = (swipeType & SWIPEABLE_LEFT) != 0;
            this.isRightSwipeEnable = (swipeType & SWIPEABLE_RIGHT) != 0;
            this.swipeLeftMargin = styled.getDimension(R.styleable.SwipeableListView_swipeLeftMargin1, 0);
            this.swipeRightMargin = styled.getDimension(R.styleable.SwipeableListView_swipeRightMargin1, 0);*/
        }
    }

    public interface OnLeftSwipeListener {}

    public interface OnRightSwipeListener {}

    public interface OnSwipeListener extends OnRightSwipeListener, OnLeftSwipeListener {}

}
