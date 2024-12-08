package com.example.health.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.health.data.local.dao.AppDao
import com.example.health.data.local.entities.MedicineEntity

@Database(entities = [MedicineEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): AppDao
}