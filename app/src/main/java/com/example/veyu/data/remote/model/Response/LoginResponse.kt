package com.example.veyu.data.remote.model.Response

data class LoginResponse(
    val token: String,
    val refreshToken: String,
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
    val createdAt: String?,
    val updatedAt: String?
)