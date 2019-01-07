package com.bowoon.android.aac_practice_kotlin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MemoDAO {
    @Query("SELECT * FROM memo")
    fun loadAllMemo(): LiveData<List<Memo>>

    @Insert
    fun insert(memo: Memo)

    @Delete
    fun deleteMemo(memo: Memo)
}