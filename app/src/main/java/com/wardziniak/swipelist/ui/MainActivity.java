package com.wardziniak.swipelist.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wardziniak.swipelist.R;
import com.wardziniak.swipelist.AbsSwipeLayout;
import com.wardziniak.swipelist.SwipeableView;


public class MainActivity extends Activity {

    private SwipeableView absSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        absSwipeLayout = (SwipeableView) findViewById(R.id.swipeLayout);
        absSwipeLayout.setOnSwipeListener(new AbsSwipeLayout.OnSwipeListener() {
            @Override
            public void onLeftSwipe() {
                Log.d("DUPA", "onLeftSwipe");
            }

            @Override
            public void onRightSwipe() {
                Log.d("DUPA", "onRightSwipe");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
