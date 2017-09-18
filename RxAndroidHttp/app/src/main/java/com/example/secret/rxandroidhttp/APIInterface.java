package com.example.secret.rxandroidhttp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Secret on 2017-09-15.
 */

public interface APIInterface {
    @GET("/api/")
    Call<PersonModel> getUsers(@Query("results") int results);
}
