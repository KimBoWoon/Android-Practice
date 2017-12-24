package com.example.translationapplication.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translationapplication.R;
import com.example.translationapplication.util.TranslationType;
import com.example.translationapplication.util.ValuableChecker;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter mMainPresenter;
    private EditText translationText;

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

        translationText = (EditText) findViewById(R.id.main_edit_text);
    }

    @OnClick(R.id.main_btn_smt_request)
    public void requestSMTBtnClick() {
        if (ValuableChecker.valueNull(translationText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "값을 제대로 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "SMT", Toast.LENGTH_SHORT).show();
        mMainPresenter.requestBtnClick(TranslationType.SMT, translationText.getText().toString());
    }

    @OnClick(R.id.main_btn_nmt_request)
    public void requestNMTBtnClick() {
        if (ValuableChecker.valueNull(translationText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "값을 제대로 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "NMT", Toast.LENGTH_SHORT).show();
        mMainPresenter.requestBtnClick(TranslationType.NMT, translationText.getText().toString());
    }

    public void setText(String s) {
        TextView textView = (TextView) findViewById(R.id.main_text_result);
        textView.setText(s);
    }
}
