package com.bowoon.android.aac_practice;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MemoDAO {
    @Query("SELECT * FROM memo")
    LiveData<List<Memo>> loadAllMemo();

    @Insert
    void insert(Memo memo);

    @Delete
    void deleteAllMemo(Memo memo);
}
