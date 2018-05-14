package com.bowoon.android.android_http_spi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.PermissionRequest;

import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.util.Utility;
import com.bowoon.android.android_http_spi.volley.VolleyManager;
import com.twitter.sdk.android.core.Twitter;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        requestReadExternalStoragePermission();

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());
    }

    @OnClick({R.id.upload_naver_blog, R.id.upload_google_drive, R.id.upload_twitter_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_naver_blog:
                Intent naverBlog = new Intent(this, NaverBlogUpload.class);
                naverBlog.putExtra("image", "/storage/sdcard0/Download/android-logcat.gif");
                startActivity(naverBlog);
                break;
            case R.id.upload_google_drive:
                Intent googleDrive = new Intent(this, GoogleDriveUpload.class);
                googleDrive.putExtra("image", "/storage/sdcard0/Download/android-logcat.gif");
                startActivity(googleDrive);
                break;
            case R.id.upload_twitter_post:
//                HttpServiceProvider.getRetrofitInstance().twitterPostUpload(new HttpCallback() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFail() {
//                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
                Intent twitterPost = new Intent(this, TwitterPostUpload.class);
                twitterPost.putExtra("image", "/storage/sdcard0/Download/android-logcat.gif");
                startActivity(twitterPost);
                break;
        }
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
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE: {
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
