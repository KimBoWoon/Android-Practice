package com.bowoon.android.aidl_practice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class ExampleService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("on", "Bind");
        return new AIDLExample();
    }

    private class AIDLExample extends AIDLService.Stub {
        @Override
        public int add(int x, int y) throws RemoteException {
            return x + y;
        }
    }
}
