package com.bowoon.android.aac_practice_kotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "memo")
data class Memo(
    @ColumnInfo(name = "content")
    var content: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "time")
    var time: DateTime = DateTime(System.currentTimeMillis())
)