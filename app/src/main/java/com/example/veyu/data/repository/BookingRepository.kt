package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.BookingApi
import com.example.veyu.data.remote.model.Request.BookingRequest
import com.example.veyu.data.remote.model.Response.BookingCreatedRp
import com.example.veyu.data.remote.model.Response.BookingResponse

class BookingRepository(private val api: BookingApi) {
    suspend fun createBooking(request: BookingRequest): Result<BookingCreatedRp> {
        return try {
            val result = api.createBooking(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingById(bookingId: Long): Result<BookingResponse> {
        return try {
            val result = api.getBookingById(bookingId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmBooking(bookingId: Long): Result<BookingResponse> {
        return try {
            val result = api.cofirmedBooking(bookingId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingByUserId(userId: Long): Result<List<BookingResponse>> {
        return try {
            val result = api.getBookingByUserId(userId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: String): Result<BookingResponse> {
        return try {
            val result = api.updateBookingStatus(bookingId, status)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBooking(bookingId: Long): Result<String> {
        return try {
            val result = api.deleteBooking(bookingId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}