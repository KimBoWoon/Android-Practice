package com.bowoon.android.hilt_practice.activities.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.bowoon.android.hilt_practice.api.ApiHelperImpl
import com.bowoon.android.hilt_practice.api.PersonApi
import com.bowoon.android.hilt_practice.base.BaseViewModel
import com.bowoon.android.hilt_practice.model.Person
import com.bowoon.android.hilt_practice.model.Persons
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.QueryMap
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val apiHelperImpl: ApiHelperImpl,
    private val personApi: PersonApi
) : BaseViewModel(), LifecycleObserver {
    val personList = MutableLiveData<MutableList<Person>>()
    val personFlow = flow<Persons> {
        val result = personApi.getUsersUsingCoroutines(mapOf("results" to "10"))
        Log.d("personFlow", result.toString())
        emit(result)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        viewModelScope.launch {
            personFlow
                .onCompletion { Log.d("onCompletion", it.toString()) }
                .flowOn(Dispatchers.IO)
                .collect {
                    Log.d("collect", it.toString())
//                    personList.value = it.persons
                }
        }
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