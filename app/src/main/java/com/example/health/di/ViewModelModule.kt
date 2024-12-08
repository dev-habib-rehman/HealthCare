package com.example.health.di

import com.example.health.data.local.dao.AppDao
import com.example.health.data.remote.apiService.NetworkService
import com.example.health.repositories.AppRepository
import com.example.health.repositories.AppRepositoryImpl
import com.example.health.ui.viewmodels.AppVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object ViewModelModule {
    @Singleton
    @Provides
    fun provideExchangeRateRepository(
        apiService: NetworkService, appDao: AppDao
    ): AppRepository = AppRepositoryImpl(appDao, apiService)

    @Singleton
    @Provides
    fun provideViewModel(repository: AppRepository): AppVM =
        AppVM(repository)
}