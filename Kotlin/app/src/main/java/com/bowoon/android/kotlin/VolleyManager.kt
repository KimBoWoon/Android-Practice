package com.bowoon.android.kotlin

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyManager {
    private var rq: RequestQueue? = null

    init {

    }

    companion object {
        private class Singleton {
            companion object {
                val INSTANCE: VolleyManager = VolleyManager()
            }
        }

        fun getInstance(): VolleyManager = Singleton.INSTANCE
    }

    fun getRequestQueue(): RequestQueue? {
        if (rq == null) {
            throw IllegalAccessException("Need Initialize Request Queue")
        }
        return rq
    }

    fun setRequestQueue(context: Context) {
        this.rq = Volley.newRequestQueue(context)
    }
}