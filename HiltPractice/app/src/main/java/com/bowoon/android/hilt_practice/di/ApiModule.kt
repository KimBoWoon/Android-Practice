package com.bowoon.android.hilt_practice.di

import com.bowoon.android.hilt_practice.api.ApiHelperImpl
import com.bowoon.android.hilt_practice.api.PersonApi
import com.bowoon.android.hilt_practice.api.providePersonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit() = providePersonApi()

    @Provides
    @Singleton
    fun providePersonApiImpl(personApi: PersonApi) = ApiHelperImpl(personApi)
}