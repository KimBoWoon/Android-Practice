package com.bowoon.android.android_http_spi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static OAuthLogin mOAuthLoginInstance;
    private static String token;
    private static Context context;
    private static ByteArrayOutputStream outputStream;

    private String OAUTH_CLIENT_ID = "u6os9btMkZnp2DorvWa9";
    private String OAUTH_CLIENT_SECRET = "d6UAm3Fp_i";
    private String OAUTH_CLIENT_NAME = "네이버 블로그에 업로드";

    private TwitterLoginButton loginButton;

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
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
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
                Intent intent2 = new Intent(this, TwitterPostUpload.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                mOAuthLoginInstance.logout(getApplicationContext());
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                token = mOAuthLoginInstance.getAccessToken(context);
                outputStream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                HttpServiceProvider.getRetrofitInstance().naverBlogPost(token, outputStream.toByteArray(), new HttpCallback() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFail() {
//                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                HttpServiceProvider.getVolleyInstance().twitterPost(mCredential, new HttpCallback() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFail() {
//                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
                HttpServiceProvider.getVolleyInstance().naverBlogPost(token, outputStream.toByteArray(), new HttpCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i("token", token + "");
                Log.i("success", "success");
            } else {
                Log.i("fail", "fail");
            }
        }
    };
}
