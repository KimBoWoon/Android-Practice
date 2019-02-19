package com.bowoon.android.aac_practice_kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class DataRepository private constructor(private val database: AppDatabase) {
    private val observableMemo: MediatorLiveData<List<Memo>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val memo: LiveData<List<Memo>>
        get() = observableMemo

    init {
        observableMemo.addSource(this.database.memoDAO().loadAllMemo(), object : Observer<List<Memo>> {
            override fun onChanged(memos: List<Memo>) {
                observableMemo.postValue(memos)
            }
        })
    }

    fun addMemo(memo: Memo) {
        executors.diskIO().execute(Runnable { database.memoDAO().insert(memo) })
    }

    fun deleteMemo(memo: Memo) {
        executors.diskIO().execute(Runnable { database.memoDAO().deleteMemo(memo) })
    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}