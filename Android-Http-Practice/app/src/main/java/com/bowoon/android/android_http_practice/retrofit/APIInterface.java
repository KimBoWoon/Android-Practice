package com.bowoon.android.android_http_practice.retrofit;

import com.bowoon.android.android_http_practice.model.Person;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("/api/")
    Call<Person> getPerson(@Query("results") int results);
}
