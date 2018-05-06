package com.bowoon.android.android_http_spi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bowoon.android.android_http_spi.adapter.RecyclerAdapter;
import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.model.PersonModel;
import com.bowoon.android.android_http_spi.volley.VolleyManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private OAuthLoginButton mOAuthLoginButton;
    private static OAuthLogin mOAuthLoginInstance;
    private static String token;
    private static Context context;
    private static ByteArrayOutputStream outputStream;
    private static Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        context = this;
        resources = getResources();

        String OAUTH_CLIENT_ID = "u6os9btMkZnp2DorvWa9";
        String OAUTH_CLIENT_SECRET = "d6UAm3Fp_i";
        String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(
                getApplicationContext(),
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
//        mOAuthLoginInstance.startOauthLoginActivity(this, mOAuthLoginHandler);

//        linearLayoutManager = new LinearLayoutManager(this);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(linearLayoutManager);

        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());

//        HttpServiceProvider.getRetrofitInstance().naverBlogPost(new HttpResultCallback());
//        HttpServiceProvider.getVolleyInstance().naverBlogPost(new HttpResultCallback());
//        HttpServiceProvider.getOkHttpInstance().RequestUser(new HttpResultCallback());
//        APIExamBlogPostMultipart apiExamBlogPostMultipart = new APIExamBlogPostMultipart();
//        apiExamBlogPostMultipart.sendPost();
    }

    private class HttpResultCallback implements HttpCallback {
        @Override
        public void onSuccess(Object result) {
            if (result instanceof PersonModel) {
                PersonModel p = (PersonModel) result;
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), p.getItems()));
            }
        }

        @Override
        public void onFail() {

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
