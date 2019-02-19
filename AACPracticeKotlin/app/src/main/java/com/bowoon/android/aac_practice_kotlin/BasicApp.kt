package com.bowoon.android.aac_practice_kotlin

import android.app.Application

class BasicApp : Application() {

    private fun getDatabase(): AppDatabase {
        return AppDatabase.Singleton.getInstance(this)!!
    }

    fun getRepository(): DataRepository {
        return DataRepository.Singleton.getInstance(getDatabase())
    }
}