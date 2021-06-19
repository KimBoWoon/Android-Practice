package com.bowoon.android.hilt_practice.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GlobalModule {
    @Provides
    @Singleton
    fun provideGson() = Gson()
}