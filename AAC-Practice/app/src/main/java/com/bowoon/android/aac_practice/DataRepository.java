package com.bowoon.android.aac_practice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;
    private final AppDatabase mDatabase;
    private MediatorLiveData<List<Memo>> mObservableProducts;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableProducts = new MediatorLiveData<>();

        mObservableProducts.addSource(mDatabase.memoDAO().loadAllMemo(),
                productEntities -> {
                    mObservableProducts.postValue(productEntities);
//                    if (mDatabase.getDatabaseCreated().getValue() != null) {
//                        mObservableProducts.postValue(productEntities);
//                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<Memo>> getMemo() {
        return mObservableProducts;
    }

    public void addMemo(Memo memo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.memoDAO().insert(memo);
            }
        }).start();
    }

    public void deleteAllMemo(Memo memo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDatabase.memoDAO().deleteAllMemo(memo);
            }
        }).start();
    }
}