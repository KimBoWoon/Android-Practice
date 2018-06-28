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

        fun userRequest(callback: (ArrayList<User>?) -> Unit) {
            val request = JsonObjectRequest(
                    Request.Method.GET,
                    "https://randomuser.me/api/?results=10",
                    JSONObject(),
                    Response.Listener<JSONObject> {
                        Log.i("Success", it.toString())
                        val user = ArrayList<User>()
                        for (i in 0 until it.getJSONArray("results").length()) {
//                            Log.i("Success", it.getJSONArray("results").get(i).toString())
                            user.add(gson.fromJson(it.getJSONArray("results").get(i).toString(), User::class.java))
//                            Log.i("User", user[i].toString())
                        }
                        callback.invoke(user)
                    },
                    Response.ErrorListener {
                        Log.i("Error", it.message)
                        callback.invoke(null)
                    }
            )

            VolleyManager.getInstance().getRequestQueue()?.add(request)
        }
    }
}