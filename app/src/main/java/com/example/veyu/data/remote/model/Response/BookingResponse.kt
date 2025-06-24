package com.example.veyu.data.remote.model.Response

data class BookingResponse (
    val id: Long,
    val bookingReference: String,
    val userId: Long,
    val userFullName: String,
    val status: String,
    val totalAmount: Double,
    val passengerIds: List<Long>,
    val flightIds: List<Long>,
    val seatFlights: List<seatFlight>,
    val bookingDate: String,
    val passengerCount: Int,
    val tripType: String,
    val bookingSource: String?,
    val promotionCode: String?,
    val cancellationReason: String?,
    val createdAt: String,
    val updatedAt: String,
    val note: String?
)

data class seatFlight(
    val id: Long,
    val flightId: Long?,
    val flightNo: String,
    val seatNumber: String,
    val seatType: String,
    val status: String,
)