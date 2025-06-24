package com.example.veyu.domain.model

data class TicketRequest(
    val bookingId: Long,
    val flightId: Long,
    val passengerId: Long,
    val seatId: Long,
    val ticketPrice: String,
    val ticketType: String,
    val ticketClass: String,
    val legNumber: Long,
    val relatedTicketId: Long,
    val status: String
)