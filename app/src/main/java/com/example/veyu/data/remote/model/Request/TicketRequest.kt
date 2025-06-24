package com.example.veyu.data.remote.model.Request

data class TicketRequest (
    val bookingId: Long,
    val flightId: Long,
    val passengerId: Long,
    val seatId: Long,
    val ticketPrice: Double,
    val ticketType: String,
    val ticketClass: String,
    val legNumber: Int,
    val relatedTicketId: Long?,
    val status: String
)