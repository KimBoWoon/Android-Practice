package com.bowoon.android.kotlin

interface HttpCallback {
    fun onSuccess(o: Any)
    fun onFail(message: String)
}