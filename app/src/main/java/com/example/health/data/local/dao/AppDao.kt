package com.example.health.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.health.data.local.entities.MedicineEntity
import com.example.health.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM ${Constants.Entities.TABLE_NAME}")
    suspend fun getAllMedicine(): List<MedicineEntity>

    @Insert
    suspend fun insertMedicine(medicines : List<MedicineEntity>)
}