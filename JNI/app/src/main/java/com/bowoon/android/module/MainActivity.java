package com.bowoon.android.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bowoon.android.jni.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), Constant.getAddress() + ":" + Constant.getPort(), Toast.LENGTH_SHORT).show();
    }
}
