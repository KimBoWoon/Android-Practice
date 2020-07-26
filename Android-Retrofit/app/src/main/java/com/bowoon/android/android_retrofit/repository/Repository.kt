package com.bowoon.android.android_retrofit.repository

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_retrofit.adapter.PersonAdapter
import com.bowoon.android.android_retrofit.api.providePersonApi
import com.bowoon.android.android_retrofit.listener.PersonListListener
import com.bowoon.android.android_retrofit.model.PersonModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Repository {
    fun notUseRx(listener: PersonListListener) {
        providePersonApi().getUsers(10).enqueue(object : Callback<PersonModel> {
            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onError()
            }

            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                if (response.isSuccessful) {
                    listener.onSuccess(response)
                }
            }
        })
    }

//    fun useRx(): Single<PersonModel> {
//        return Single.create { emitter ->
//            providePersonApi()
//                .getUsers(10)
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                    { emitter.onSuccess(it) },
//                    { e -> emitter.onError(e) }
//                ).addTo(CompositeDisposable())
//        }
//    }
}