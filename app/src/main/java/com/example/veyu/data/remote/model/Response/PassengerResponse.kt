package com.example.veyu.data.remote.model.Response

data class PassengerResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val idNumber: String?,
    val passportNumber: String?,
    val bookingIds: List<Long>,
    val dateOfBirth: String,
    val gender: String,
    val phone: String?,
    val createdAt: String,
    val updatedAt: String
)