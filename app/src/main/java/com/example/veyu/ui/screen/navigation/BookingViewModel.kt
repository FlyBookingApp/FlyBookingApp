package com.example.veyu.ui.screen.navigation

import androidx.lifecycle.ViewModel
import com.example.veyu.domain.model.Booking
import com.example.veyu.domain.model.BookingRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookingViewModel : ViewModel() {
    private val _request = MutableStateFlow<BookingRequest?>(null)
    val request: StateFlow<BookingRequest?> = _request

    private val _requestSeat = MutableStateFlow<Booking?>(null)
    val requestSeat: StateFlow<Booking?> = _requestSeat

    private val _bookingId = MutableStateFlow<Long?>(null)
    val bookingId: StateFlow<Long?> = _bookingId

    fun setRequest(req: BookingRequest) {
        _request.value = req
    }

    fun seatRequestSeat(req: Booking) {
        _requestSeat.value = req
    }

    fun bookingIdRequest(id: Long) {
        _bookingId.value = id
    }
}