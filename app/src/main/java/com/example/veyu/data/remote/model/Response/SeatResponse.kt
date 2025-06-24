package com.example.veyu.data.remote.model.Response

import com.google.gson.annotations.SerializedName

data class SeatResponse (
    @SerializedName("id") val seatId: Long,
    @SerializedName("flightId") val flightId: Long,
    @SerializedName("flightNo") val flightNo: String,
    @SerializedName("seatNumber") val seatNumber: String,
    @SerializedName("seatType") val seatType: String,
    @SerializedName("seatPosition") val seatPosition: String,
    @SerializedName("price") val price: Double,
    @SerializedName("dateHold") val dateHold: String?,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)