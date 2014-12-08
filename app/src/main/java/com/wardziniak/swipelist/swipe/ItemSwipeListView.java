package com.wardziniak.swipelist.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.wardziniak.swipelist.R;

/**
 * Created by wardziniak on 12/7/14.
 */
public class ItemSwipeListView extends FrameLayout {

    private View frontView;
    private View swipeRightView;
    private View swipeLeftView;


    public ItemSwipeListView(Context context) {
        super(context);
    }

    public ItemSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemSwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onFinishInflateInit();
    }

    private void onFinishInflateInit() {
        frontView = findViewById(R.id.frontView);
        swipeRightView = findViewById(R.id.swipeRight);
        swipeLeftView = findViewById(R.id.swipeLeft);
    }

    public void moveFrontView(float x) {
        frontView.setTranslationX(frontView.getX() + x);
        Log.d("DUPA", "moveFrontView:");
    }

    public boolean isFrontViewContains(int x, int y) {
        int [] location = new int[2];
        frontView.getLocationOnScreen(location);
        Log.d("DUPA", "isFrontViewContains:::" + x + ":" + y + "::" + location[0] + ":" + location[1] + ":" + (location[0] + frontView.getWidth())
            + ":" + (location[1] + frontView.getHeight()));
        Rect rect = new Rect(location[0], location[1], location[0] + frontView.getWidth(), location[1] + frontView.getHeight());
        return rect.contains(x, y);
    }

    public float getFrontViewX() {
        return frontView.getX();
    }

    public View getFrontView() {
        return frontView;
    }

}