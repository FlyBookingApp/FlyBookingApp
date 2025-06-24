package com.example.veyu.ui.screen.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    private val userPreferences = UserPreferences(application)

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // Optional delay splash 2s
            kotlinx.coroutines.delay(2000)

            val token = userPreferences.token.first()
            val isLoggedIn = !token.isNullOrEmpty()
            _uiState.value = SplashState(
                isLoading = false,
                isLoggedIn = isLoggedIn
            )
        }
    }
}
