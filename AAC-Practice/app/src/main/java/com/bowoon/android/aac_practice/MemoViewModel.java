package com.bowoon.android.aac_practice;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

public class MemoViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private LiveData<List<Memo>> memoList;
    private final MediatorLiveData<List<Memo>> mObservableProducts;

    public MemoViewModel(Application application) {
        super(application);
//        mRepository = ((BasicApp) application).getRepository();
//
////        memoList = ((BasicApp) application).getDatabase().memoDAO().loadAllMemo();
//        memoList = mRepository.getMemo();

        mObservableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableProducts.setValue(null);

        mRepository = ((BasicApp) application).getRepository();
        LiveData<List<Memo>> products = mRepository.getMemo();

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
    }

    public LiveData<List<Memo>> getAllMemo() {
//        return memoList;
        return mObservableProducts;
    }

    public void addMemo(Memo memo) {
        mRepository.addMemo(memo);
    }

    public void deleteAllMemo(Memo memo) {
        mRepository.deleteAllMemo(memo);
    }
}
