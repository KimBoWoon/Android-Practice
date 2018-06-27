package com.bowoon.android.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VolleyManager.getInstance().setRequestQueue(applicationContext)

        userRequest()
    }

    fun userRequest() {
        val request = JsonObjectRequest(
                Request.Method.GET,
                "https://randomuser.me/api/?results=10",
                JSONObject(),
                Response.Listener<JSONObject> {
                    Log.i("Success", it.toString())
                    val gson = Gson()
                    var user = ArrayList<User>()
                    for (i in 0 until it.getJSONArray("results").length()) {
                        Log.i("Success", it.getJSONArray("results").get(i).toString())
                        user.add(gson.fromJson(it.getJSONArray("results").get(i).toString(), User::class.java))
                        Log.i("User", user[i].toString())
                    }
                },
                Response.ErrorListener {
                    Log.i("Error", it.message)
                }
        )

        VolleyManager.getInstance().getRequestQueue()?.add(request)
    }
}
