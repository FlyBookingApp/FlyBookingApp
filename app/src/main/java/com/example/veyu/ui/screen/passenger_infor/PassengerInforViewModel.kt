package com.example.veyu.ui.screen.passenger_infor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PassengerInforViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FlightData())
    val uiState: StateFlow<FlightData> = _uiState

    // Optional: hàm cập nhật dữ liệu
    fun updateFlightData(data: FlightData) {
        _uiState.value = data
    }
    val _bookingInfo = MutableStateFlow(BookingInfo(
        contact = ContactInfo(), // để trống để nhập sau
        tickets = listOf(
            PassengerTicket(
                passenger = PassengerInfo(),
                roundTrip = RoundTrip(
                    outbound = FlightData(
                        "2025-06-20 08:00:00", "2025-06-20 10:00:00",
                        "VN123", "Vietnam Airlines", "1.200.000đ",
                        "Sài Gòn", "SGN", "Hà Nội", "HAN", "Bay thẳng"
                    ),
                    returnTrip = FlightData(
                        "2025-06-25 17:00:00", "2025-06-25 19:00:00",
                        "VN124", "Vietnam Airlines", "1.100.000đ",
                        "Hà Nội", "HAN", "Sài Gòn", "SGN", "Bay thẳng"
                    )
                )
            ),
            // Lặp lại PassengerTicket 3 lần như bạn đã làm
            PassengerTicket(
                passenger = PassengerInfo(),
                roundTrip = RoundTrip(
                    outbound = FlightData(
                        "2025-06-20 08:00:00", "2025-06-20 10:00:00",
                        "VN123", "Vietnam Airlines", "1.200.000đ",
                        "Sài Gòn", "SGN", "Hà Nội", "HAN", "Bay thẳng"
                    ),
                    returnTrip = FlightData(
                        "2025-06-25 17:00:00", "2025-06-25 19:00:00",
                        "VN124", "Vietnam Airlines", "1.100.000đ",
                        "Hà Nội", "HAN", "Sài Gòn", "SGN", "Bay thẳng"
                    )
                )
            ),
            PassengerTicket(
                passenger = PassengerInfo(),
                roundTrip = RoundTrip(
                    outbound = FlightData(
                        "2025-06-20 08:00:00", "2025-06-20 10:00:00",
                        "VN123", "Vietnam Airlines", "1.200.000đ",
                        "Sài Gòn", "SGN", "Hà Nội", "HAN", "Bay thẳng"
                    ),
                    returnTrip = FlightData(
                        "2025-06-25 17:00:00", "2025-06-25 19:00:00",
                        "VN124", "Vietnam Airlines", "1.100.000đ",
                        "Hà Nội", "HAN", "Sài Gòn", "SGN", "Bay thẳng"
                    )
                )
            ),
            PassengerTicket(
                passenger = PassengerInfo(),
                roundTrip = RoundTrip(
                    outbound = FlightData(
                        "2025-06-20 08:00:00", "2025-06-20 10:00:00",
                        "VN123", "Vietnam Airlines", "1.200.000đ",
                        "Sài Gòn", "SGN", "Hà Nội", "HAN", "Bay thẳng"
                    ),
                    returnTrip = FlightData(
                        "2025-06-25 17:00:00", "2025-06-25 19:00:00",
                        "VN124", "Vietnam Airlines", "1.100.000đ",
                        "Hà Nội", "HAN", "Sài Gòn", "SGN", "Bay thẳng"
                    )
                )
            )
        )
    )
    )
    val bookingInfo: StateFlow<BookingInfo> = _bookingInfo
    fun updatePassengerAt(index: Int, newPassenger: PassengerInfo) {
        val current = _bookingInfo.value
        val updatedTickets = current.tickets.toMutableList().apply {
            this[index] = this[index].copy(passenger = newPassenger)
        }
        _bookingInfo.value = current.copy(tickets = updatedTickets)
    }

    fun getTotalPrice(): String {
        val total = _bookingInfo.value.tickets.sumOf { ticket ->
            val outbound = ticket.roundTrip.outbound.price.orEmpty().filter(Char::isDigit).toLongOrNull() ?: 0L
            val returnTrip = ticket.roundTrip.returnTrip.price.orEmpty().filter(Char::isDigit).toLongOrNull() ?: 0L
            outbound + returnTrip
        }

        // Định dạng lại có dấu chấm phân cách nhóm 3
        return "%,d".format(total).replace(',', '.') + "đ"
    }

    //Nhập thông tin liên hệ
    fun updateContactInfo(newContact: ContactInfo) {
        val current = _bookingInfo.value
        _bookingInfo.value = current.copy(contact = newContact)
    }

}
