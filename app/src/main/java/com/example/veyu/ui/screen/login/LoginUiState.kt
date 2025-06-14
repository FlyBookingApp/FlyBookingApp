package com.example.veyu.ui.screen.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)