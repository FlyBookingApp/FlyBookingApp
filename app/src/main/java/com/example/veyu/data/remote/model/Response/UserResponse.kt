package com.example.veyu.data.remote.model.Response

data class UserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phone: String,
    val role: String,
    val status: String,
    val isActive: Boolean,
    val lastLogin: String,
    val facebookAccountId: Long?,
    val googleAccountId: Long?,
    val loyaltyPoints: Int?,
    val lastSearchedRoute: String?,
    val preferredAirportId: Long?,
    val preferredSeatClass: String?,
    val preferredAirlineId: Long?,
    val createdAt: String,
    val updatedAt: String
)
