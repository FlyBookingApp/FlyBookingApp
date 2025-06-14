package com.example.veyu.ui.screen.register

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val errorMessage: String = "",
    val isRegistered: Boolean = false
)
