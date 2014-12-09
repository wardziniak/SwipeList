package com.wardziniak.swipelist.swipe;

/**
 * Created by wardziniak on 12/9/14.
 */
public enum AnimationType {
    LEFT, FRONT, RIGHT;

    public void onAnimationEnd(SwipeListView swipeListView, int position) {
        switch (this) {
            case FRONT:
                swipeListView.onFrontBack(position);
                break;
            case RIGHT:
                swipeListView.onRightSwipe(position);
                break;
            case LEFT:
                swipeListView.onLeftSwipe(position);
        }
    }
}
