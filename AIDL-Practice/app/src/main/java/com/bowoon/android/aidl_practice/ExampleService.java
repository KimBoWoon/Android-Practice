package com.bowoon.android.aidl_practice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class ExampleService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final java.util.Random mGenerator = new java.util.Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends android.os.Binder {
        ExampleService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ExampleService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method for clients
     */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }
}
