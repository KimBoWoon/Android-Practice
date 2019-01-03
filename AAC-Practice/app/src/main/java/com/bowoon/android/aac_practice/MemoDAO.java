package com.bowoon.android.aac_practice;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MemoDAO {
    @Query("SELECT * FROM memo")
    LiveData<List<Memo>> loadAllMemo();

    @Insert
    void insert(Memo memo);

    @Delete
    void deleteMemo(Memo memo);
}
