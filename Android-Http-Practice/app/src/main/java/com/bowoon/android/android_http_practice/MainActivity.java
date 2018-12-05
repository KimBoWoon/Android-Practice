package com.bowoon.android.android_http_practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bowoon.android.android_http_practice.common.HttpCallback;
import com.bowoon.android.android_http_practice.common.HttpService;
import com.bowoon.android.android_http_practice.common.HttpServiceFactory;
import com.bowoon.android.android_http_practice.common.RecyclerAdapter;
import com.bowoon.android.android_http_practice.model.Person;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

//        HttpService retrofit = HttpServiceFactory.createClass("retrofit");
//        retrofit.getPerson(new HttpCallback() {
//            @Override
//            public void onSuccess(Object o) {
//                if (o instanceof Person) {
//                    RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), ((Person) o).getItems());
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });

//        HttpService volley = HttpServiceFactory.createClass("volley", getApplicationContext());
//        volley.getPerson(new HttpCallback() {
//            @Override
//            public void onSuccess(Object o) {
//                if (o instanceof Person) {
//                    RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), ((Person) o).getItems());
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });

        HttpService okHttp = HttpServiceFactory.createClass("okhttp");
        okHttp.getPerson(new HttpCallback() {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof Person) {
                    final RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), ((Person) o).getItems());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFail() {
                Log.d("error", "OkHttp");
            }
        });
    }
}
