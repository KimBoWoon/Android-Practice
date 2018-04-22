package com.bowoon.android.aidl_practice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AIDLService aidlService;
    private int value = 0;
    private Button start, end, check;
    private ExampleService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        end = (Button) findViewById(R.id.end);
        check = (Button) findViewById(R.id.check);

        start.setOnClickListener(listener);
        end.setOnClickListener(listener);
        check.setOnClickListener(listener);

        Log.i("className", AIDLService.class.getName());

//        new CountDownTimer(10 * 1000, 1000) {
//            @Override
//            public void onFinish() {
//                add();
//            }
//
//            @Override
//            public void onTick(long arg0) {
//                add();
//            }
//        }.start();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Connection", "onServiceConnected");
//            aidlService = AIDLService.Stub.asInterface(iBinder);
            ExampleService.LocalBinder binder = (ExampleService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("Connection", "onServiceDisconnected");
            aidlService = null;
        }
    };

    @Override
    protected void onStop() {
        unbindService(connection);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start:
                    Log.i("Bind", "Service");
                    Intent intent = new Intent("com.bowoon.android.aidl_practice.AIDLService");
                    intent.setPackage("com.bowoon.android.aidl_practice.AIDLService");
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    break;
                case R.id.end:
                    Log.i("unBind", "Service");
                    unbindService(connection);
                    break;
                case R.id.check:
                    Log.i("Check", "Service");
                        value = mService.getRandomNumber();
                        Toast.makeText(getApplicationContext(), String.valueOf(value), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
