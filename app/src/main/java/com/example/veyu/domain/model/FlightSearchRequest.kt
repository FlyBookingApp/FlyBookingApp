package com.example.veyu.domain.model

import com.example.veyu.ui.screen.ticket_type.PassengerInfo

data class FlightSearchRequest(
    val isRoundTrip: Boolean,
    val departureCode: String,
    val destinationCode: String,
    val departureDate: String,
    val returnDate: String,
    val passengerInfo: PassengerInfo,
)