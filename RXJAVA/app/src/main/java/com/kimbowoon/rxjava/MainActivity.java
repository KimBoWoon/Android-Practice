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

        Observable.just("Hello RxAndroid!!")
                .compose(this.<String>bindToLifecycle())
                .subscribe(s -> {
                    simpleTextView.setText(s);
                });
    }
}
