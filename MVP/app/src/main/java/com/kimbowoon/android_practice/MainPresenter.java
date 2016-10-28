package com.kimbowoon.android_practice;

/**
 * Created by 보운 on 2016-10-23.
 */

public class MainPresenter implements MainContract.UserAction {
    private MainContract.View mMainView;
    private MainModel mMainModel;

    public MainPresenter(MainContract.View view) {
        this.mMainModel = new MainModel();
        this.mMainView = view;
    }

    @Override
    public void sumButtonClick(int val1, int val2) {
        mMainView.setText(String.valueOf(mMainModel.getSumData(val1, val2)));
    }
}
