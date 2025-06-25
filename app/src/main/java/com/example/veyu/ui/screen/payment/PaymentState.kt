package com.example.veyu.ui.screen.payment

data class Ticket(
    val id: Long,
    val ticketNumber: String,
    val bookingId: Long,
    val flightId: Long,
    val passengerId: Long,
    val seatFlightId: Long,
    val seatFlightNumber: String = "",
    val status: String,
    val price: String,
    val issueDate: String,
    val barcode: String?,
    val ticketType: String,
    val ticketClass: String,
    val legNumber: Int,
    val relatedTicketId: Long?,
    val createdAt: String,
    val updatedAt: String
)