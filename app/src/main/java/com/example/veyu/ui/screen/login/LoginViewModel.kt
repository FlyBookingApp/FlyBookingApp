package com.example.veyu.ui.screen.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onLoginClick() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (!email.contains("@")) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email không hợp lệ. Vui lòng kiểm tra lại.")
            return
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "",
                isLoggedIn = true
            )
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Email hoặc mật khẩu không đúng.")
        }
    }
}