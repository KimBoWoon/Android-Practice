package com.example.translationapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        this.mMainPresenter = new MainPresenter(this);
    }

    @OnClick(R.id.main_btn_smt_request)
    public void requestSMTBtnClick() {
        Toast.makeText(getApplicationContext(), "SMT", Toast.LENGTH_SHORT).show();
        mMainPresenter.requestBtnClick(TranslationType.SMT, "Hello");
    }

    @OnClick(R.id.main_btn_nmt_request)
    public void requestNMTBtnClick() {
        Toast.makeText(getApplicationContext(), "NMT", Toast.LENGTH_SHORT).show();
        mMainPresenter.requestBtnClick(TranslationType.NMT, "Hello");
    }

    public void setText(String s) {
        TextView textView = (TextView) findViewById(R.id.main_text_result);
        textView.setText(s);
    }
}
