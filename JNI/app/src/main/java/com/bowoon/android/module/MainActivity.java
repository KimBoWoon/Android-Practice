package com.bowoon.android.module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bowoon.android.jni.Module;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), String.valueOf(Module.add(10, 10)), Toast.LENGTH_SHORT).show();
    }
}
