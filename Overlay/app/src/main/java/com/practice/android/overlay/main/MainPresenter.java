package com.practice.android.overlay.main;

import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practice.android.overlay.service.RestAPIMethod;
import com.practice.android.overlay.service.TranslationDeserializer;
import com.practice.android.overlay.service.TranslationModel;
import com.practice.android.overlay.service.OverlayService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by null on 11/21/17.
 */

public class MainPresenter implements MainContract.UserAction {
    public static final String baseUrl = "https://openapi.naver.com/";
    private MainContract.View mMainView;
    private Gson gson;
    private RestAPIMethod service;

    public MainPresenter(MainContract.View view) {
        this();
        this.mMainView = view;
    }

    public MainPresenter() {
        initGson();
        initRetrofit();
    }

    @Override
    public void translationClick() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                service.getSMTText("en", "ko", "Hello")
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> showToast(data.getTranslatedText()),
                                e -> showToast(e.getMessage()))
        );
    }

    public void overlayServiceStart() {
        ((MainActivity) mMainView).startService(new Intent((MainActivity) mMainView, OverlayService.class));
    }

    public void overlayServiceStop() {
        ((MainActivity) mMainView).stopService(new Intent((MainActivity) mMainView, OverlayService.class));
    }

    public void showToast(String str) {
        Toast.makeText((MainActivity) mMainView, str, Toast.LENGTH_SHORT).show();
    }

    private void initGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(TranslationModel.class, new TranslationDeserializer())
                .create();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(RestAPIMethod.class);
    }
}
