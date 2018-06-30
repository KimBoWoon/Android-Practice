package com.bowoon.android.kotlin

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject

class HttpRequest {
    companion object {
        private val gson: Gson = Gson()

        fun userRequest(callback: HttpCallback) {
            val request = JsonObjectRequest(
                    Request.Method.GET,
                    "https://randomuser.me/api/?results=10",
                    JSONObject(),
                    Response.Listener<JSONObject> {
                        Log.i("Success", it.toString())
                        val user = gson.fromJson<RandomUser>(it.toString(), RandomUser::class.java)
                        callback.onSuccess(user)
                    },
                    Response.ErrorListener {
                        Log.i("Error", it.message)
                        callback.onFail(it?.message!!)
                    }
            )

            VolleyManager.getInstance().getRequestQueue()?.add(request)
        }
    }
}