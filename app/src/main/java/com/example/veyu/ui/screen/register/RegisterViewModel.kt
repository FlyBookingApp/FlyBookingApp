package com.example.veyu.ui.screen.register

import androidx.lifecycle.ViewModel
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.remote.model.Request.RegisterRequest
import com.example.veyu.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.String

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository(NetworkModule.authApi)
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun onFullNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = password)
    }

    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone)
    }

    fun onRegisterClick() {
        val state = _uiState.value

        // Kiểm tra dữ liệu đầu vào
        if (state.email.isBlank() || state.password.isBlank() || state.phone.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Vui lòng điền đầy đủ thông tin.")
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Mật khẩu không khớp.")
            return
        }


        val request = RegisterRequest(
            username = state.email,
            email = state.email,
            password = state.password,
            confirmPassword = state.confirmPassword,
            phone = state.phone
        )

        // Gọi API
        _uiState.value = state.copy(errorMessage = "")
        kotlinx.coroutines.GlobalScope.launch {
            val result = repository.register(request)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isRegistered = true,
                    errorMessage = "Đăng ký thành công!",
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Đăng ký thất bại.")
            }
        }
    }
}
