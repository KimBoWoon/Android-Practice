package com.bowoon.android.paging_example.network.api

import com.bowoon.android.paging_example.model.PersonModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {
    @GET("/api/")
    fun getUsers(@Query("results") results: Int): Single<PersonModel>
}