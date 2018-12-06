package com.bowoon.android.android_http_practice.okhttp;

import android.util.Log;

import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClass implements HttpService {
    private OkHttpClient client;
    private final String BASE_URL = "https://randomuser.me/api/";

    public OkHttpClass() {
        // OkHttp 초기화 및 로그 인터셉터 추가
        this.client = new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build();
    }

    public void getPerson(final HttpCallback callback) {
        // url 설정
        String url = HttpUrl
                .parse(BASE_URL)
                .newBuilder()
                .addEncodedQueryParameter("results", "10")
                .build()
                .toString();

        // request 설정
        Request request = new Request.Builder().url(url).build();

        // 만들어진 request를 토대로 데이터 요청
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
                callback.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Person person = gson.fromJson(response.body().string(), Person.class);
                callback.onSuccess(person);
            }
        });
    }
}
