package com.bowoon.android.paging_example.repository

import com.bowoon.android.paging_example.model.PersonModel
import com.bowoon.android.paging_example.network.provider.providePersonApi
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

object Repository {
    private val disposeBag = CompositeDisposable()

    fun useRx(): Single<PersonModel> {
        return Single.create { emitter ->
            providePersonApi()
                .getUsers(10)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { emitter.onSuccess(it) },
                    { e -> emitter.onError(e) }
                ).addTo(disposeBag)
        }
    }

    fun disposeBagClear() {
        disposeBag.clear()
    }
}