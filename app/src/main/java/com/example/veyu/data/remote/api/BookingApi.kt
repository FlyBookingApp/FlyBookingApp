package com.example.veyu.data.remote.api

import com.example.veyu.data.remote.model.Request.BookingRequest
import com.example.veyu.data.remote.model.Response.BookingCreatedRp
import com.example.veyu.data.remote.model.Response.BookingResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingApi {
    @POST("api/v1/bookings/with-passengers/ultra-simple")
    suspend fun createBooking(@Body request: BookingRequest): BookingCreatedRp

    @GET("api/v1/bookings/{bookingId}")
    suspend fun getBookingById(@Path("bookingId") bookingId: Long): BookingResponse

    @GET("api/v1/bookings/user/{userId}")
    suspend fun getBookingByUserId(@Path("userId") userId: Long): List<BookingResponse>

    @PUT("api/v1/bookings/{id}/confirm")
    suspend fun cofirmedBooking(@Path("id") id: Long): BookingResponse

    @PUT("api/v1/bookings/{bookingId}/status")
    suspend fun updateBookingStatus(
        @Path("bookingId") bookingId: String,
        @Query("status") status: String
    ): BookingResponse
    
    @DELETE("api/v1/bookings/{bookingId}")
    suspend fun deleteBooking(@Path("bookingId") bookingId: Long): String
}