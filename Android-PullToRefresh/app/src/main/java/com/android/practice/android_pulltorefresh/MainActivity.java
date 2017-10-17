package com.android.practice.android_pulltorefresh;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Retrofit client;
    private APIInterface service;
    private Call<PersonModel> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        client = new Retrofit.Builder().baseUrl("https://randomuser.me").addConverterFactory(GsonConverterFactory.create()).build();
        service = client.create(APIInterface.class);
        call = service.getUsers(10);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Success", response.toString());
                    PersonModel person = response.body();
                    recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), response.body().getItems()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                Log.i("Failed", "Failed Connection");
            }
        });
    }

    @Override
    public void onRefresh() {
        call.clone().enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Success", response.toString());
                    PersonModel person = response.body();
                    recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), response.body().getItems()));
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                Log.i("Failed", "Failed Connection");
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
