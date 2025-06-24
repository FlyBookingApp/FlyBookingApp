package com.example.veyu.data.remote.model.Request

import com.google.gson.annotations.SerializedName

data class BookingRequest(
    @SerializedName("userId") val userId: Long,
    @SerializedName("passengerCount") val passengerCount: Int,
    @SerializedName("status") val status: String = "PENDING",           // Cho phép status nullable
    @SerializedName("tripType") val tripType: String,
    @SerializedName("passengers") val passengers: List<PassengerRequest>,
    @SerializedName("seatFlightIds") val seatFlightIds: List<Long>,
    @SerializedName("flightIds") val flightIds: List<Long>,
    @SerializedName("bookingDate") val bookingDate: String? = "2025-06-22",
    @SerializedName("bookingSource") val bookingSource: String? = null,
    @SerializedName("promotionCode") val promotionCode: String? = null,
    @SerializedName("cancellationReason") val cancellationReason: String? = null,
    @SerializedName("note") val note: String,
)