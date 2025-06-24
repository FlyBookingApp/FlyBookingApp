package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Response.SeatResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SeatFlightApi {
    @GET("api/seat-flights/flight/{flightId}")
    suspend fun getSeatByFlightsId(@Path("flightId") flightId: Long): List<SeatResponse>

    @GET("api/seat-flights/{seatId}")
    suspend fun getSeatBySeatId(@Path("seatId") seatId: Long): SeatResponse

    @POST("api/seat-flights/hold-seats/{flightId}")
    suspend fun updateHoldSeats(
        @Path("flightId") flightId: Long,
        @Body seatIds: List<Long>
    ): String
}
