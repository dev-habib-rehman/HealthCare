package com.example.health.repositories

import com.example.health.data.apiHelper.ApiHelper
import com.example.health.data.apiHelper.Result
import com.example.health.data.local.dao.AppDao
import com.example.health.data.local.entities.toDomain
import com.example.health.data.remote.apiService.NetworkService
import com.example.health.data.remote.models.MedicineResponse
import com.example.health.data.remote.models.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val localDataSource: AppDao, private val remoteDataSource: NetworkService
) : AppRepository, ApiHelper() {

    override suspend fun getData(): Flow<Result<MedicineResponse>> = flow {
        val localData = getDataFromDatabase()
        if (localData != null) {
            emit(localData)
            return@flow
        }
        // If no local data, fetch from remote
        val remoteResult = getDataApiCall()
        // Handle API response and insert into DB
        if (remoteResult is Result.Success && remoteResult.data?.medicines?.isNotEmpty() == true) {
            emit(insertData(remoteResult.data!!))
            return@flow
        }
        emit(remoteResult)
    }

    override suspend fun getDataApiCall(): Result<MedicineResponse> =
        serviceCall { remoteDataSource.getMedicineDose() }.first()

    override suspend fun getDataFromDatabase(): Result<MedicineResponse>? {
        val localData = localDataSource.getAllMedicine()
        return if (localData.isEmpty().not()) {
            Result.Success(
                data = MedicineResponse(medicines = localData.map { it.toDomain() }),
                responseCode = 200
            )
        } else null // No local data found
    }

    override suspend fun insertData(remoteResult: MedicineResponse): Result<MedicineResponse> {
        localDataSource.insertMedicine(remoteResult.toEntities())
        return Result.Success(
            data = MedicineResponse(remoteResult.medicines), responseCode = 200
        )
    }
}