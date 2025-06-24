package com.example.veyu.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.veyu.data.remote.model.Response.FlightResponse
import retrofit2.http.Path


interface FlightApi {

    @GET("api/v1/flights/search")
    suspend fun searchFlights(
        @Query("departureAirport") departure: String,
        @Query("arrivalAirport") arrival: String,
        @Query("departureDate") date: String
    ): List<FlightResponse>

    @GET("api/v1/flights/{id}")
    suspend fun getFlightById(
        @Path("id") id: Long
    ): FlightResponse
}