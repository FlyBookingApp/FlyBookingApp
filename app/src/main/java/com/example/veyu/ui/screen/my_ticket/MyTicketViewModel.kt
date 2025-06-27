package com.example.veyu.ui.screen.my_ticket

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.R
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.repository.BookingRepository
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.ui.screen.passenger_infor.FlightData
import com.example.veyu.ui.screen.seat.BookingNew
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
class MyTicketViewModel @Inject constructor(
    private val repositoryBooking: BookingRepository,
    private val repositoryUser: UserRepository,
    private val userPreferences: UserPreferences,
    private val repositoryFlight: FlightRepository
): ViewModel() {
    private var userId: Long? = null

    private var _bookingList = MutableStateFlow<List<BookingNew>> (emptyList())
    val bookingList: StateFlow<List<BookingNew>> = _bookingList

    private val _uiFlights = MutableStateFlow<List<FlightData>>(emptyList())
    val uiFlights: StateFlow<List<FlightData>> = _uiFlights

    private var _sortBookingOption = MutableStateFlow(SortBookingOption.NEWEST_FIRST)
    val sortBookingOption: StateFlow<SortBookingOption> = _sortBookingOption

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(status: String) {
        viewModelScope.launch {
            val userId = fetchUserFromLocalStoreSuspend() ?: return@launch

            getListBookingByUserIdSuspend(userId, status)

            // Sau khi bookingList cập nhật, lấy flight
            _bookingList.value.forEach { booking ->
                booking.flightIds.forEach { flightId ->
                    getFlightByIdFromServerSuspend(flightId)
                }
            }

            Log.d("ProfileViewModel", "_uiFlights: ${_uiFlights.value.toString()}")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getListBookingByUserIdSuspend(userId: Long, status: String) {
        try {
            val result = repositoryBooking.getBookingByUserId(userId)
            Log.d("ProfileViewModel", "getListBookingByUserIdSuspend: $result")
            val bookingResponses = result.getOrNull() ?: emptyList()

            _bookingList.value = bookingResponses
                .filter { it.status == status }
                .map { booking ->
                    BookingNew(
                        flightIds = booking.flightIds ?: emptyList(),
                        bookingReference = booking.bookingReference,
                        userId = booking.userId,
                        status = booking.status,
                        totalPrice = formatPrice(booking.totalAmount),
                        passengerIds = booking.passengerIds ?: emptyList(),
                        tripType = booking.tripType == "ROUND_TRIP",
                        bookingDate = booking.bookingDate,
                        passengerCountInt = booking.passengerCount,
                        bookingSource = booking.bookingSource ?: "",
                        promotionCode = booking.promotionCode ?: "",
                        cancellationReason = booking.cancellationReason ?: "",
                        createdAt = booking.createdAt,
                        updatedAt = booking.updatedAt,
                        note = booking.note ?: "",
                        bookingId = booking.id
                    )
                }

            updateSortOption(_sortBookingOption.value)
            Log.d("ProfileViewModel", "_bookingList: ${_bookingList.value.size}")
        } catch (e: Exception) {
            Log.e("FlightListViewModel", "API error: ${e.message}")
            _bookingList.value = emptyList()
        }
    }

    private suspend fun getFlightByIdFromServerSuspend(flightId: Long) {
        try {
            val result = repositoryFlight.getFlightById(flightId)
            Log.d("ProfileViewModel", "getFlightByIdFromServerSuspend: $result")
            val flightResponse = result.getOrNull() ?: return

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


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSortOption(option: SortBookingOption) {
        _sortBookingOption.value = option
        _bookingList.value = sortBookings(_bookingList.value, option)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortBookings(bookings: List<BookingNew>, sortOption: SortBookingOption): List<BookingNew> {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        return when (sortOption) {
            SortBookingOption.NEWEST_FIRST -> bookings.sortedByDescending {
                try {
                    LocalDateTime.parse(it.createdAt, formatter)
                } catch (e: Exception) {
                    LocalDateTime.MIN
                }
            }
            SortBookingOption.OLDEST_FIRST -> bookings.sortedBy {
                try {
                    LocalDateTime.parse(it.createdAt, formatter)
                } catch (e: Exception) {
                    LocalDateTime.MIN
                }
            }
        }
    }

    fun onDelete(bookingId: Long, context: Context) {
        viewModelScope.launch {
            try {
                val result = repositoryBooking.deleteBooking(bookingId)
                Log.d("PaymentViewModel", "deleteBooking: ${result}")

                _bookingList.value = _bookingList.value.filter { it.bookingId != bookingId }
                Toast.makeText(context, "Hủy booking thành công!!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("PaymentViewModel", "deleteBooking error: ${e.message}")
                Toast.makeText(context, "Lỗi: Hủy booking thất bại!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatPrice(price: Double?): String {
        // Nếu price null thì trả về "0đ"
        if (price == null) return "0đ"
        // Chuyển Double sang Long để format kiểu số nguyên
        val longPrice = price.toLong()
        return "%,dđ".format(longPrice).replace(",", ".")
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
}

