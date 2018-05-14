package com.bowoon.android.android_http_spi.retrofit;

import android.util.Log;

import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.model.BlogCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UseRetrofit {
    private final String NAVER_BASE_URL = "https://openapi.naver.com/";
    private final String GOOGLE_BASE_URL = "https://www.googleapis.com/";
    private final String TWITTER_BASE_URL = "https://api.twitter.com/";

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    public void getNaverBlogCategory(String token, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(NAVER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        APIInterface service = client.create(APIInterface.class);

        String header = "Bearer " + token;

        Call<Object> call = service.naverBlogCategory(header);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                BlogCategory category = gson.fromJson(jsonObject.get("message"), BlogCategory.class);
                callback.onSuccess(category);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("getNaverBlogCategory", call.toString());
                callback.onFail();
            }
        });
    }

    public void naverBlogPost(String token, int categoryNo, File file, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(NAVER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        APIInterface service = client.create(APIInterface.class);

        String header = "Bearer " + token;
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "네이버 multi-part 이미지 첨부 테스트");
        RequestBody contents = RequestBody.create(MediaType.parse("text/plain"), "<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
        RequestBody image = RequestBody.create(MediaType.parse("image/gif"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), image);

        Call<JSONObject> call = service.naverBlogSendPost(header, title, contents, categoryNo, part);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("naverBlogPost", String.valueOf(response.raw()));
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("naverBlogPost", call.toString());
                callback.onFail();
            }
        });
    }

    public void googleDriveUpload(String token, byte[] file, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(GOOGLE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        APIInterface service = client.create(APIInterface.class);

        String header = "Bearer " + token;
        RequestBody image = RequestBody.create(MediaType.parse("image/gif"), file);

        Call<JSONObject> call = service.googleDriveUpload(header, image);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("googleDriveUpload", String.valueOf(response.raw()));
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("googleDriveUpload", t.getMessage());
                callback.onFail();
            }
        });
    }

    public void twitterPostUpload(final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(TWITTER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        APIInterface service = client.create(APIInterface.class);

        Call<JSONObject> call = service.twitterTweet("Test", null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("twitterPostUpload", String.valueOf(response.raw()));
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("twitterPostUpload", t.getMessage());
                callback.onFail();
            }
        });
    }
}
