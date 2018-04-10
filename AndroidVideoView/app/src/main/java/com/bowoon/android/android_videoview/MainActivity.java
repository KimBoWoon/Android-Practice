package com.bowoon.android.android_videoview;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.logcat.log.ALog;

import java.io.File;
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

        videoList = new ArrayList<>();
        String root = Environment.getExternalStorageDirectory().toString();
        File directory = new File(root + "/Movies");
        findVideos(directory, videoList);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), videoList));
    }

    void findVideos(File dir, ArrayList<Item> list) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                findVideos(file, list);
            } else if (file.getAbsolutePath().contains(".mp4")) {
                list.add(new Item(file.getName(), file.getAbsolutePath()));
            } else if (file.getAbsolutePath().contains(".avi")) {
                list.add(new Item(file.getName(), file.getAbsolutePath()));
            } else if (file.getAbsolutePath().contains(".skm")) {
                list.add(new Item(file.getName(), file.getAbsolutePath()));
            }
        }
    }
}
