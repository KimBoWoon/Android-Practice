package com.bowoon.android.hilt_practice.api

import com.bowoon.android.hilt_practice.model.Persons
import io.reactivex.rxjava3.disposables.CompositeDisposable

interface ApiHelper {
    fun getPersons(
        compositeDisposable: CompositeDisposable,
        params: Map<String, String>? = null,
        success: ((Persons) -> Unit)? = null,
        error: ((Throwable) -> Unit)? = null
    )
}