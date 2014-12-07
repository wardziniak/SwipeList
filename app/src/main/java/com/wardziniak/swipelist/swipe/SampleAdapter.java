package com.wardziniak.swipelist.swipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wardziniak.swipelist.R;

/**
 * Created by wardziniak on 12/7/14.
 */
public class SampleAdapter extends ArrayAdapter<String> {

    public SampleAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Log.d("DUPA", "getView:convertView");
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_layout, parent, false);
        }

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        final TextView frontText = (TextView) convertView.findViewById(R.id.frontView);
        frontText.setText(getItem(position));
        return convertView;
    }
}
