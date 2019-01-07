package com.bowoon.android.aac_practice_kotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var observableMemo: MediatorLiveData<List<Memo>> = MediatorLiveData()

    init {
        observableMemo.value = null
        repository = (application as BasicApp).getRepository()!!
        val memo = repository.memo
        observableMemo.addSource(memo, object : Observer<List<Memo>> {
            override fun onChanged(memos: List<Memo>) {
                observableMemo.value = memos
            }
        })
    }

    fun getAllMemo(): LiveData<List<Memo>> {
        return observableMemo
    }

    fun addMemo(memo: Memo) {
        repository.addMemo(memo)
    }

    fun deleteMemo(memo: Memo) {
        repository.deleteMemo(memo)
    }
}