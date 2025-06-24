package com.example.veyu.ui.screen.passenger_infor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PassengerInfoFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PassengerInforViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PassengerInforViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}