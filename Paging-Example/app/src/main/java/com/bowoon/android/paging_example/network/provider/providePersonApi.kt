package com.bowoon.android.paging_example.network.provider

import com.bowoon.android.paging_example.network.api.PersonApi
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//fun providePersonApi(): PersonApi = Retrofit.Builder().apply {
//    baseUrl("https://randomuser.me/")
//    client(createOkHttpClient())
//    addConverterFactory(GsonConverterFactory.create())
//    addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
//}.build().create(PersonApi::class.java)

fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(okHttpProfiler)
    .build()

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpProfiler = OkHttpProfilerInterceptor()

fun providePersonApi(): PersonApi = Retrofit.Builder().apply {
    baseUrl("https://randomuser.me/")
    client(createOkHttpClient())
    addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaTypeOrNull()!!))
    addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
}.build().create(PersonApi::class.java)