package com.example.veyu.data.remote.model.Response

data class FlightResponse(
    val id: Long,
    val flightNumber: String,
    val departureAirport: Airport,
    val arrivalAirport: Airport,
    val airline: Airline,
    val airplane: Airplane,
    val departureTime: String,
    val arrivalTime: String,
    val basePrice: Double?,
    val currentPrice: Double?,
    val status: String,
    val isFull: Boolean,
    val delayMinutes: Long,
    val flightType: String,
    val availableSeats: Int,
)

data class Airport(
    val id: Long,
    val name: String,
    val city: String,
    val iataCode: String,
)

data class Airline(
    val id: Long,
    val name: String,
    val iataCode: String,
)

data class Airplane(
    val id: Long,
    val model: String,
    val registrationNumber: String,
    val seatCapacity: Int,
)
