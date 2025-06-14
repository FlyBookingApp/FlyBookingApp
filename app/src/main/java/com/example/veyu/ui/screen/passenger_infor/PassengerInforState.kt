package com.example.veyu.ui.screen.passenger_infor
data class FlightData(
    val departTime: String? = null,
    val arriveTime: String? = null,
    val code: String? = null,
    val airline: String? = null,
    val price: String? = null,
    val departAirport: String? = null,
    val departAirportId: String? = null,
    val arriveAirport: String? = null,
    val arriveAirportId: String? = null,
    val type: String? = null,
)
data class RoundTrip(
    val outbound: FlightData,
    val returnTrip: FlightData
)
data class PassengerInfo(
    val lastName: String = "",
    val middleName: String = "",
    val firstName: String = "",
    val dateOfBirth: String = "",   // "yyyy-MM-dd"
    val phoneNumber: String = "",
    val email: String = "",
    val idCard: String = ""
)
data class ContactInfo(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

data class PassengerTicket(
    val passenger: PassengerInfo,
    val roundTrip: RoundTrip
)
data class BookingInfo(
    val contact: ContactInfo,
    val tickets: List<PassengerTicket>
)