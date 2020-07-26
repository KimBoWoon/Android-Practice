package com.bowoon.android.android_retrofit.api

import com.bowoon.android.android_retrofit.model.PersonModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {
    @GET("/api/")
    fun getUsers(@Query("results") results: Int): Call<PersonModel>

//    @GET("/api/")
//    fun getUsers(@Query("results") results: Int): Single<PersonModel>
}