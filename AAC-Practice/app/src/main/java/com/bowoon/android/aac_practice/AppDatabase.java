package com.bowoon.android.aac_practice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * 데이터 베이스 초기화
 */
@Database(entities = {Memo.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "aac-practice-memo";
    private static AppDatabase instance;

    // 데이터 베이스 접근을 위한 메소드 (Data Access Object라고 불림)
    public abstract MemoDAO memoDAO();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                    insertData(AppDatabase.getInstance(context.getApplicationContext()));
//                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 데이터 베이스 생성
     * addCallback가 작동되지 않음
     * 이유는 모르겠음
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        AppDatabase database = AppDatabase.getInstance(appContext);
                        insertData(database);
                        Log.i("AppDataBase", "after insert data");
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    private static void insertData(final AppDatabase database) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        database.memoDAO().insert(new Memo("First Memo"));
                        database.memoDAO().insert(new Memo("Second Memo"));
                        database.memoDAO().insert(new Memo("Third Memo"));
                        Log.i("AppDataBase", database.memoDAO().loadAllMemo().toString());
                    }
                });
            }
        }).start();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS memo (content TEXT, time LONG)");
        }
    };
}
