package com.wardziniak.swipelist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wardziniak on 12/6/14.
 */
public class SampleAdapter extends ArrayAdapter<String> {
    public SampleAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SampleAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SampleAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public SampleAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SampleAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public SampleAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_layout, parent, false);
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("DUPA", "view:onTouch");
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DUPA", "view:onClick");
            }
        });

        final TextView textView = (TextView) convertView.findViewById(R.id.text_id);
        textView.setText(getItem(position));

        return convertView;
    }
}
