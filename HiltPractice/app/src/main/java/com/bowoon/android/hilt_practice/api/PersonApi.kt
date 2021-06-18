package com.bowoon.android.hilt_practice.api

import com.bowoon.android.hilt_practice.model.Persons
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PersonApi {
    @GET("/api/")
    fun getUsers(@QueryMap params: Map<String, String>): Single<Persons>
}