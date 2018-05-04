package com.bowoon.android.android_http_spi.retrofit;

import android.util.Log;

import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.model.PersonModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UseRetrofit {
    public void requestUser(final HttpCallback callback) {
        Retrofit client = new Retrofit.Builder().baseUrl("https://randomuser.me").addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface service = client.create(APIInterface.class);
        Call<PersonModel> call = service.getUsers(10);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Success", response.toString());
                    PersonModel person = response.body();
                    Log.i("Success", String.valueOf(response.body()));
                    callback.onSuccess(person);
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                Log.i("Failed", "Failed Connection");
                callback.onFail();
            }
        });
    }
}
