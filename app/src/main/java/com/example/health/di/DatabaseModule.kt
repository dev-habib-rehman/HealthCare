package com.example.health.di

import android.content.Context
import androidx.room.Room
import com.example.health.data.local.dao.AppDao
import com.example.health.data.local.database.AppDatabase
import com.example.health.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.Database.DB_NAME).build()
    }

    @Provides
    fun provideMedicineDao(db: AppDatabase): AppDao = db.medicineDao()
}