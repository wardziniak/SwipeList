package com.wardziniak.swipelist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by wardziniak on 12/2/14.
 */
public class SwipeListAdapter extends BaseAdapter {

    private Adapter adapter;

    private ListView lv;

    public SwipeListAdapter(Adapter adapter) {
        this.adapter = adapter;
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
    public View getView(int position, View convertedView, ViewGroup parent) {
        SwipeListItemView swipeListItemView = null;
        try {
            swipeListItemView = (SwipeListItemView) adapter.getView(position, convertedView, parent);
            swipeListItemView.setPosition(position);
        }
        catch (ClassCastException cce) {
            throw new IllegalArgumentException("Wrong View type:" + cce.getMessage());
        }
        return swipeListItemView;
    }
}
