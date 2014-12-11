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
 * Addaper wrapper class. It gets ArrayAdapter class in constructor.
 * Calls of overridden method are passed to wrapped class
 * SwipeListAdapter:getView(...) method calls wrapped adapter getView(...) method
 *  and then check if proper view was returned and prepare ItemSwipeListView view
 */
public class SwipeListAdapter extends BaseAdapter {

    private List<AnimationType> viewsStates;
    private ArrayAdapter adapter;
    private SwipeListView swipeList;

    public SwipeListAdapter(ArrayAdapter adapter) {
        super();
        this.adapter = adapter;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = adapter.getView(position, convertView, parent);
        if (!(view instanceof ItemSwipeListView)) {
            throw new IllegalArgumentException("Incorrect type of View inflate by adapter. Is " + view.getClass() + " instead of ItemSwipeListView.");
        }
        if (viewsStates.size() <= position) {
            viewsStates.add(AnimationType.FRONT);
        }
        ItemSwipeListView itemSwipeListView = (ItemSwipeListView) view;
        itemSwipeListView.checkView();
        swipeList.cancelItemSwipeListViewAnimations(itemSwipeListView);
        swipeList.setItemSwipeListViewAttributes(itemSwipeListView);
        itemSwipeListView.prapreView(viewsStates.get(position));
        return view;
    }

    public void setViewState(int position, AnimationType animationType) {
        viewsStates.set(position, animationType);
    }
}
