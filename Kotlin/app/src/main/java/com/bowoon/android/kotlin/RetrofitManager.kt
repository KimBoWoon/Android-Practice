package com.bowoon.android.kotlin

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    companion object {
        private lateinit var retrofit: Retrofit
        private var service: APIInterface

        init {
            this.service = createRetrofit()
        }

        fun getUser(callback: HttpCallback) {
            val call: Call<RetrofitUser> = service.getUsers(10)

            call.enqueue(object: Callback<RetrofitUser> {
                override fun onResponse(call: Call<RetrofitUser>?, response: Response<RetrofitUser>?) {
                    Log.i("Retrofit2", response?.message())
                    val retrofitUser: RetrofitUser = response?.body()!!
                    Log.i("response", retrofitUser.toString())
                    callback.onSuccess(retrofitUser)
                }

                override fun onFailure(call: Call<RetrofitUser>?, t: Throwable?) {
                    Log.e("Retrofit2", t?.message)
                    callback.onFail(t?.message!!)
                }
            })
        }

        private fun createRetrofit(): APIInterface {
            retrofit = Retrofit
                    .Builder()
                    .baseUrl("https://randomuser.me")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createOkHttpClient())
                    .build()

            return retrofit.create(APIInterface::class.java)
        }

        private fun createOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            }

            return OkHttpClient.Builder()
                    .addNetworkInterceptor(interceptor)
                    .build()
        }
    }
}