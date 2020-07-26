package com.bowoon.android.android_retrofit.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_retrofit.R
import com.bowoon.android.android_retrofit.adapter.PersonAdapter
import com.bowoon.android.android_retrofit.listener.PersonListListener
import com.bowoon.android.android_retrofit.model.PersonModel
import com.bowoon.android.android_retrofit.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Repository.notUseRx(object : PersonListListener {
            override fun onSuccess(response: Response<PersonModel>) {
                Log.i("Success", response.toString())
                val person = response.body()
                recyclerview.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                recyclerview.adapter = PersonAdapter().apply {
                    person?.let { setItems(person.items) }
                }
            }

            override fun onError() {
                Log.i("Failed", "Failed Connection")
            }
        })
//        Repository
//            .useRx()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                    recyclerview.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
//                    recyclerview.adapter = PersonAdapter().apply {
//                        setItems(it.items)
//                    }
//                },
//                { e -> e.printStackTrace() }
//            ).addTo(CompositeDisposable())
    }
}