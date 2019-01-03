package com.bowoon.android.aac_practice;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * 데이터 베이스와 뷰-모델간의 데이터 전송을 위한 코드
 */
public class DataRepository {
    private static DataRepository instance;
    private final AppDatabase database;
    private MediatorLiveData<List<Memo>> observableMemo;
    private AppExecutors executors;

    private DataRepository(final AppDatabase database) {
        this.database = database;
        this.executors = new AppExecutors();
        this.observableMemo = new MediatorLiveData<>();

        observableMemo.addSource(this.database.memoDAO().loadAllMemo(), new Observer<List<Memo>>() {
                    @Override
                    public void onChanged(List<Memo> memos) {
                        observableMemo.postValue(memos);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Memo>> getMemo() {
        return observableMemo;
    }

    public void addMemo(final Memo memo) {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.memoDAO().insert(memo);
            }
        });
    }

    public void deleteMemo(final Memo memo) {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.memoDAO().deleteMemo(memo);
            }
        });
    }
}