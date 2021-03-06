package com.bowoon.android.kotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIInterface {
    @GET("/api/")
    fun getUsers(@Query("results") results: Int): Call<RandomUser>
}