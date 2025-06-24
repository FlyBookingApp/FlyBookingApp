package com.example.veyu.ui.screen.flight_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.veyu.data.repository.FlightRepository

class FlightListViewModelFactory(
    private val flightRepository: FlightRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlightListViewModel(flightRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}