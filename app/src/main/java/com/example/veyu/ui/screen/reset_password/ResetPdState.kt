package com.example.veyu.ui.screen.reset_password

data class ResetPdState(
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val tokenReset: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false
)