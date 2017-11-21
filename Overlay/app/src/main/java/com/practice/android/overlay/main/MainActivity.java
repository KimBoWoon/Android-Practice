package com.practice.android.overlay.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.practice.android.overlay.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenter(this);
    }

    @OnClick(R.id.main_service_start)
    public void overlayServiceStart() {
        mMainPresenter.overlayServiceStart();
    }

    @OnClick(R.id.main_service_stop)
    public void overlayServiceStop() {
        mMainPresenter.overlayServiceStop();
    }

    @OnClick(R.id.main_translation_button)
    public void translationClick() {
        mMainPresenter.translationClick();
    }
}
