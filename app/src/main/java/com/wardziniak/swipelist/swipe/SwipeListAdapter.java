package com.wardziniak.swipelist.swipe;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wardziniak.swipelist.R;
import com.wardziniak.swipelist.SampleAdapter;

import java.util.List;

/**
 * Created by wardziniak on 12/7/14.
 */
public class SwipeListAdapter extends BaseAdapter {

    private Adapter adapter;
    private Context context;

    public SwipeListAdapter(Context context, Adapter adapter) {
        super();
        this.adapter = adapter;
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = adapter.getView(position, convertView, parent);
        if (!(view instanceof ItemSwipeListView)) {
            throw new IllegalArgumentException("Incorrect type of View inflate by adapter. Is " + view.getClass() + " instead of ItemSwipeListView.");
        }
        resetView((ItemSwipeListView) view);
        return view;
    }
}
