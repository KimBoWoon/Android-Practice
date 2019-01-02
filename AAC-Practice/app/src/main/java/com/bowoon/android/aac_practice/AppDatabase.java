package com.bowoon.android.aac_practice;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {Memo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "aac-practice-memo";
    private static AppDatabase instance;

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

    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
//                .addCallback(new Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        AppDatabase database = AppDatabase.getInstance(appContext);
//                        insertData(database);
//                        Log.i("AppDataBase", "after insert data");
//                    }
//                })
//                .addMigrations(MIGRATION_1_2)
                .build();
    }

    private static void insertData(final AppDatabase database) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.runInTransaction(() -> {
                    database.memoDAO().insert(new Memo("First Memo"));
                    database.memoDAO().insert(new Memo("Second Memo"));
                    database.memoDAO().insert(new Memo("Third Memo"));
                    Log.i("AppDataBase", database.memoDAO().loadAllMemo().toString());
                });
            }
        }).start();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `memo` (`content` TEXT, `time` LONG)");
        }
    };
}
