package com.example.veyu.ui.screen.splash

import android.app.Application
import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.remote.model.Response.UserResponse
import com.example.veyu.data.repository.AuthRepository
import com.example.veyu.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    private val userPreferences = UserPreferences(application)
    private val userRepository = UserRepository(application)
    private val authRepository = AuthRepository(NetworkModule.authApi)

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)

            val token = userPreferences.token.first()
            val username = userPreferences.userName.first()

            Log.d("SplashViewModel", "tokenOld: $token")

            if (token.isNullOrEmpty() || username.isNullOrEmpty()) {
                _uiState.value = SplashState(isLoading = false, isLoggedIn = false)
                return@launch
            }

            Log.d("SplashViewModel", "token: $username")
            val result = tryGetUser(username)

            _uiState.value = SplashState(
                isLoading = false,
                isLoggedIn = result.isSuccess
            )
        }
    }

    private suspend fun tryGetUser(username: String): Result<UserResponse> {
        var result = userRepository.getUserByUsername(username)

        Log.d("SplashViewModel", "result: $result")

        if (result.isSuccess) return result

        val exception = result.exceptionOrNull()
        if (exception is HttpException && exception.code() == 403) {
            val refreshToken = userPreferences.refreshToken.first()
            val refreshResult = authRepository.refresh(refreshToken ?: "")
            Log.d("SplashViewModel", "refreshResult: $refreshResult")

            if (refreshResult.isSuccess) {
                val newAccessToken = refreshResult.getOrNull()
                userPreferences.updateToken(newAccessToken ?: "")
                result = userRepository.getUserByUsername(username)

                Log.d("SplashViewModel", "newAccessToken: $newAccessToken")
            }

            val exception = refreshResult.exceptionOrNull()
            if (exception is HttpException && exception.code() == 400) {
                Log.d("SplashViewModel", "Refresh token bị từ chối với HTTP 400")

                userPreferences.clearToken()
            }
        }

        return result
    }
}
