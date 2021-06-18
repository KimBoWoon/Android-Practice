package com.bowoon.android.hilt_practice.repository

import com.bowoon.android.hilt_practice.api.providePersonApi
import com.bowoon.android.hilt_practice.model.Persons
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

object Repository {
    private val compositeDisposable = CompositeDisposable()

    fun useRx(): Single<Persons> {
        return Single.create { emitter ->
            providePersonApi()
                .getUsers(mapOf("results" to "10"))
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { emitter.onSuccess(it) },
                    { e -> emitter.onError(e) }
                ).addTo(compositeDisposable)
        }
    }
}