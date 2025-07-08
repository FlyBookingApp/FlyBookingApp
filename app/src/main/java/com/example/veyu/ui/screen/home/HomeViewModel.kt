package com.example.veyu.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    fun onTicketTypeClick(){
        _uiState.value = _uiState.value.copy(
            isTicketTypeSelected = true
        )
    }
    fun resetTicketTypeFlag() {
        _uiState.update { it.copy(isTicketTypeSelected = false) }
    }

    fun onAccountClick() {
        viewModelScope.launch {
            val token = userPreferences.token.first()
            val isLoggedIn = !token.isNullOrEmpty()

            _uiState.value = _uiState.value.copy(
                isLogin = isLoggedIn,
                isAccessSelsected = isLoggedIn
            )
        }
    }

    fun resetIsLogin() {
        _uiState.update { it.copy(isLogin = true) }
    }

    fun resetAccountFlag() {
        _uiState.update { it.copy(isAccessSelsected = false) }
    }

    fun onMyTicketClick(){
        viewModelScope.launch {
            val token = userPreferences.token.first()
            val isLoggedIn = !token.isNullOrEmpty()

            _uiState.value = _uiState.value.copy(
                isLogin = isLoggedIn,
                isMyTicketSelected = isLoggedIn
            )
        }
    }

    fun resetMyTicketFlag() {
        _uiState.update { it.copy(isMyTicketSelected = false) }
    }
}