package com.bowoon.android.hilt_practice.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun providePersonApi(): PersonApi = Retrofit.Builder().apply {
    baseUrl("https://randomuser.me/")
    client(createOkHttpClient())
    addConverterFactory(GsonConverterFactory.create())
    addCallAdapterFactory(RxJava3CallAdapterFactory.create())
}.build().create(PersonApi::class.java)

fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}