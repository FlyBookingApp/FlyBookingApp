package com.example.veyu.ui.screen.seat
import com.example.veyu.domain.model.PassengerCount
import com.example.veyu.ui.screen.passenger_infor.ContactInfo
import com.example.veyu.ui.screen.passenger_infor.PassengerInfo

data class Seat(
    val seatId: Long?,
    val flightId: Long?,
    val flightNo: String = "",
    val price: String = "",
    val seatNumber: String,
    val status: String,
    val seatClass: String,
)

data class BookingNew(
    val userId: Long = 0,
    val flightIds: List<Long> = emptyList(),
    val seatIds: List<Long> = emptyList(),
    val seatNumber: List<String> = emptyList(),
    val totalPrice: String = "0đ",
    val passengerCount: PassengerCount = PassengerCount(),
    val status: String = "",
    val tripType: Boolean = false,
    val passengerInfo: List<PassengerInfo> = emptyList(),
    val contactInfo: ContactInfo = ContactInfo(),
    val bookingDate: String = "",
    val bookingSource: String = "",
    val promotionCode: String = "",
    val cancellationReason: String = "",

    val bookingId: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val passengerIds: List<Long> = emptyList(),
    val bookingReference: String = "",
    val passengerCountInt: Int = 0,
    val note: String = "",
)