package com.bowoon.unit_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter mMainPresenter;
    private EditText mValEdit1;
    private EditText mValEdit2;
    private TextView mTextView;
    private Button mResultBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        mMainPresenter = new MainPresenter(this);

        mValEdit1 = (EditText) findViewById(R.id.main_edit_val1);
        mValEdit2 = (EditText) findViewById(R.id.main_edit_val2);
        mResultBtn = (Button) findViewById(R.id.main_result_btn);
        mResultBtn.setOnClickListener(listener);
    }

    @Override
    public void setText(String s) {
        mTextView.setText(s);
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_result_btn:
                    int val1 = Integer.parseInt(mValEdit1.getText().toString());
                    int val2 = Integer.parseInt(mValEdit2.getText().toString());

                    mMainPresenter.sumButtonClick(val1, val2);
                    break;
            }
        }
    };
}
