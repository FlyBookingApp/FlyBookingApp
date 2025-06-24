package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.AirportApi
import com.example.veyu.data.remote.model.Response.AirportResponse

class AirportRepository(private val api: AirportApi) {
    suspend fun fetchAirports(): Result<List<AirportResponse>> {
        return try {
            val response = api.getAirports()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}