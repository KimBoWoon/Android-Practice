package com.bowoon.android.android_http_spi.retrofit;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @Multipart
    @POST("/blog/writePost.json")
    Call<JSONObject> sendPost(@Header("Authorization") String authorization,
                              @Part("title") RequestBody title,
                              @Part("contents") RequestBody contents,
                              @Part MultipartBody.Part image
    );

    @POST("/upload/drive/v3/files?uploadType=media")
    Call<JSONObject> upload(@Header("Authorization") String authorization,
                            @Body RequestBody image);
}
