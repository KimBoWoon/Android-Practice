package com.kimbowoon.rxjava;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
// RxAndroid는 메모리 누수가 있어서 ThirdParty Library를 사용해 메모리를 관리

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RxAppCompatActivity는 RxLifeCycle에 있는 클래스
 * onCreate()에서 Subscribe하면 onDestroy()에서 Unsubscribe
 * onResume()에서 Subscribe하면 onPause()에서 Unsubscribe
 * 만약 Unsubscribe 시점을 바꾸고 싶다면 bindUntilEvent로 조정할 수 있음
 */
public class MainActivity extends RxAppCompatActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getName();

    private MainPresenter mMainPresenter;

    @Bind(R.id.sum_result_text)
    TextView mTextView;
    @Bind(R.id.edit_val1)
    EditText mEditVal1;
    @Bind(R.id.edit_val2)
    EditText mEditVal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void init() {
        ButterKnife.bind(this);

        mMainPresenter = new MainPresenter(this);
    }

    @Override
    public void setText(String s) {
        mTextView.setText(s);
    }

    @OnClick(R.id.sum_result_btn)
    void onClicked() {
        int val1 = Integer.valueOf(mEditVal1.getText().toString());
        int val2 = Integer.valueOf(mEditVal2.getText().toString());

        mMainPresenter.sumButtonClicked(val1, val2);
    }
}
