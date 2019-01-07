package com.bowoon.android.aac_practice_kotlin

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Memo::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // 데이터 베이스 접근을 위한 메소드 (Data Access Object라고 불림)
    abstract fun memoDAO(): MemoDAO

    companion object {
        val DATABASE_NAME = "aac-practice-memo"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = buildDatabase(context.applicationContext)
                        insertData(AppDatabase.getInstance(context.applicationContext)!!)
//                        instance.updateDatabaseCreated(context.getApplicationContext());
                    }
                }
            }
            return instance
        }

        /**
         * 데이터 베이스 생성
         * addCallback가 작동되지 않음
         * 이유는 모르겠음
         */
        private fun buildDatabase(appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val database = AppDatabase.getInstance(appContext)
                        insertData(database!!)
                        Log.i("AppDataBase", "after insert data")
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
        }

        private fun insertData(database: AppDatabase) {
            Thread(Runnable {
                database.runInTransaction {
                    database.memoDAO().insert(Memo("First Memo"))
                    database.memoDAO().insert(Memo("Second Memo"))
                    database.memoDAO().insert(Memo("Third Memo"))
                }
            }).start()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS memo (content TEXT, time LONG)")
            }
        }
    }
}