package com.example.veyu.data.remote.model.Request

import com.google.gson.annotations.SerializedName

data class PassengerRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("personalId") val personalId: String,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("passportNumber") val passportNumber: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?
)