package com.example.veyu.ui.screen.ticket_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.veyu.data.repository.AirportRepository

class TicketTypeViewModelFactory(
    private val repository: AirportRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketTypeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TicketTypeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}