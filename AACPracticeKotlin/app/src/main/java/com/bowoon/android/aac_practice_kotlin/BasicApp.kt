package com.bowoon.android.aac_practice_kotlin

import android.app.Application

class BasicApp : Application() {

    private fun getDatabase(): AppDatabase? {
        return AppDatabase.getInstance(this)
    }

    fun getRepository(): DataRepository? {
        return DataRepository.getInstance(getDatabase())
    }
}