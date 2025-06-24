package com.example.veyu.ui.screen.passenger_infor

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.veyu.domain.model.BookingRequest
import com.example.veyu.ui.screen.flight_list.FlightBookingType
import com.example.veyu.ui.screen.flight_list.FlightInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class PassengerInforViewModel @Inject constructor(): ViewModel() {
    var isRoundTrip: Boolean = false

    var flightBookingType: FlightBookingType = FlightBookingType()

    private val _uiState = MutableStateFlow(FlightData())
    val uiState: StateFlow<FlightData> = _uiState

    private val _isInitFist = MutableStateFlow(true)
    val isInitFirst: StateFlow<Boolean> = _isInitFist

    fun updateIsInitFirst(value: Boolean) {
        _isInitFist.value = value
    }

    // Optional: hàm cập nhật dữ liệu
    fun updateFlightData(data: FlightData) {
        _uiState.value = data
    }

    private val _bookingInfo = MutableStateFlow(
        BookingInfo(
            contact = ContactInfo(),
            tickets = emptyList()
        )
    )
    val bookingInfo: StateFlow<BookingInfo> = _bookingInfo

    fun initBookingInfo(request: BookingRequest) {
        val passengerInfo = request.flightBookingType.passengerInfo
        val passengerTicket = updatePassengerTicket(request)

        val totalTickets = passengerInfo.adults + passengerInfo.children + passengerInfo.infants

        _bookingInfo.value = BookingInfo(
            contact = ContactInfo(), // để trống để nhập sau
            tickets = List(totalTickets) {
                passengerTicket.copy(passenger = PassengerInfo())
            }
        )
    }

    fun updatePassengerTicket(request: BookingRequest) : PassengerTicket {
        flightBookingType = request.flightBookingType
        isRoundTrip = request.flightBookingType.isRoundTrip

        return PassengerTicket(
            passenger = PassengerInfo(),
            roundTrip = RoundTrip(
                outbound = FlightData(
                    departTime = request.outbound.departTime,
                    arriveTime = request.outbound.arriveTime,
                    code = request.outbound.code,
                    airline = request.outbound.airline,
                    price = request.outbound.price,
                    departAirport = request.outbound.departAirportName,
                    departAirportId = request.outbound.departAirport,
                    arriveAirport = request.outbound.arriveAirportName,
                    arriveAirportId = request.outbound.arriveAirport,
                    type = request.outbound.type,
                    partLogo = request.outbound.partLogo,
                    id = request.outbound.id,
                    flightType = request.outbound.flightType
                ),
                returnTrip = if (isRoundTrip) {
                    FlightData(
                        departTime = request.returnTrip?.departTime,
                        arriveTime = request.returnTrip?.arriveTime,
                        code = request.returnTrip?.code,
                        airline = request.returnTrip?.airline,
                        price = request.returnTrip?.price,
                        departAirport = request.returnTrip?.departAirportName,
                        departAirportId = request.returnTrip?.departAirport,
                        arriveAirport = request.returnTrip?.arriveAirportName,
                        arriveAirportId = request.returnTrip?.arriveAirport,
                        type = request.returnTrip?.type,
                        partLogo = request.returnTrip?.partLogo,
                        id = request.returnTrip?.id,
                        flightType = request.returnTrip?.flightType.toString()
                    )
                } else null
            )
        )
    }

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
            val returnTrip = ticket.roundTrip.returnTrip?.price.orEmpty().filter(Char::isDigit).toLongOrNull() ?: 0L
            outbound + returnTrip
        }

        return String.format(java.util.Locale.US, "%,d", total) + "đ"
    }

    //Nhập thông tin liên hệ
    fun updateContactInfo(newContact: ContactInfo) {

        val current = _bookingInfo.value
        _bookingInfo.value = current.copy(contact = newContact)
    }

    fun isInternational(ticket: PassengerTicket): Boolean {
        return ticket.roundTrip.outbound.flightType == "INTERNATIONAL"
    }

    fun isPassengerInfoValid(): Boolean {
        return _bookingInfo.value.tickets.all {
            val p = it.passenger
            p.firstName.isNotBlank() && p.lastName.isNotBlank() && p.idCard.isNotBlank()
        }
    }

    fun isContactInfoValid(): Boolean {
        val contact = _bookingInfo.value.contact
        return contact.fullName.isNotBlank() && (contact.phoneNumber.isNotBlank() || contact.email.isNotBlank())
    }
}
