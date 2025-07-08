package com.example.veyu.data.remote.model.Request

data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String
)