package com.example.secret.rxandroidhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit client = new Retrofit.Builder().baseUrl("https://randomuser.me").addConverterFactory(GsonConverterFactory.create()).build();
        APIInterface service = client.create(APIInterface.class);
        Call<PersonModel> call = service.getUsers(10);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.isSuccessful()) {
                    Log.i("Success", String.valueOf(response.body()));
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
}