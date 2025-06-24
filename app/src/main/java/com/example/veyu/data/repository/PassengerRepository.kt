package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.PassengerApi
import com.example.veyu.data.remote.model.Response.PassengerResponse

class PassengerRepository(private val passengerApi: PassengerApi) {
    suspend fun getPassengerById(passengerId: Long): Result<PassengerResponse> {
        return try {
            val result = passengerApi.getPassengerById(passengerId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}