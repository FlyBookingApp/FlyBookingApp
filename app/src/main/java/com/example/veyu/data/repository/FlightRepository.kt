package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.FlightApi
import com.example.veyu.data.remote.model.Response.FlightResponse

class FlightRepository(private val flightApi: FlightApi) {

    suspend fun searchFlights(
        departure: String,
        arrival: String,
        date: String
    ): Result<List<FlightResponse>> {
        return try {
            val result = flightApi.searchFlights(departure, arrival, date)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFlightById(id: Long): Result<FlightResponse> {
        return try {
            val result = flightApi.getFlightById(id)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}