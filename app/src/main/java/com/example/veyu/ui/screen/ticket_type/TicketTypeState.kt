package com.example.veyu.ui.screen.ticket_type

data class TicketTypeState(
    //Chuyên hướng FlightList
    val isFlightList: Boolean = false,
    // một chiều hay khứ hồi
    val isRoundTrip: Boolean = false,
    // điểm đi
    val selectedDeparture: String = "",
    // điểm đến
    val selectedDestination: String = "",
    // ngày đi
    val departureDate: String = "",
    // ngày về
    val returnDate: String = "",
    // hiện lịch chọn ngày
    val showDatePicker: Boolean = false,
    // có hiện ngày về hay không
    val isReturnPicker: Boolean = false,
    // danh sách san bay
    val airportLocations: List<String> = emptyList(),
    val airportLocationsId: List<String> = emptyList(),
    // số lượng hành khách
    val passengerInfo: PassengerInfo = PassengerInfo(),
    val showPassengerPicker: Boolean = false,
)
data class PassengerInfo(
    val adults: Int = 1,     // từ 12 tuổi trở lên, bắt buộc >= 1
    val children: Int = 0,   // từ 2 đến dưới 12 tuổi
    val infants: Int = 0     // dưới 2 tuổi
)