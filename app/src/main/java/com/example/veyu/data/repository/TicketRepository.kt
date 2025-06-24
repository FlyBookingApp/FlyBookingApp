package com.example.veyu.data.repository

import com.example.veyu.data.remote.api.TicketApi
import com.example.veyu.data.remote.model.Request.TicketRequest
import com.example.veyu.data.remote.model.Response.TicketResponse

class TicketRepository(private val ticketApi: TicketApi) {
    suspend fun getTicketById(ticketId: Long): Result<TicketResponse> {
        return try {
            val result = ticketApi.getTicketById(ticketId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTicketByBookingId(bookingId: Long): Result<List<TicketResponse>> {
        return try {
            val result = ticketApi.getTicketByBookingId(bookingId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateTicket(bookingId: Long): Result<List<TicketResponse>> {
        return try {
            val result = ticketApi.generateTicket(bookingId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}