package com.wardziniak.swipelist.swipe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.wardziniak.swipelist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wardziniak on 12/7/14.
 */
public class SwipeListAdapter extends BaseAdapter {

    private List<AnimationType> viewsStates;
    private ArrayAdapter adapter;
    private Context context;
    private SwipeListView swipeList;

    public SwipeListAdapter(Context context, ArrayAdapter adapter) {
        super();
        this.adapter = adapter;
        this.context = context;
        this.viewsStates = new ArrayList<AnimationType>();
    }

    void setSwipeList(SwipeListView swipeList) {
        this.swipeList = swipeList;
    }

    @Override
    public int getCount() {
        return adapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    private void resetView(ItemSwipeListView itemSwipeListView) {
        View frontView = itemSwipeListView.findViewById(R.id.frontView);
        frontView.setTranslationX(0);
    }

    private void prapreView(ItemSwipeListView itemSwipeListView, AnimationType animationType) {
        View frontView = itemSwipeListView.findViewById(R.id.frontView);
        switch (animationType) {
            case FRONT:
                frontView.setTranslationX(0);
                break;
            default:
                frontView.setTranslationX(frontView.getWidth());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = adapter.getView(position, convertView, parent);
        if (viewsStates.size() <= position) {
            viewsStates.add(AnimationType.FRONT);
        }
        if (!(view instanceof ItemSwipeListView)) {
            throw new IllegalArgumentException("Incorrect type of View inflate by adapter. Is " + view.getClass() + " instead of ItemSwipeListView.");
        }
        swipeList.cancelItemSwipeListViewAnimations((ItemSwipeListView) view);
        swipeList.setItemSwipeListViewAttributes((ItemSwipeListView) view);
        ((ItemSwipeListView) view).prapreView(viewsStates.get(position));
        //prapreView((ItemSwipeListView) view, viewsStates.get(position));
        return view;
    }

    public void setViewState(int position, AnimationType animationType) {
        viewsStates.set(position, animationType);
    }
}
