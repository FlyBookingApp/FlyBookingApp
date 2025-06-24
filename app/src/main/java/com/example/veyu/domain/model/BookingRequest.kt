package com.example.veyu.domain.model

import com.example.veyu.ui.screen.flight_list.FlightBookingType
import com.example.veyu.ui.screen.flight_list.FlightInfo
import com.example.veyu.ui.screen.passenger_infor.ContactInfo
import com.example.veyu.ui.screen.passenger_infor.PassengerInfo

data class BookingRequest(
    val outbound: FlightInfo,
    val flightBookingType: FlightBookingType,
    val returnTrip: FlightInfo?,
)

data class Booking(
    val userId: Long = 0,
    val flightIds: List<Long>,
    val totalPrice: String = "",
    val passengerCount: PassengerCount = PassengerCount(),
    val status: String,
    val tripType: Boolean,
    val passengerInfo: List<PassengerInfo>,
    val contactInfo: ContactInfo = ContactInfo(),
    val bookingDate: String = "",
    val bookingSource: String = "",
    val promotionCode: String = "",
    val cancellationReason: String = ""
)

data class PassengerCount (
    val adult: Int = 0,
    val child: Int = 0,
    val infant: Int = 0
)