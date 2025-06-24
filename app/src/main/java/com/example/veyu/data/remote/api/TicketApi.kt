package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.TicketRequest
import com.example.veyu.data.remote.model.Response.TicketResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApi {
    @GET("api/v1/tickets/{ticketId}")
    suspend fun getTicketById(@Path("ticketId") ticketId: Long): TicketResponse

    @GET("api/v1/tickets/booking/{bookingId}")
    suspend fun getTicketByBookingId(@Path("bookingId") bookingId: Long): List<TicketResponse>

    @POST("api/v1/tickets/booking/{bookingId}/generate")
    suspend fun generateTicket(@Path("bookingId") bookingId: Long): List<TicketResponse>
}
