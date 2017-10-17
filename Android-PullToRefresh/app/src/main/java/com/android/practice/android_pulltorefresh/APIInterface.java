package com.android.practice.android_pulltorefresh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Null on 2017-10-17.
 */

public interface APIInterface {
    @GET("/api/")
    Call<PersonModel> getUsers(@Query("results") int results);
}
