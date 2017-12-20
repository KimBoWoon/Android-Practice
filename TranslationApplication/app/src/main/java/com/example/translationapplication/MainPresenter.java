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
        ServiceProvider.registerDefaultProvider(new ProviderImplement());
    }

    private void requestPapago(String s) {
        ServiceProvider.newInstance().request(
                ((MainActivity) mMainView).getApplicationContext(),
                new VolleyCallback() {
                    @Override
                    public void onSuccess(MainModel result) {
                        mMainView.setText(result.getTranslatedText());
                    }
                },
                s);
    }

    @Override
    public void requestBtnClick(String s) {
        requestPapago(s);
    }
}