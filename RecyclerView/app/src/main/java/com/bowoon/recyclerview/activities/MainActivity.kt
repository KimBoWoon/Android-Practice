package com.bowoon.recyclerview.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.recyclerview.R
import com.bowoon.recyclerview.adapters.RecyclerAdapter
import com.bowoon.recyclerview.model.Item
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val items = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        items.add(Item(R.mipmap.ic_launcher, "Basketball"))
        items.add(Item(R.mipmap.ic_launcher, "Soccer"))
        items.add(Item(R.mipmap.ic_launcher, "Baseball"))
        items.add(Item(R.mipmap.ic_launcher, "Ping-Pong"))
        items.add(Item(R.mipmap.ic_launcher, "Badminton"))
        items.add(Item(R.mipmap.ic_launcher, "Tennis"))

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = RecyclerAdapter().apply {
            setItems(items)
        }
    }
}