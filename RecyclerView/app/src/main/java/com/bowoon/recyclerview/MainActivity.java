package com.bowoon.recyclerview;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Item> items = new ArrayList<>();
    private Item[] item = new Item[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        setContentView(R.layout.activity_main);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        item[0] = new Item(R.mipmap.ic_launcher, "Basketball");
        item[1] = new Item(R.mipmap.ic_launcher, "Soccer");
        item[2] = new Item(R.mipmap.ic_launcher, "Baseball");
        item[3] = new Item(R.mipmap.ic_launcher, "Ping-Pong");
        item[4] = new Item(R.mipmap.ic_launcher, "Badminton");
        item[5] = new Item(R.mipmap.ic_launcher, "Tennis");

        for (int i = 0; i < 6; i++)
            items.add(item[i]);

        recyclerView.setAdapter(new com.bowoon.recyclerview.RecyclerAdapter(getApplicationContext(), items));
    }
}
