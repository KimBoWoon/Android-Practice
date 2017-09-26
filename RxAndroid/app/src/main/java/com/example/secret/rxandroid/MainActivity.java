package com.example.secret.rxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {
    private EditText inputEdit;
    private TextView printText;
    private Button printBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEdit = (EditText) findViewById(R.id.main_edit);
        printText = (TextView) findViewById(R.id.main_text);
        printBtn = (Button) findViewById(R.id.main_btn);

        printBtn.setOnClickListener(view -> Observable.just(inputEdit.getText().toString())
                .map(Integer::parseInt)
                .filter(x -> 1 < x && x < 10)
                .flatMap(x -> Observable.range(1, 9), (x, row) -> x + " * " + row + " = " + (x * row))
                .map(row -> row + "\n")
                .subscribe(printText::append));
    }
}
