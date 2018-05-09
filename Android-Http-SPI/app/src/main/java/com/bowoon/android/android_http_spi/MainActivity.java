package com.bowoon.android.android_http_spi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.volley.VolleyManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static OAuthLogin mOAuthLoginInstance;
    private static String token;
    private static Context context;

    private String OAUTH_CLIENT_ID = "u6os9btMkZnp2DorvWa9";
    private String OAUTH_CLIENT_SECRET = "d6UAm3Fp_i";
    private String OAUTH_CLIENT_NAME = "네이버 블로그에 업로드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    public void initData() {
        context = MainActivity.this;

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(
                getApplicationContext(),
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
        );
    }

    private void initView() {
        ButterKnife.bind(this);
        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());
    }

    @OnClick({R.id.upload_naver_blog, R.id.upload_google_drive, R.id.upload_twitter_post, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_naver_blog:
                mOAuthLoginInstance.startOauthLoginActivity(MainActivity.this, mOAuthLoginHandler);
                break;
            case R.id.upload_google_drive:
                Intent intent1 = new Intent(this, GoogleDriveUpload.class);
                startActivity(intent1);
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
                Intent intent2 = new Intent(this, TwitterPostUpload.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mOAuthLoginInstance.logoutAndDeleteToken(getApplicationContext());
                    }
                }).start();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                try {
                    token = mOAuthLoginInstance.getAccessToken(context);
                    File imageFile = new File("/storage/sdcard0/Download/android-logcat.gif");
//                    HttpServiceProvider.getRetrofitInstance().naverBlogPost(token, imageFile, new HttpCallback() {
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFail() {
//                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    HttpServiceProvider.getVolleyInstance().naverBlogPost(token, com.google.android.gms.common.util.IOUtils.toByteArray(imageFile), new HttpCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("success", "success");
            } else {
                Log.i("fail", "fail");
            }
        }
    };
}
