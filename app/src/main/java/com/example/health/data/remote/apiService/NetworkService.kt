package com.example.health.data.remote.apiService

import com.example.health.data.remote.models.MedicineResponse
import com.example.health.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET(Constants.ApiEndpoints.MOCKY_ENDPOINT)
    suspend fun getMedicineDose(): Response<MedicineResponse>
}