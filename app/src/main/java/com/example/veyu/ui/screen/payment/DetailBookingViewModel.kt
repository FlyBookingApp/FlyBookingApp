package com.example.veyu.ui.screen.payment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.R
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.model.Request.PaymentTransactionRq
import com.example.veyu.data.repository.BookingRepository
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.data.repository.PassengerRepository
import com.example.veyu.data.repository.PaymentMethodRepository
import com.example.veyu.data.repository.PaymentTrasactionRepository
import com.example.veyu.data.repository.TicketRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.ui.screen.passenger_infor.FlightData
import com.example.veyu.ui.screen.passenger_infor.PassengerInfo
import com.example.veyu.ui.screen.seat.BookingNew
import com.example.veyu.ui.screen.seat.Seat
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@HiltViewModel
class DetailBookingViewModel @Inject constructor(
    private val repositoryBooking: BookingRepository,
    private val repositoryTicket: TicketRepository,
    private val repositoryFlight: FlightRepository,
    private val repositoryPassenger: PassengerRepository,
    private val repository: PaymentTrasactionRepository,
    private val repositoryUser: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var isRoundTrip: Boolean = false
    private var userId: Long? = null

    private var _booking = MutableStateFlow(BookingNew())
    val booking: StateFlow<BookingNew> = _booking

    private val _uiFlights = MutableStateFlow<List<FlightData>>(emptyList())
    val uiFlights: StateFlow<List<FlightData>> = _uiFlights

    private val _uiPassenger = MutableStateFlow<List<PassengerInfo>>(emptyList())
    val uiPassenger: StateFlow<List<PassengerInfo>> = _uiPassenger

    private val _seats = MutableStateFlow<List<Seat>>(emptyList())
    val seats: StateFlow<List<Seat>> = _seats

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    init {
        Log.d("DetailBookingViewModel", "Created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("DetailBookingViewModel", "Destroyed")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(request: Long) {
        _seats.value = emptyList()
        _uiFlights.value = emptyList()
        _uiPassenger.value = emptyList()
        _booking.value = BookingNew()
        _isError.value = false
        isRoundTrip = false
        userId = null

        viewModelScope.launch {
            try {
                val bookingResult = repositoryBooking.getBookingById(request)
                val bookingResponse = bookingResult.getOrNull() ?: return@launch

                Log.d("PaymentViewModel", "bookingResponse: ${bookingResponse.toString()}")

                val seatsList = bookingResponse.seatFlights.map {
                    Seat(
                        seatId = it.id,
                        flightId = it.flightId,
                        seatNumber = it.seatNumber,
                        flightNo = it.flightNo,
                        status = it.status,
                        seatClass = it.seatType
                    )
                }
                _seats.value = seatsList

                Log.d("PaymentViewModel", "seatsList: ${_seats.value.toString()}")

                _booking.value = BookingNew(
                    flightIds = bookingResponse.flightIds ?: emptyList(),
                    bookingReference = bookingResponse.bookingReference,
                    userId = bookingResponse.userId,
                    status = bookingResponse.status,
                    totalPrice = formatPrice(bookingResponse.totalAmount),
                    passengerIds = bookingResponse.passengerIds ?: emptyList(),
                    tripType = bookingResponse.tripType == "ROUND_TRIP",
                    bookingDate = bookingResponse.bookingDate,
                    passengerCountInt = bookingResponse.passengerCount,
                    bookingSource = bookingResponse.bookingSource ?: "",
                    promotionCode = bookingResponse.promotionCode ?: "",
                    cancellationReason = bookingResponse.cancellationReason ?: "",
                    createdAt = bookingResponse.createdAt,
                    updatedAt = bookingResponse.updatedAt,
                    note = bookingResponse.note ?: "",
                    bookingId = bookingResponse.id
                )

                Log.d("PaymentViewModel", "init: ${_booking.value.toString()}")

                bookingResponse.passengerIds?.forEach { passengerId ->
                    getPassengerByIdFromServer(passengerId)
                }

                bookingResponse.flightIds?.forEach { flightId ->
                    getFlightByIdFromServer(flightId)
                }

                Log.d("PaymentViewModel", "_uiFlights: ${_uiFlights.value.toString()}")
                if (_uiFlights.value.isNotEmpty()) {
                    if(_uiFlights.value.size > 1) {
                        var flightTemp: FlightData
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val date1 = LocalDateTime.parse(_uiFlights.value[1].departTime, formatter)
                        val date2 = LocalDateTime.parse(_uiFlights.value[2].departTime, formatter)
                        if (date1.isAfter(date2)) {
                            val updatedList = listOf(_uiFlights.value[1], _uiFlights.value[0])
                            // Cập nhật lại state bằng updatedList
                            _uiFlights.value = updatedList
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("PaymentViewModel", "init error: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFlightByIdFromServer(flightId: Long) {
        viewModelScope.launch {
            try {
                val result = repositoryFlight.getFlightById(flightId)
                val flightResponse = result.getOrNull() ?: return@launch

                val newFlight = FlightData(
                    id = flightResponse.id,
                    departTime = convertIsoToReadable(flightResponse.departureTime),
                    arriveTime = convertIsoToReadable(flightResponse.arrivalTime),
                    code = flightResponse.flightNumber,
                    airline = flightResponse.airline.name,
                    price = formatPrice(flightResponse.currentPrice),
                    departAirport = flightResponse.departureAirport.city,
                    departAirportId = flightResponse.departureAirport.iataCode,
                    arriveAirport = flightResponse.arrivalAirport.city,
                    arriveAirportId = flightResponse.arrivalAirport.iataCode,
                    type = "Bay thẳng",
                    status = flightResponse.status,
                    flightType = flightResponse.flightType,
                    partLogo = getLogoForAirline(flightResponse.airline.name)
                )

                val updatedList = _uiFlights.value + newFlight
                _uiFlights.value = updatedList


            } catch (e: Exception) {
                Log.e("PaymentViewModel", "FlightAPI error: ${e.message}")
            }
        }

        Log.d("PaymentViewModel", "_uiFlights: ${_uiFlights.value.toString()}")
    }

    fun onConfirm(bookingId: Long, totalAmount: Double) {
        viewModelScope.launch {
            val userId = fetchUserFromLocalStoreSuspend() ?: return@launch

            val confirmed = confirmBooking(bookingId)
            if (!confirmed) {
                Log.e("PaymentViewModel", "Xác nhận booking thất bại, dừng lại.")
                return@launch
            }

            val ticketGenerated = generateTicket(bookingId)
            if (!ticketGenerated) {
                Log.e("PaymentViewModel", "Tạo vé thất bại, dừng lại.")
                return@launch
            }

            createdPaymentTransaction(bookingId, userId, totalAmount)

            Log.d("PaymentViewModel", "Hoàn tất quy trình xác nhận booking.")
        }
    }

    fun onDelete(bookingId: Long) {
        viewModelScope.launch {
            try {
                val result = repositoryBooking.deleteBooking(bookingId)
                Log.d("PaymentViewModel", "deleteBooking: ${result.toString()}")
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "deleteBooking error: ${e.message}")
            }
        }
    }

    suspend fun confirmBooking(bookingId: Long): Boolean {
        return try {
            val result = repositoryBooking.confirmBooking(bookingId)
            val response = result.getOrNull()
            val isConfirmed = response?.status == "CONFIRMED"

            if (isConfirmed) {
                _booking.value = _booking.value.copy(status = "CONFIRMED")
            }

            isConfirmed
        } catch (e: Exception) {
            Log.e("PaymentViewModel", "confirmBooking error: ${e.message}")
            false
        }
    }

    suspend fun generateTicket(bookingId: Long): Boolean {
        return try {
            val result = repositoryTicket.generateTicket(bookingId)
            Log.d("PaymentViewModel", "generateTicket: ${result.toString()}")
            val response = result.getOrNull()
            true
        } catch (e: Exception) {
            Log.e("PaymentViewModel", "generateTicket error: ${e.message}")
            false
        }
    }

    fun createdPaymentTransaction(bookingIdRq: Long, userIdRq: Long, totalAmount: Double) {
        viewModelScope.launch {
            try {
                val request = PaymentTransactionRq (
                    bookingId = bookingIdRq,
                    paymentMethodId = userIdRq,
                    amount = totalAmount
                )
                val result = repository.createPaymentTransaction(request)
                Log.d("PaymentViewModel", "createPaymentTransaction: ${result.toString()}")
                val bookingResponse = result.getOrNull() ?: return@launch

                _isError.value = false
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "createPaymentTransaction error: ${e.message}")
                _isError.value = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPassengerByIdFromServer(passengerId: Long) {
        viewModelScope.launch {
            try {
                val result = repositoryPassenger.getPassengerById(passengerId)
                Log.d("PaymentViewModel", "passengerResponse: ${result.toString()}")
                val passengerResponse = result.getOrNull() ?: return@launch

                Log.d("PaymentViewModel", "passengerResponse: ${passengerResponse.toString()}")
                val newPassenger = PassengerInfo(
                    firstName = passengerResponse.firstName,
                    lastName = passengerResponse.lastName,
                    dateOfBirth = passengerResponse.dateOfBirth,
                    idCard = passengerResponse.idNumber ?: "",
                    passport = passengerResponse.passportNumber ?: "",
                    phoneNumber = passengerResponse.phone ?: "",
                    gender = passengerResponse.gender
                )

                val updatedList = _uiPassenger.value + newPassenger
                _uiPassenger.value = updatedList

            } catch (e: Exception) {
                Log.e("PaymentViewModel", "PassengerAPI error: ${e.message}")
            }
        }
    }

    private suspend fun fetchUserFromLocalStoreSuspend(): Long? {
        val name = userPreferences.userName.first()
        Log.d("ProfileViewModel", "fetchUserFromLocalStore: $name")
        if (!name.isNullOrEmpty()) {
            val user = repositoryUser.getUserByUsername(name).getOrNull()
            Log.d("ProfileViewModel", "fetchUserFromLocalStore: $user")
            userId = user?.id
            return userId
        } else {
            Log.e("myTicketViewModel", "User name is empty")
            return null
        }
    }

    private fun formatPrice(price: Double?): String {
        // Nếu price null thì trả về "0đ"
        if (price == null) return "0đ"
        // Chuyển Double sang Long để format kiểu số nguyên
        val longPrice = price.toLong()
        return "%,dđ".format(longPrice).replace(",", ".")
    }

    private fun getLogoForAirline(airline: String): Int {
        return when (airline) {
            "VietJet Air" -> R.drawable.vietjetair
            "Vietnam Airlines" -> R.drawable.vietnamairline
            "Bamboo Airways" -> R.drawable.bambooairways
            "Jetstar Pacific" -> R.drawable.pacificairlines
            "Singapore Airlines" -> R.drawable.singaporeairlines
            "Thai Airways" -> R.drawable.thaiairways
            "Malaysia Airlines" -> R.drawable.malaysiaairlines
            "Korean Air" -> R.drawable.koreanair
            "Japan Airlines" -> R.drawable.japanairlines

            "VASCO (Vietnam Air Service Company)" -> R.drawable.vasco_logo
            "Vietnam Air Services Company" -> R.drawable.vasco_logo
            "Hai Au Aviation" -> R.drawable.logo_sticky
            "Sky Viet Aviation" -> R.drawable.sk_logo
            "Vietnam Cargo Airlines" -> R.drawable.vietnamairline
            "Air Mekong" -> R.drawable.air_meko_logo
            "Pacific Airlines" -> R.drawable.pacificairlines
            "Star Aviation" -> R.drawable.star_air
            else -> R.drawable.vietnamairline
        }
    }

    private fun convertIsoToReadable(input: String): String {
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

    fun changeFormatPriceDb(price: String): Double {
        return price
            .replace(".", "")
            .replace(",", "")
            .replace("đ", "")
            .trim()
            .toDoubleOrNull() ?: 0.0
    }
}