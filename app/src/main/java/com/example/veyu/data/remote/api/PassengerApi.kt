package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Response.PassengerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PassengerApi {
    @GET("api/v1/passengers/{id}")
    suspend fun getPassengerById(@Path("id") passengerId: Long): PassengerResponse
}