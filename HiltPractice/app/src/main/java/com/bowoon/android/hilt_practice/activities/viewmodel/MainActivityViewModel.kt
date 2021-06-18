package com.bowoon.android.hilt_practice.activities.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.bowoon.android.hilt_practice.api.ApiHelperImpl
import com.bowoon.android.hilt_practice.api.PersonApi
import com.bowoon.android.hilt_practice.base.BaseViewModel
import com.bowoon.android.hilt_practice.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiHelperImpl: ApiHelperImpl
) : BaseViewModel(), LifecycleObserver {
    val personList = MutableLiveData<MutableList<Person>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        apiHelperImpl.getPersons(
            compositeDisposable,
            mapOf("results" to "10"),
            { personList.value = it.persons },
            { Log.e("MainActivityViewModel", it.message ?: "something wrong") }
        )
    }
}