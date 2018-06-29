package com.bowoon.android.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        RetrofitManager.getUser(object: HttpCallback {
            override fun onSuccess(o: Any) {
                if (o is RetrofitUser) {
                    val adapter = UserAdapter(o.results)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                }
                Log.i("MainActivity", o.toString())
            }

            override fun onFail(message: String) {
                Log.i("MainActivity", message)
            }
        })

//        VolleyManager.getInstance().setRequestQueue(applicationContext)
//
//        HttpRequest.userRequest(callback = {
//            when (it) {
//                is ArrayList<User> -> {
////                    Log.i("MainActivity", it.toString())
//                    val adapter = UserAdapter(it)
//                    recyclerView.setHasFixedSize(true)
//                    recyclerView.layoutManager = linearLayoutManager
//                    recyclerView.adapter = adapter
//                }
//                null -> Log.i("MainActivity", "Null")
//            }
//        })
    }
}