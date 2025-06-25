package com.example.veyu.data.remote.model.Response

data class TicketResponse(
    val id: Long,
    val ticketNumber: String,
    val bookingId: Long,
    val flightId: Long,
    val passengerId: Long,
    val seatFlightId: Long,
    val status: String,
    val price: Double,
    val issueDate: String,
    val cancelDate: String?,
    val barcode: String?,
    val ticketType: String,
    val ticketClass: String,
    val legNumber: Int,
    val relatedTicketId: Long?,
    val createdAt: String,
    val updatedAt: String
)