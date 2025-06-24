package com.example.veyu.data.remote.model.Response

data class AirportResponse(
    val id: Long,
    val name: String,
    val city: String,
    val state: String,
    val country: String,
    val iataCode: String,
    val icaoCode: String,
    val createdAt: String,
    val updatedAt: String
)