package com.example.veyu.data.remote.model.Request

data class VerifyOPTRequest (
    val email: String,
    val otp: String,
)