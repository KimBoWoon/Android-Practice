package com.kimbowoon.rxjava;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by 보운 on 2016-11-05.
 */

public class MainPresenter implements MainContract.UserAction {
    private MainActivity mMainView;
    private MainModel mMainModel;

    public MainPresenter(MainActivity view) {
        this.mMainView = view;
        this.mMainModel = new MainModel();
    }

    @Override
    public void sumButtonClicked(int val1, int val2) {
//        mMainView.setText(String.valueOf(mMainModel.getSum(val1, val2)));

        /**
         * retrolambda를 사용
         */
        Observable.just("Hello RxAndroid!!")
                .compose(mMainView.<String>bindToLifecycle())
                .subscribe(s -> {
                    mMainView.setText(s);
                });

        /**
         * RxAndroid 사용
         * onCompleted() 이벤트가 성공적으로 호출 됨
         * onError() 이벤트 처리중 오류가 발생
         * onNext() 이벤트가 발생
         */
        Observable<String> simpleObservable =
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(String.valueOf(mMainModel.getSum(val1, val2)));
                        subscriber.onCompleted();
                    }
                }).compose(mMainView.bindToLifecycle());


        simpleObservable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "complete!");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error: " + e.getMessage());
            }

            @Override
            public void onNext(String text) {
                mMainView.setText(text);
            }
        });
    }
}
