package com.kimbowoon.rxjava;

import android.os.Bundle;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

// RxAndroid는 메모리 누수가 있어서 ThirdParty Library를 사용해 메모리를 관리

/**
 * RxAppCompatActivity는 RxLifeCycle에 있는 클래스
 * onCreate()에서 Subscribe하면 onDestroy()에서 Unsubscribe
 * onResume()에서 Subscribe하면 onPause()에서 Unsubscribe
 * 만약 Unsubscribe 시점을 바꾸고 싶다면 bindUntilEvent로 조정할 수 있음
 */
public class MainActivity extends RxAppCompatActivity {
    private final String TAG = MainActivity.class.getName();

    @Bind(R.id.textView)
    TextView simpleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /**
         * retrolambda를 사용
         */
        Observable.just("Hello RxAndroid!!")
                .compose(this.<String>bindToLifecycle())
                .subscribe(s -> {
                    simpleTextView.setText(s);
                });

        /**
         * RxAndroid 사용
         * onCompleted() 이벤트가 성공적으로 호출 됨
         * onError() 이벤트 처리중 오류가 발생
         * onNext() 이벤트가 발생
        Observable<String> simpleObservable =
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Hello RxAndroid !!");
                        subscriber.onCompleted();
                    }
                }).compose(this.<String>bindToLifecycle());


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
                ((TextView) findViewById(R.id.textView)).setText(text);
            }
        });
         */
    }
}
