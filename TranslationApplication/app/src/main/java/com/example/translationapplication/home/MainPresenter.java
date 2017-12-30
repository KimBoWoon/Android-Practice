package com.example.translationapplication.home;

import com.example.translationapplication.http.HttpServiceProvider;
import com.example.translationapplication.http.ProviderImplement;
import com.example.translationapplication.http.VolleyCallback;
import com.example.translationapplication.http.VolleyManager;
import com.example.translationapplication.util.DataManager;
import com.example.translationapplication.util.TranslationType;

/**
 * Created by 보운 on 2017-12-19.
 */

public class MainPresenter implements MainContract.UserAction {
    private MainContract.View mMainView;

    public MainPresenter(MainContract.View view) {
        this.mMainView = view;
        HttpServiceProvider.registerDefaultProvider(new ProviderImplement());
        VolleyInit();
    }

    private void VolleyInit() {
        VolleyManager.getInstance().setRequestQueue(((MainActivity) mMainView).getApplicationContext());
    }

    private void requestPapago(String s) {
        HttpServiceProvider.newInstance().requestPapagoAPI(
                new VolleyCallback() {
                    @Override
                    public void onSuccess(TranslatedModel result) {
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
    public void requestBtnClick(String s) {
        requestPapago(s);
    }

    @Override
    public void toggle() {
        if (DataManager.getInstance().getType() == TranslationType.SMT) {
            DataManager.getInstance().setType(TranslationType.NMT);
        } else if (DataManager.getInstance().getType() == TranslationType.NMT) {
            DataManager.getInstance().setType(TranslationType.SMT);
        }
    }
}