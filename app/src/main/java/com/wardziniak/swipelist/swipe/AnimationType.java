package com.wardziniak.swipelist.swipe;

/**
 * Created by wardziniak on 12/9/14.
 * AnimationType
 */
public enum AnimationType {
    LEFT, FRONT, RIGHT;

    public void onAnimationEnd(SwipeListView swipeListView, int position, ItemSwipeListView itemSwipeListView) {
        switch (this) {
            case FRONT:
                swipeListView.onFrontBack(position, itemSwipeListView);
                break;
            case RIGHT:
                swipeListView.onRightSwipe(position, itemSwipeListView);
                break;
            case LEFT:
                swipeListView.onLeftSwipe(position, itemSwipeListView);
        }
    }
}
