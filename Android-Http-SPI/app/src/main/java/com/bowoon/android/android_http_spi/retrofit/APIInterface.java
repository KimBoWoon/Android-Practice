package com.bowoon.android.android_http_spi.retrofit;

import com.bowoon.android.android_http_spi.model.PersonModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("/api/")
    Call<PersonModel> getUsers(@Query("results") int results);
}
