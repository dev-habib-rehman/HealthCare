package com.example.health.repositories

import com.example.health.data.apiHelper.Result
import com.example.health.data.remote.models.MedicineResponse
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getData(): Flow<Result<MedicineResponse>>

    suspend fun getDataApiCall(): Result<MedicineResponse>

    suspend fun getDataFromDatabase(): Result<MedicineResponse>?

    suspend fun insertData(remoteResult: MedicineResponse): Result<MedicineResponse>
}