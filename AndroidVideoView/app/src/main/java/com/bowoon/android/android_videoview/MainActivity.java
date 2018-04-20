package com.bowoon.android.android_videoview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestReadExternalStoragePermission();

        ALog.logSetting(getApplicationContext(), false, false);
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


    private void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
