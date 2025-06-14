package com.example.veyu.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    init {
        simulateLoading()
    }

    private fun simulateLoading() {
        viewModelScope.launch {
            delay(2000) // Splash delay 2 giây
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
