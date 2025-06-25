package com.example.veyu.ui.screen.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.repository.TicketRepository
import com.example.veyu.ui.screen.seat.Seat
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _departureTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val departureTickets: StateFlow<List<Ticket>> = _departureTickets

    private val _returnTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val returnTickets: StateFlow<List<Ticket>> = _returnTickets

    fun init(bookingId: Long) {
        _departureTickets.value = emptyList()
        _returnTickets.value = emptyList()

        viewModelScope.launch {
            val ticketListResult =  ticketRepository.getTicketByBookingId(bookingId)
            Log.d("TicketViewModel", "departureTickets: ${_departureTickets.value.toString()}")
            val ticketListResponse = ticketListResult.getOrNull() ?: return@launch

            _departureTickets.value = ticketListResponse
                .filter { it.legNumber == 1 }
                .map {
                    Ticket(
                        id = it.id,
                        ticketNumber = it.ticketNumber,
                        bookingId = it.bookingId,
                        flightId = it.flightId,
                        passengerId = it.passengerId,
                        seatFlightId = it.seatFlightId,
                        status = it.status,
                        price = formatPrice(it.price),
                        issueDate = it.issueDate,
                        barcode = it.barcode,
                        ticketType = it.ticketType,
                        ticketClass = it.ticketClass,
                        legNumber = it.legNumber,
                        relatedTicketId = it.relatedTicketId,
                        createdAt = convertIsoToReadable(it.createdAt),
                        updatedAt = convertIsoToReadable(it.updatedAt)
                    )
                }

            _returnTickets.value = ticketListResponse
                .filter { it.legNumber == 2 }
                .map {
                    Ticket(
                        id = it.id,
                        ticketNumber = it.ticketNumber,
                        bookingId = it.bookingId,
                        flightId = it.flightId,
                        passengerId = it.passengerId,
                        seatFlightId = it.seatFlightId,
                        status = it.status,
                        price = formatPrice(it.price),
                        issueDate = it.issueDate,
                        barcode = it.barcode,
                        ticketType = it.ticketType,
                        ticketClass = it.ticketClass,
                        legNumber = it.legNumber,
                        relatedTicketId = it.relatedTicketId,
                        createdAt = convertIsoToReadable(it.createdAt),
                        updatedAt = convertIsoToReadable(it.updatedAt)
                    )
                }
        }
    }
}

fun formatPrice(price: Double?): String {
    // Nếu price null thì trả về "0đ"
    if (price == null) return "0đ"
    // Chuyển Double sang Long để format kiểu số nguyên
    val longPrice = price.toLong()
    return "%,dđ".format(longPrice).replace(",", ".")
}

fun convertIsoToReadable(input: String): String {
    return try {
        val trimmedInput = input.substringBeforeLast(".")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(trimmedInput) ?: return "Invalid date"
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}