package com.bowoon.android.aac_practice;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * UI 관련 데이터를 관리하는 클래스
 */
public class MemoViewModel extends AndroidViewModel {
    private final DataRepository repository;
    private final MediatorLiveData<List<Memo>> observableMemo;

    public MemoViewModel(Application application) {
        super(application);

        observableMemo = new MediatorLiveData<>();
        observableMemo.setValue(null);

        repository = ((BasicApp) application).getRepository();
        LiveData<List<Memo>> memo = repository.getMemo();

        observableMemo.addSource(memo, new Observer<List<Memo>>() {
            @Override
            public void onChanged(List<Memo> memos) {
                observableMemo.setValue(memos);
            }
        });
    }

    public LiveData<List<Memo>> getAllMemo() {
        return observableMemo;
    }

    public void addMemo(Memo memo) {
        repository.addMemo(memo);
    }

    public void deleteMemo(Memo memo) {
        repository.deleteMemo(memo);
    }
}
