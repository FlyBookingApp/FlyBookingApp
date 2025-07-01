package com.example.veyu.ui.screen.ticket_type

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.remote.model.Response.AirportResponse
import com.example.veyu.data.repository.AirportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TicketTypeViewModel @Inject constructor(
    private val repository: AirportRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketTypeState())
    val uiState: StateFlow<TicketTypeState> = _uiState

    private val _airports = MutableStateFlow<List<AirportResponse>>(emptyList())
    val airports: StateFlow<List<AirportResponse>> = _airports

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {

        loadAirports()
    }

    fun onToggleTripType(isRoundTrip: Boolean) {
        _uiState.value = _uiState.value.copy(isRoundTrip = isRoundTrip)
    }

    fun onSelectDeparture(value: String) {
        _uiState.value = _uiState.value.copy(selectedDeparture = value)
    }

    fun onSelectDestination(value: String) {
        _uiState.value = _uiState.value.copy(selectedDestination = value)
    }

    fun onSelectDepartureDate(date: String) {
        _uiState.value = _uiState.value.copy(departureDate = date)
    }

    fun onSelectReturnDate(date: String) {
        _uiState.value = _uiState.value.copy(returnDate = date)
    }
    /*fun onDataPickerClick(isReturn: Boolean) {
        _uiState.value = _uiState.value.copy(isReturnPicker = isReturn)
    }*/
    fun showDatePicker(isReturn: Boolean) {
        _uiState.value = _uiState.value.copy(showDatePicker = true, isReturnPicker = isReturn)
    }
    fun hideDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePicker = false)
    }
    fun loadAirportLocations() {
        viewModelScope.launch {
            val cityList = _airports.value.mapNotNull { it.city }
            android.util.Log.d("TicketTypeViewModel", "Số lượng cityList: ${cityList.size}")
            _uiState.update { it.copy(airportLocations = cityList) }
        }
    }
    fun loadAirportLocationId(){
        viewModelScope.launch {
            val iataList = _airports.value.mapNotNull { it.iataCode }
            _uiState.update { it.copy(airportLocationsId = iataList) }
        }
    }

    fun isFlightList(isFlightList: Boolean) {
        _uiState.value = _uiState.value.copy(isFlightList = isFlightList)
    }
    fun setPassengerInfo(passengerInfo: PassengerInfo) {
        _uiState.value = _uiState.value.copy(passengerInfo = passengerInfo)
    }

    fun loadAirports() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchAirports()
                .onSuccess { _airports.value = it
                    android.util.Log.d("TicketTypeViewModel", "Số lượng sân bay: ${it.size}")
                    loadAirportLocations()
                    loadAirportLocationId()
                    _isLoading.value = false
                }
                .onFailure { _error.value = it.message
                    android.util.Log.e("TicketTypeViewModel", "Lỗi gọi API sân bay: ${it.message}")
                }
        }
    }
}