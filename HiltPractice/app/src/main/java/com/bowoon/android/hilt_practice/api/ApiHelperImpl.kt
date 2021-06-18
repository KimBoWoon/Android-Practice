package com.bowoon.android.hilt_practice.api

import com.bowoon.android.hilt_practice.model.Persons
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val personApi: PersonApi
) : ApiHelper {
    override fun getPersons(
        compositeDisposable: CompositeDisposable,
        params: Map<String, String>?,
        success: ((Persons) -> Unit)?,
        error: ((Throwable) -> Unit)?
    ) {
        personApi
            .getUsers(params ?: mapOf())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success?.invoke(it) },
                { error?.invoke(it) }
            )
    }
}