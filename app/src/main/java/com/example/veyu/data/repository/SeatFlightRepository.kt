package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.SeatFlightApi
import com.example.veyu.data.remote.model.Response.SeatResponse


class SeatFlightRepository(private val seatFlightApi: SeatFlightApi) {
    suspend fun getSeatByFlightsId(flightId: Long): Result<List<SeatResponse>> {
        return try {
            val result = seatFlightApi.getSeatByFlightsId(flightId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSeatBySeatId(seatId: Long): Result<SeatResponse> {
        return try {
            val result = seatFlightApi.getSeatBySeatId(seatId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateHoldSeats(flightId: Long, seatIds: List<Long>): Result<String> {
        return try {
            val result = seatFlightApi.updateHoldSeats(flightId, seatIds)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}