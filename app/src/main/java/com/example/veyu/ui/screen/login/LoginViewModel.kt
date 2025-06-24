package com.example.veyu.ui.screen.login

import androidx.lifecycle.ViewModel
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.model.Request.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(NetworkModule.authApi) ,
    private val userPreferences: UserPreferences
) : ViewModel() {
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
            _uiState.value =
                _uiState.value.copy(errorMessage = "Email không hợp lệ. Vui lòng kiểm tra lại.")
            return
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Gọi API
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
                val result = repository.login(LoginRequest(email, password))
                result.onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                    )
                    userPreferences.saveToken(response.refreshToken, email) // Lưu token vào DataStore
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Lỗi không xác định"
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Email hoặc mật khẩu không đúng.")
        }
    }
}