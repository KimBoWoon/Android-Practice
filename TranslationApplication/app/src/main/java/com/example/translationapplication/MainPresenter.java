package com.example.translationapplication;

import com.example.translationapplication.http.ProviderImplement;
import com.example.translationapplication.http.ServiceProvider;
import com.example.translationapplication.http.VolleyCallback;

/**
 * Created by 보운 on 2017-12-19.
 */

public class MainPresenter implements MainContract.UserAction {
    private MainContract.View mMainView;

    public MainPresenter(MainContract.View view) {
        this.mMainView = view;
        ServiceProvider.registerDefaultProvider(new ProviderImplement());
    }

    private void requestPapago(TranslationType transType, String s) {
        ServiceProvider.newInstance().request(
                ((MainActivity) mMainView).getApplicationContext(),
                transType,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(MainModel result) {
                        mMainView.setText(result.getTranslatedText());
                    }

                    @Override
                    public void onFail() {
                        mMainView.setText("Network Failed");
                    }
                },
                s);
    }

    @Override
    public void requestBtnClick(TranslationType transType, String s) {
        requestPapago(transType, s);
    }
}