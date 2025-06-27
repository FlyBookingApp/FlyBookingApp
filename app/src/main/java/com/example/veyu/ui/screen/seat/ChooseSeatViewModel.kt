package com.example.veyu.ui.screen.seat

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.model.Request.BookingRequest
import com.example.veyu.data.remote.model.Request.PassengerRequest
import com.example.veyu.data.repository.BookingRepository
import com.example.veyu.domain.model.Booking
import com.example.veyu.data.repository.SeatFlightRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.ui.screen.passenger_infor.toReadableString
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.text.replace

@HiltViewModel
class ChooseSeatViewModel @Inject constructor(
    private val repository: SeatFlightRepository,
    private val repositoryUser: UserRepository,
    private val userPreferences: UserPreferences,
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private var _seatsBusiness: List<Seat> = emptyList()
    private val _seatsBss = MutableStateFlow<List<Seat>>(emptyList())
    val seatsBusiness: StateFlow<List<Seat>> = _seatsBss

    private var _seatsEconomy: List<Seat> = emptyList()
    private val _seatsEmy = MutableStateFlow<List<Seat>>(emptyList())
    val seatsEconomy: StateFlow<List<Seat>> = _seatsEmy

    private var _seatsPremiuEconomy: List<Seat> = emptyList()
    private val _seatsPE = MutableStateFlow<List<Seat>>(emptyList())
    val seatsPremiuEconomy: StateFlow<List<Seat>> = _seatsPE

    private var _booking = MutableStateFlow(BookingNew())
    val booking: StateFlow<BookingNew> = _booking

    private var _seatsTempIds = MutableStateFlow<List<Long>>(emptyList())
    val seatsTemps: StateFlow<List<Long>> = _seatsTempIds

    private var totalPriceTemp = MutableStateFlow(0.0)

    private var _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private var errorMessageApi = MutableStateFlow("")
    val errorMessage: StateFlow<String> = errorMessageApi

    private val _isDeparture = MutableStateFlow(true)
    val isDeparture: StateFlow<Boolean> = _isDeparture

    private var _bookingId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val bookingId: StateFlow<Long?> = _bookingId

    private val price = MutableStateFlow(0.0)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadingSeats(request: Booking) {
        _seatsBusiness = emptyList()
        _seatsEconomy = emptyList()
        _seatsPremiuEconomy = emptyList()
        _seatsBss.value = emptyList()
        _seatsEmy.value = emptyList()
        _seatsPE.value = emptyList()

        if (!request.tripType) {
            fetchSeatsFromServer(request.flightIds.first())
        } else {
            if (_isDeparture.value) {
                fetchSeatsFromServer(request.flightIds.first())
            } else {
                fetchSeatsFromServer(request.flightIds.last())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchSeatsFromServer(flightId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getSeatByFlightsId(flightId)
                val seatResponses = result.getOrNull() ?: emptyList()

                val sortedSeatResponses = seatResponses.sortedBy { it.seatId }

                _seatsBusiness = sortedSeatResponses
                    .filter { it.seatType == "BUSINESS" }
                    .map { seat ->
                        Seat(
                            seatId = seat.seatId,
                            flightId = seat.flightId,
                            price = formatPrice(seat.price),
                            seatNumber = seat.seatNumber,
                            status = seat.status,
                            seatClass = seat.seatType
                        )
                    }

                _seatsEconomy = sortedSeatResponses
                    .filter { it.seatType == "ECONOMY" }
                    .map { seat ->
                        Seat(
                            seatId = seat.seatId,
                            flightId = seat.flightId,
                            price = formatPrice(seat.price),
                            seatNumber = seat.seatNumber,
                            status = seat.status,
                            seatClass = seat.seatType
                        )
                    }

                _seatsPremiuEconomy = sortedSeatResponses
                    .filter { it.seatType == "PREMIUM_ECONOMY" }
                    .map { seat ->
                        Seat(
                            seatId = seat.seatId,
                            flightId = seat.flightId,
                            price = formatPrice(seat.price),
                            seatNumber = seat.seatNumber,
                            status = seat.status,
                            seatClass = seat.seatType
                        )
                    }

                _seatsBss.value = _seatsBusiness
                _seatsEmy.value = _seatsEconomy
                _seatsPE.value = _seatsPremiuEconomy

                _isError.value = false

            } catch (e: Exception) {
                Log.e("ChooseSeatViewModel", "API error: ${e.message}")
                _seatsBusiness = emptyList()
                _seatsEconomy = emptyList()
                _seatsPremiuEconomy = emptyList()

                _seatsBss.value = emptyList()
                _seatsEmy.value = emptyList()
                _seatsPE.value = emptyList()

                _isError.value = true
                errorMessageApi.value = e.message.toString()
            }
        }
    }

    fun updateBooking(request: Booking) {
        _booking.value = BookingNew(
            tripType = request.tripType,
            flightIds = request.flightIds,
            passengerCount = request.passengerCount,
            status = request.status,
            passengerInfo = request.passengerInfo,
            contactInfo = request.contactInfo,
            totalPrice = request.totalPrice
        )
        price.value = changeFormatPrice(request.totalPrice)
        Log.d("ChooseSeatViewModel", "Updated booking: ${_booking.value.toString()}")
    }

    fun getChosenSeats(): List<Pair<Long, String>> {
        val business = _seatsBusiness.filter { it.status == "CHOOSE" }.map { it.seatId to it.seatNumber }
        val economy = _seatsEconomy.filter { it.status == "CHOOSE" }.map { it.seatId to it.seatNumber }
        val premium = _seatsPremiuEconomy.filter { it.status == "CHOOSE" }.map { it.seatId to it.seatNumber }
        return (business + economy + premium).filter { it.first != null && it.second != null }
            .map { it.first!! to it.second!! }
    }

    fun updateBookingWithChosenSeats() {
        val chosenSeats = getChosenSeats()
        val updated = _booking.value.copy(
            seatIds = chosenSeats.map { it.first },
            seatNumber = chosenSeats.map { it.second }
        )
        _booking.value = updated
        Log.d("ChooseSeatViewModel", "Updated booking with chosen seats: ${_booking.value.seatIds.toString()}")
    }


    fun deleteSeatIds() {
        val updated = _booking.value.copy(
            seatIds = emptyList(),
            seatNumber = emptyList()
        )
        _booking.value = updated
    }

    fun onClickSeat(seatNumber: String, seatClass: String) {
        if (seatNumber == "") return

        val currentSeats = when (seatClass) {
            "BUSINESS" -> _seatsBusiness
            "ECONOMY" -> _seatsEconomy
            "PREMIUM_ECONOMY" -> _seatsPremiuEconomy
            else -> _seatsBusiness
        }

        val maxPassenger = _booking.value.passengerCount.let {
            it.adult + it.child + it.infant
        }

        val currentChosen = getChosenSeats().size

        val updatedSeats = currentSeats.map { seat ->
            if (seat.seatNumber == seatNumber) {
                when (seat.status) {
                    "CHOOSE" -> seat.copy(status = "AVAILABLE")  // Bỏ chọn
                    "AVAILABLE" -> {
                        if (currentChosen < maxPassenger) {
                            seat.copy(status = "CHOOSE")  // Chọn nếu chưa vượt giới hạn
                        } else {
                            seat  // Giữ nguyên vì vượt giới hạn
                        }
                    }
                    else -> seat
                }
            } else {
                seat
            }
        }

        when (seatClass) {
            "BUSINESS" -> {
                _seatsBusiness = updatedSeats
                _seatsBss.value = updatedSeats
            }
            "ECONOMY" -> {
                _seatsEconomy = updatedSeats
                _seatsEmy.value = updatedSeats
            }
            "PREMIUM_ECONOMY" -> {
                _seatsPremiuEconomy = updatedSeats
                _seatsPE.value = updatedSeats
            }
        }
    }

    fun checkAvailableSeats(): Boolean {
        val passengerCount = _booking.value.passengerCount.adult + _booking.value.passengerCount.child + _booking.value.passengerCount.infant

        val chosenBusiness = _seatsBusiness.count { it.status == "CHOOSE" }
        val chosenEconomy = _seatsEconomy.count { it.status == "CHOOSE" }
        val chosenPremium = _seatsPremiuEconomy.count { it.status == "CHOOSE" }

        val totalChosen = chosenBusiness + chosenEconomy + chosenPremium

        // Ví dụ: trả về true nếu có ít nhất 1 ghế được chọn
        return totalChosen == passengerCount
    }

    private suspend fun fetchUser(username: String) {
        repositoryUser.getUserByUsername(username).onSuccess {
            _booking.value = _booking.value.copy(
                userId = it.id
            )
        }.onFailure {
            // handle error
        }
    }

    fun fetchUserFromLocalStore() {
        viewModelScope.launch {
            val name = userPreferences.userName.first()
            Log.d("ProfileViewModel", "fetchUserFromLocalStore: $name")
            if (!name.isNullOrEmpty()) {
                fetchUser(name)
            } else {
                // handle empty name, maybe navigate to login
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

    private fun changeFormatPrice(price: String): Double {
        return price.replace(".", "")
            .replace("đ", "")
            .replace(",", "")
            .trim()
            .toIntOrNull()?.toDouble() ?: 0.0
    }

    private fun nextToBooking(context: Context) {
        viewModelScope.launch {
            // Lấy userId trước
            val name = userPreferences.userName.first()
            if (!name.isNullOrEmpty()) {
                val userResult = repositoryUser.getUserByUsername(name)
                userResult.onSuccess {
                    _booking.value = _booking.value.copy(userId = it.id)

                    Log.d("ChooseSeatViewModel", "Updated booking with chosen seats: ${_seatsTempIds.value.toString()}")

                    // Kiểm tra nếu đủ data
                    if (_booking.value.seatIds.isEmpty() || _booking.value.flightIds.isEmpty()) {
                        Log.e("ChooseSeatViewModel", "Data not ready for booking.")
                        return@launch
                    }

                    // Tạo booking
                    createBooking(context)
                }.onFailure {
                    Log.e("ChooseSeatViewModel", "Error fetch user: ${it.message}")
                }
            } else {
                Log.e("ChooseSeatViewModel", "No user name found.")
            }
        }
    }

    fun onClickNext(context: Context): Boolean {
        val isRoundTrip = _booking.value.tripType
        val enoughSeats = checkAvailableSeats()

        if (!isRoundTrip && enoughSeats) {
            updateBookingWithChosenSeats()
            _seatsTempIds.value = _seatsTempIds.value + _booking.value.seatIds
            nextToBooking(context)
            return true
        }

        if (isRoundTrip) {
            if (_isDeparture.value && enoughSeats) {
                updateBookingWithChosenSeats()
                _seatsTempIds.value = _seatsTempIds.value + _booking.value.seatIds
                _isDeparture.value = false
                return false
            } else if (enoughSeats) {
                updateBookingWithChosenSeats()
                _seatsTempIds.value = _seatsTempIds.value + _booking.value.seatIds
                nextToBooking(context)
                return true
            }
        }

        Toast.makeText(context, "Vui lòng chọn đủ ghế với số lượng khách hàng", Toast.LENGTH_SHORT).show()
        return false
    }

    fun onClickDelete() {
        deleteSeatIds()
        _isDeparture.value = true
    }

    fun updatePrice() {
        // Lấy giá các ghế được chọn
        val business = _seatsBusiness.filter { it.status == "CHOOSE" }.map { changeFormatPrice(it.price ?: "0") }
        val economy = _seatsEconomy.filter { it.status == "CHOOSE" }.map { changeFormatPrice(it.price ?: "0") }
        val premium = _seatsPremiuEconomy.filter { it.status == "CHOOSE" }.map { changeFormatPrice(it.price ?: "0") }

        // Tính tổng giá
        val totalPrice = (business + economy + premium).sum() + if(!_isDeparture.value) totalPriceTemp.value else 0.0 + if(_isDeparture.value) price.value else 0.0

        if (_isDeparture.value) {
            totalPriceTemp.value = totalPrice
        }

        // Cập nhật booking với giá đã format
        _booking.value = _booking.value.copy(
            totalPrice = formatPrice(totalPrice)
        )
    }

    private suspend fun createBooking(context: Context) {
        bookingRepository.createBooking(
            BookingRequest(
                userId = _booking.value.userId,
                passengerCount = _booking.value.passengerCount.adult + _booking.value.passengerCount.child + _booking.value.passengerCount.infant,
                status = "PENDING",
                tripType = if(_booking.value.tripType) "ROUND_TRIP" else "ONE_WAY",
                passengers = _booking.value.passengerInfo.map { passenger ->
                    PassengerRequest(
                        firstName = passenger.firstName,
                        lastName = passenger.lastName,
                        personalId = passenger.idCard,
                        birthDate = passenger.dateOfBirth,
                        gender = passenger.gender,
                        passportNumber = passenger.passport,
                        email = passenger.email,
                        phone = passenger.phoneNumber
                    )
                },
                seatFlightIds = _seatsTempIds.value,
                flightIds = _booking.value.flightIds,
                note = _booking.value.contactInfo.toReadableString(),
            )
        ).onSuccess {
            _bookingId.value = it.id
            Log.d("ChooseSeatViewModel", "Booking created with ID: $_bookingId")
            Toast.makeText(context, "Tạo booking thành công!!", Toast.LENGTH_SHORT).show()
        }.onFailure {
            Log.e("ChooseSeatViewModel", "API error: ${it.message}")
            Toast.makeText(context, "Lỗi: Không thể tạo booking", Toast.LENGTH_SHORT).show()
        }
    }
}