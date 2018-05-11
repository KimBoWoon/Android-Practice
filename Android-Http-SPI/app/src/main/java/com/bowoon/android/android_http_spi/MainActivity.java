package com.bowoon.android.android_http_spi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.model.BlogCategory;
import com.bowoon.android.android_http_spi.model.Category;
import com.bowoon.android.android_http_spi.volley.VolleyManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.twitter.sdk.android.core.Twitter;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static OAuthLogin mOAuthLoginInstance;
    private static String token;
    private static Context context;

    private String OAUTH_CLIENT_ID = "u6os9btMkZnp2DorvWa9";
    private String OAUTH_CLIENT_SECRET = "d6UAm3Fp_i";
    private String OAUTH_CLIENT_NAME = "네이버 블로그에 업로드";

    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        requestReadExternalStoragePermission();

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
                    Log.i("naverToken", token);
//                    HttpServiceProvider.getRetrofitInstance().getNaverBlogCategory(token, new HttpCallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                            if (o instanceof BlogCategory) {
//                                final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
//                                        context,
//                                        android.R.layout.select_dialog_singlechoice);
//                                BlogCategory category = (BlogCategory) o;
//
//                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
//                                        context);
//                                alertBuilder.setIcon(R.mipmap.ic_launcher);
//                                alertBuilder.setTitle("항목중에 하나를 선택하세요.");
//
//                                for (int i = 0; i < category.getResult().size(); i++) {
//                                    adapter.add(category.getResult().get(i));
//                                    if (category.getResult().get(i).getSubCategories().size() != 0) {
//                                        for (int j = 0; j < category.getResult().get(i).getSubCategories().size(); j++) {
//                                            adapter.add(category.getResult().get(i).getSubCategories().get(j));
//                                        }
//                                    }
//                                }
//
//                                alertBuilder.setAdapter(adapter,
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int id) {
//
//                                                // AlertDialog 안에 있는 AlertDialog
//                                                Category clickedItem = adapter.getItem(id);
//                                                File imageFile = new File("/storage/sdcard0/Download/android-logcat.gif");
//                                                HttpServiceProvider.getRetrofitInstance().naverBlogPost(token, clickedItem.getCategoryNo(), imageFile, new HttpCallback() {
//                                                    @Override
//                                                    public void onSuccess(Object o) {
//                                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                    @Override
//                                                    public void onFail() {
//                                                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                            }
//                                        });
//                                alertBuilder.show();
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
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            if (o instanceof BlogCategory) {
                                final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(
                                        context,
                                        android.R.layout.select_dialog_singlechoice);
                                BlogCategory category = (BlogCategory) o;

                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                                        context);
                                alertBuilder.setIcon(R.mipmap.ic_launcher);
                                alertBuilder.setTitle("항목중에 하나를 선택하세요.");

                                for (int i = 0; i < category.getResult().size(); i++) {
                                    adapter.add(category.getResult().get(i));
                                    if (category.getResult().get(i).getSubCategories().size() != 0) {
                                        for (int j = 0; j < category.getResult().get(i).getSubCategories().size(); j++) {
                                            adapter.add(category.getResult().get(i).getSubCategories().get(j));
                                        }
                                    }
                                }

                                alertBuilder.setAdapter(adapter,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                                // AlertDialog 안에 있는 AlertDialog
                                                Category clickedItem = adapter.getItem(id);
                                                File imageFile = new File("/storage/sdcard0/Download/android-logcat.gif");
                                                try {
                                                    HttpServiceProvider.getVolleyInstance().naverBlogPost(token, clickedItem.getCategoryNo(), com.google.android.gms.common.util.IOUtils.toByteArray(imageFile), new HttpCallback() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFail() {
                                                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                alertBuilder.show();
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });
//                    File imageFile = new File("/storage/sdcard0/Download/android-logcat.gif");
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
//                    HttpServiceProvider.getVolleyInstance().naverBlogPost(token, com.google.android.gms.common.util.IOUtils.toByteArray(imageFile), new HttpCallback() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("success", "success");
            } else {
                Log.i("fail", "fail");
            }
        }
    };

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
