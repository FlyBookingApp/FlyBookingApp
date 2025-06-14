package com.example.veyu.ui.screen.flight_list

data class FlightInfo(
    val departTime: String,
    val arriveTime: String,
    val code: String,
    val airline: String,
    val price: String,
    val departAirport: String ="HEHE",
    val arriveAirport: String = "HUHU",
    val type: String
)
enum class SortOption {
    PRICE_LOWEST,
    DEPART_EARLIEST,
    DEPART_LATEST,
    ARRIVE_EARLIEST,
    ARRIVE_LATEST,
    FLIGHT_SHORTEST
}
