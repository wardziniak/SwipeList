package com.wardziniak.swipelist.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wardziniak.swipelist.R;
import com.wardziniak.swipelist.AbsSwipeLayout;
import com.wardziniak.swipelist.SwipeableView;
import com.wardziniak.swipelist.swipe.ItemSwipeListView;
import com.wardziniak.swipelist.swipe.SampleAdapter;
import com.wardziniak.swipelist.swipe.SwipeListAdapter;
import com.wardziniak.swipelist.swipe.SwipeListView;


public class MainActivity extends Activity implements SwipeListView.OnItemSwipeListener {

    private SwipeableView absSwipeLayout;

    private SwipeListView swipeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*        //if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        //}*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeListView = (SwipeListView) findViewById(R.id.swipe_list);
        String[] itemArray = getResources().getStringArray(R.array.list_items);
        //swipeListView.setAdapter(new SampleAdapter(this, android.R.layout.simple_list_item_1, itemArray));
        //swipeListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemArray));

        swipeListView.setAdapter(new SwipeListAdapter(new SampleAdapter(this, android.R.layout.simple_list_item_1, itemArray)));
        swipeListView.setOnItemSwipeListener(this);

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

    @Override
    public void onLeftSwipe(SwipeListView swipeListView, ItemSwipeListView itemSwipeListView, int position, long id) {
        Log.d("DUPA", "onLeftSwipe:" + position);
        Toast.makeText(this, "onLeftSwipe:" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightSwipe(SwipeListView swipeListView, ItemSwipeListView itemSwipeListView, int position, long id) {
        Log.d("DUPA", "onRightSwipe:" + position);
        Toast.makeText(this, "onRightSwipe:" + position, Toast.LENGTH_SHORT).show();
    }
}
