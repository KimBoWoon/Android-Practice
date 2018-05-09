package com.bowoon.android.android_http_spi.retrofit;

import android.util.Log;

import com.bowoon.android.android_http_spi.common.HttpCallback;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UseRetrofit {
    private final String NAVER_BASE_URL = "https://openapi.naver.com/";
    private final String GOOGLE_BASE_URL = "https://www.googleapis.com/";

    public void naverBlogPost(String token, File file, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(NAVER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface service = client.create(APIInterface.class);

        String header = "Bearer " + token;
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "네이버 multi-part 이미지 첨부 테스트");
        RequestBody contents = RequestBody.create(MediaType.parse("text/plain"), "<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
        RequestBody image = RequestBody.create(MediaType.parse("image/gif"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), image);

        Call<JSONObject> call = service.sendPost(header, title, contents, part);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("Success", String.valueOf(response.raw()));
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("Success", call.toString());
                callback.onFail();
            }
        });
    }

    public void googleDriveUpload(String token, File file, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl(GOOGLE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface service = client.create(APIInterface.class);

        String header = "Bearer " + token;
        RequestBody image = RequestBody.create(MediaType.parse("image/gif"), file);

        Call<JSONObject> call = service.upload(header, image);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("googleDriveUpload", String.valueOf(response.raw()));
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("googleDriveUpload", t.getMessage());
                callback.onFail();
            }
        });
    }
}
