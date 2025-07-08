package com.example.veyu.ui.screen.reset_password

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.data.remote.model.Request.ResetPasswordRequest
import com.example.veyu.data.remote.model.Request.VerifyOPTRequest
import com.example.veyu.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    init {
        Log.d("NewPasswordViewModel", "NewPasswordViewModel created")
    }

    private var _uiState = MutableStateFlow(ResetPdState())
    val uiState: StateFlow<ResetPdState> = _uiState

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(passwordConfirmation = newConfirmPassword)
    }

    fun onSendOTP(onSuccess: () -> Unit) {
        val email = _uiState.value.email.trim()

        Log.d("NewPasswordViewModel", "VerifiEmail: ${_uiState.value.toString()}")
        if (!email.contains("@")) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email không hợp lệ. Vui lòng kiểm tra lại.")
            return
        }

        if (email.isNotEmpty()) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
                val result = repository.sendOtp(email)
                Log.d("NewPasswordViewModel", "onSendOTP: $result")
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "")
                    onSuccess()
                }
                result.onFailure {
                    _uiState.value = _uiState.value.copy(errorMessage = "Gửi OTP thất bại.")
                }
            }
        }
    }


    fun VerifiEmail(otp: String, onSuccess: () -> Unit) {
        if (otp.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = true, otp = otp)

            Log.d("NewPasswordViewModel", "VerifiEmail: ${_uiState.value.toString()}")
            viewModelScope.launch {
                val result = repository.verifyOtp(
                    VerifyOPTRequest(
                        email = uiState.value.email,
                        otp = otp
                    )
                )

                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tokenReset = it
                    )
                    onSuccess()
                }

                result.onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tokenReset = "",
                        errorMessage = "OTP không chính xác hoặc hết hạn."
                    )
                }
            }
        }
    }


    fun onResetPassword(context: Context, onSuccess: () -> Unit) {
        val password = _uiState.value.password.trim()
        val passwordConfirmation = _uiState.value.passwordConfirmation.trim()

        if (password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {
            if (password != passwordConfirmation) {
                _uiState.value =
                    _uiState.value.copy(errorMessage = "Mật khẩu nhập lại không chùng nhau.")
                return
            }

            Log.d("NewPasswordViewModel", "VerifiEmail: ${_uiState.value.toString()}")

            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
                val result = repository.resetPassword(
                    ResetPasswordRequest(
                        resetToken = uiState.value.tokenReset,
                        newPassword = password,
                    )
                )

                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = ""
                    )
                    Toast.makeText(context, "Reset mật khẩu thành công.", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }

                result.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = ""
                    )
                    Toast.makeText(context, "Đặt lại mật khẩu không thành công.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}