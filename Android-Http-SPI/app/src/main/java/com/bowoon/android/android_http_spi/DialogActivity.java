package com.bowoon.android.android_http_spi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.volley.VolleyManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        ButterKnife.bind(this);
        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());
    }

    @OnClick({R.id.upload_naver_blog, R.id.upload_google_drive, R.id.upload_twitter_post, R.id.share_cancel})
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
            case R.id.share_cancel:
                finish();
                break;
        }
        finish();
    }
}
