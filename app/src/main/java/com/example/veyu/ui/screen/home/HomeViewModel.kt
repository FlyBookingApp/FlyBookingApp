package com.example.veyu.ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
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
    fun onAccountClick(){
        _uiState.value = _uiState.value.copy(
            isAccessSelsected = true
        )
    }
    fun resetAccountFlag() {
        _uiState.update { it.copy(isAccessSelsected = false) }
    }
    fun onMyTicketClick(){
        _uiState.value = _uiState.value.copy(
            isMyTicketSelected = true
        )
    }
    fun resetMyTicketFlag() {
        _uiState.update { it.copy(isMyTicketSelected = false) }
    }
}