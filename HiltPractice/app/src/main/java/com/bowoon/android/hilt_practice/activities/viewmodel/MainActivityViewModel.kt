package com.bowoon.android.hilt_practice.activities.viewmodel

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.bowoon.android.hilt_practice.api.ApiHelperImpl
import com.bowoon.android.hilt_practice.base.BaseViewModel
import com.bowoon.android.hilt_practice.model.Person
import com.bowoon.android.hilt_practice.model.Persons
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiHelperImpl: ApiHelperImpl
) : BaseViewModel(), LifecycleObserver {
    val personList = MutableLiveData<MutableList<Person>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    fun loadUserData(success: ((Persons) -> Unit)? = null, error: ((Throwable) -> Unit)? = null) {
        apiHelperImpl.getPersons(
            compositeDisposable,
            mapOf("results" to "10"),
            {
                personList.value = it.persons
                success?.invoke(it)
            },
            {
                Log.e("MainActivityViewModel", it.message ?: "something wrong")
                error?.invoke(it)
            }
        )
    }
}