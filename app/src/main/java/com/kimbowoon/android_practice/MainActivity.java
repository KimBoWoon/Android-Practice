package com.kimbowoon.android_practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter mMainPresenter;
    @Bind(R.id.main_edit_val1)
    EditText mValEdit1;
    @Bind(R.id.main_edit_val2)
    EditText mValEdit2;
    @Bind(R.id.sum_result_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMainPresenter = new MainPresenter(this);
    }

    @Override
    public void setText(String s) {
        mTextView.setText(s);
    }

    @OnClick(R.id.main_result_btn)
    void onSumResultButtonClick(View view) {
        int val1 = Integer.parseInt(mValEdit1.getText().toString());
        int val2 = Integer.parseInt(mValEdit2.getText().toString());

        mMainPresenter.sumButtonClick(val1, val2);
    }
}
