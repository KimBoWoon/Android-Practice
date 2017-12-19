package com.example.translationapplication;

import com.example.translationapplication.http.ProviderImplement;
import com.example.translationapplication.http.ServiceProvider;

/**
 * Created by 보운 on 2017-12-19.
 */

public class MainPresenter implements MainContract.UserAction {
    private MainContract.View mMainView;

    public MainPresenter(MainContract.View view) {
        this.mMainView = view;
    }

    public void requestPapago() {
        ServiceProvider.registerDefaultProvider(new ProviderImplement());
        ServiceProvider.newInstance().request(((MainActivity) mMainView).getApplicationContext(), "Apple");
    }
}
