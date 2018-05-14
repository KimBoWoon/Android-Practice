package com.bowoon.android.android_http_spi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.model.BlogCategory;
import com.bowoon.android.android_http_spi.model.Category;
import com.bowoon.android.android_http_spi.util.Constant;
import com.bowoon.android.android_http_spi.util.Utility;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import java.io.File;

public class NaverBlogUpload extends Activity {
    private OAuthLogin mOAuthLoginInstance;
    private String token;
    private byte[] imageFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    public void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(
                getApplicationContext(),
                Constant.OAUTH_CLIENT_ID,
                Constant.OAUTH_CLIENT_SECRET,
                Constant.OAUTH_CLIENT_NAME
        );
        mOAuthLoginInstance.startOauthLoginActivity(NaverBlogUpload.this, mOAuthLoginHandler);

        File file = new File(getIntent().getStringExtra("image"));
        imageFile = Utility.fileToByte(file);
    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                try {
                    token = mOAuthLoginInstance.getAccessToken(getApplicationContext());
                    Log.i("naverToken", token);
//                    HttpServiceProvider.getRetrofitInstance().getNaverBlogCategory(token, new HttpCallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                            if (o instanceof BlogCategory) {
//                              makeDialog(token, (BlogCategory) o);
//                            }
//                        }
//
//                        @Override
//                        public void onFail() {
//                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    HttpServiceProvider.getVolleyInstance().naverBlogCategory(token, new HttpCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            if (o instanceof BlogCategory) {
                                makeDialog(token, (BlogCategory) o);
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mOAuthLoginInstance.logoutAndDeleteToken(getApplicationContext());
//                        }
//                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("success", "success");
            } else {
                Log.i("fail", "fail");
            }
        }
    };

    public void makeDialog(String token, BlogCategory category) {
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(NaverBlogUpload.this, android.R.layout.select_dialog_singlechoice);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NaverBlogUpload.this);
        alertBuilder.setTitle("카테고리를 선택하세요.");

        for (int i = 0; i < category.getResult().size(); i++) {
            if (category.getResult().get(i).getSubCategories().size() == 0) {
                adapter.add(category.getResult().get(i));
            } else {
                for (int j = 0; j < category.getResult().get(i).getSubCategories().size(); j++) {
                    adapter.add(category.getResult().get(i).getSubCategories().get(j));
                }
            }
        }

        alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Category clickedItem = adapter.getItem(id);
                HttpServiceProvider.getVolleyInstance().naverBlogPost(token, clickedItem.getCategoryNo(), imageFile, new HttpCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });
        alertBuilder.show();
    }
}
