package com.bowoon.android.android_http_practice.retrofit;

import android.util.Log;

import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClass implements HttpService {
    private final String BASE_URL = "https://randomuser.me";

    private OkHttpClient createOkHttpClient() {
        // OkHttp 설정
        // retrofit과 함께 사용하면 좋기 때문
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public void getPerson(final HttpCallback callback) {
        // retrofit 생성
        Retrofit client = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        APIInterface service = client.create(APIInterface.class);

        // 만들어진 retrofit을 토대로 데이터 요청
        Call<Person> call = service.getPerson(10);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (response.isSuccessful()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String jsonString = gson.toJson(response.body());
                    Person person = gson.fromJson(jsonString, Person.class);
                    callback.onSuccess(person);
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.i("Failed", "Failed Connection");
                callback.onFail();
            }
        });
    }
}
