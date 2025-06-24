package com.example.veyu.data.remote.model.Response

data class BookingCreatedRp (
    val id: Long,
    val bookingReference: String,
    val status: String,
    val totalPrice: Double,
    val message: String
)