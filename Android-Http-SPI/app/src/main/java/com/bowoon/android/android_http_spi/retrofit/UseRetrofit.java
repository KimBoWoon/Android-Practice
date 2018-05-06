package com.bowoon.android.android_http_spi.retrofit;

import android.util.Log;

import com.bowoon.android.android_http_spi.common.HttpCallback;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UseRetrofit {
    public void naverBlogPost(String token, byte[] bytes, final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl("https://openapi.naver.com/").addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface service = client.create(APIInterface.class);

        RequestBody title = RequestBody.create(MediaType.parse("text/plain; charset=UTF-8"), "네이버 multi-part 이미지 첨부 테스트");
        RequestBody contents = RequestBody.create(MediaType.parse("text/plain; charset=UTF-8"), "<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), bytes);

//        MultipartBody.Part title = MultipartBody.Part.createFormData("title", "네이버 multi-part 이미지 첨부 테스트");
//        MultipartBody.Part contents = MultipartBody.Part.createFormData("contents", "<font color='red'>multi-part</font>로 첨부한 글입니다. <br>  이미지 첨부 <br> <img src='#0' />");
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", "image.jpg", image);

        Call<JSONObject> call = service.sendPost(token, title, contents, file);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.i("Success", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("Success", call.toString());
            }
        });
    }
}
