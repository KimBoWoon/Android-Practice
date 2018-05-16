package com.bowoon.android.android_http_spi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NaverBlogUpload extends Activity {
    private OAuthLogin mOAuthLoginInstance;
    private String token;
    private byte[] imageFile;
    private File file;
    private EditText titleEdit, contentEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naver_post);

        initView();
    }

    public void initView() {
        ButterKnife.bind(this);
        titleEdit = (EditText) findViewById(R.id.naver_post_title_edit);
        contentEdit = (EditText) findViewById(R.id.naver_post_content_edit);

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(
                getApplicationContext(),
                Constant.OAUTH_CLIENT_ID,
                Constant.OAUTH_CLIENT_SECRET,
                Constant.OAUTH_CLIENT_NAME
        );

        file = new File(getIntent().getStringExtra("image"));
        imageFile = Utility.fileToByte(file);
    }

    @OnClick(R.id.naver_post_send_button)
    public void onClick() {
        if (titleEdit.getText() == null || titleEdit.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contentEdit.getText() == null || contentEdit.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        mOAuthLoginInstance.startOauthLoginActivity(NaverBlogUpload.this, mOAuthLoginHandler);
    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                try {
                    token = mOAuthLoginInstance.getAccessToken(getApplicationContext());
                    Log.i("naverToken", token);
                    HttpServiceProvider.getRetrofitInstance().getNaverBlogCategory(token, new HttpCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            if (o instanceof BlogCategory) {
                              makeDialog(token, (BlogCategory) o);
                            }
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    HttpServiceProvider.getVolleyInstance().naverBlogCategory(token, new HttpCallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//                            if (o instanceof BlogCategory) {
//                                makeDialog(token, (BlogCategory) o);
//                            }
//                        }
//
//                        @Override
//                        public void onFail() {
//
//                        }
//                    });
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
                String title = titleEdit.getText().toString().trim();
                String content = contentEdit.getText().toString().trim();
                HttpServiceProvider.getRetrofitInstance().naverBlogPost(token, title, content, clickedItem.getCategoryNo(), file, new HttpCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                });
//                HttpServiceProvider.getVolleyInstance().naverBlogPost(token, title, content, clickedItem.getCategoryNo(), imageFile, new HttpCallback() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFail() {
//                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
                finish();
            }
        });
        alertBuilder.show();
    }
}
