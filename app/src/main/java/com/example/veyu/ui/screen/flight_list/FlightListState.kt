package com.example.veyu.ui.screen.flight_list

import com.example.veyu.ui.screen.ticket_type.PassengerInfo

data class FlightInfo(
    val departTime: String,
    val arriveTime: String,
    val code: String,
    val airline: String,
    val price: String,
    val departAirport: String,
    val arriveAirport: String,
    val type: String,
    val partLogo: Int,
    val id: Long,
    val flightType: String,
    val departAirportName: String,
    val arriveAirportName: String,
    val isChoiced: Boolean = false
)

data class FlightBookingType(
    val flightIds: List<Long>? = emptyList(),
    val isRoundTrip: Boolean = false,
    val departureDate: String = "",
    val returnDate: String? = null,
    val passengerInfo: PassengerInfo = PassengerInfo()
)

enum class SortOption {
    PRICE_LOWEST,
    DEPART_EARLIEST,
    DEPART_LATEST,
    ARRIVE_EARLIEST,
    ARRIVE_LATEST,
    FLIGHT_SHORTEST
}
