package com.bowoon.android.android_retrofit.listener

import com.bowoon.android.android_retrofit.model.PersonModel
import retrofit2.Response

interface PersonListListener {
    fun onSuccess(response: Response<PersonModel>)
    fun onError()
}