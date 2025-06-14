package com.example.veyu.ui.screen.ticket_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketTypeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TicketTypeState())
    val uiState: StateFlow<TicketTypeState> = _uiState
    init {
        loadAirportLocations()
        loadAirportLocationId()
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
            val listFromDb = listOf("Hà Nội", "Đà Nẵng", "TP.HCM", "Huế", "Nha Trang", "Buôn Mê Thuộc", "Cần Thơ", "Phú Quốc", "Vũng Tàu", "Cà Mau")
            _uiState.update { it.copy(airportLocations = listFromDb) }
        }
    }
    fun loadAirportLocationId(){
        viewModelScope.launch {
            val listFromDb = listOf("HAN", "DN", "SGN", "HUI", "CXR", "BMT", "CTH", "PQC", "VTS", "CAH")
            _uiState.update { it.copy(airportLocationsId = listFromDb) }
        }
    }

    fun isFlightList(isFlightList: Boolean) {
        _uiState.value = _uiState.value.copy(isFlightList = isFlightList)
    }
    fun setPassengerInfo(passengerInfo: PassengerInfo) {
        _uiState.value = _uiState.value.copy(passengerInfo = passengerInfo)
    }
}