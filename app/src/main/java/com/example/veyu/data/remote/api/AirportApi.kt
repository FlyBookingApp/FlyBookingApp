package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Response.AirportResponse
import retrofit2.http.GET

interface AirportApi {
    @GET("api/v1/airports/")
    suspend fun getAirports(): List<AirportResponse>
}