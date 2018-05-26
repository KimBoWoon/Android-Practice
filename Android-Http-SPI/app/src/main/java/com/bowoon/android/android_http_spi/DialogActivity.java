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
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        fileName = getIntent().getStringExtra("image");

        ButterKnife.bind(this);
        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());
    }

    @OnClick({R.id.upload_naver_blog, R.id.upload_google_drive, R.id.upload_twitter_post, R.id.share_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_naver_blog:
                Intent naverBlog = new Intent(this, NaverBlogUpload.class);
                naverBlog.putExtra("image", fileName);
                startActivity(naverBlog);
                break;
            case R.id.upload_google_drive:
                Intent googleDrive = new Intent(this, GoogleDriveUpload.class);
                googleDrive.putExtra("image", fileName);
                startActivity(googleDrive);
                break;
            case R.id.upload_twitter_post:
                Intent twitterPost = new Intent(this, TwitterPostUpload.class);
                twitterPost.putExtra("image", fileName);
                startActivity(twitterPost);
                break;
            case R.id.share_cancel:
                finish();
                break;
        }
        finish();
    }
}
