package com.bowoon.android.android_videoview;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.logcat.log.ALog;
import com.bowoon.android.android_videoview.adapter.RecyclerAdapter;
import com.bowoon.android.android_videoview.vo.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> videoList;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ALog.logSetting(getApplicationContext(), true, false);
        ALog.setDebug(true);

        initView();
    }

    private void initView() {
        videoList = new ArrayList<>();
        videoList = fetchAllVideos();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), videoList));
    }

    private ArrayList<Item> fetchAllVideos() {
        String[] projection = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED
        };

        Cursor videoCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "date_added DESC");

        ArrayList<Item> result = new ArrayList<>();
        int dataColumnIndex = videoCursor.getColumnIndex(projection[0]);
        int nameColumnIndex = videoCursor.getColumnIndex(projection[1]);

        if (videoCursor.moveToFirst()) {
            do {
                result.add(new Item(videoCursor.getString(nameColumnIndex), videoCursor.getString(dataColumnIndex)));
            } while (videoCursor.moveToNext());
        }
        videoCursor.close();
        return result;
    }
}
