package com.bowoon.android.android_retrofit.api

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun providePersonApi(): PersonApi = Retrofit.Builder().apply {
    baseUrl("https://randomuser.me/")
    client(createOkHttpClient())
    addConverterFactory(GsonConverterFactory.create())
    addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
}.build().create(PersonApi::class.java)

fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(okHttpProfiler)
    .build()

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpProfiler = OkHttpProfilerInterceptor()