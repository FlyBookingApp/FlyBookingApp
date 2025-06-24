package com.example.veyu.ui.screen.navigation

import androidx.lifecycle.ViewModel
import com.example.veyu.domain.model.FlightSearchRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedSearchViewModel : ViewModel() {
    private val _request = MutableStateFlow<FlightSearchRequest?>(null)
    val request: StateFlow<FlightSearchRequest?> = _request

    fun setRequest(req: FlightSearchRequest) {
        _request.value = req
    }
}