package com.bowoon.android.android_http_spi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bowoon.android.android_http_spi.adapter.RecyclerAdapter;
import com.bowoon.android.android_http_spi.common.CreateHttpServiceProvider;
import com.bowoon.android.android_http_spi.common.HttpCallback;
import com.bowoon.android.android_http_spi.common.HttpServiceProvider;
import com.bowoon.android.android_http_spi.model.PersonModel;
import com.bowoon.android.android_http_spi.volley.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        VolleyManager.getInstance().setRequestQueue(getApplicationContext());
        HttpServiceProvider.registerDefaultProvider(new CreateHttpServiceProvider());

//        HttpServiceProvider.getRetrofitInstance().requestUser(new HttpResultCallback());
        HttpServiceProvider.getVolleyInstance().requestUser(new HttpResultCallback());
    }

    private class HttpResultCallback implements HttpCallback {
        @Override
        public void onSuccess(Object result) {
            if (result instanceof PersonModel) {
                PersonModel p = (PersonModel) result;
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), p.getItems()));
            } else if (result instanceof JSONObject) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                JSONObject j = (JSONObject) result;
                PersonModel persons = gson.fromJson(String.valueOf(j), PersonModel.class);
                Log.i("listsize", String.valueOf(persons.getItems().size()));
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), persons.getItems()));
            }
        }

        @Override
        public void onFail() {

        }
    }
}
