package com.example.veyu.ui.screen.flight_list

import FlightFilter
import TimeSlot
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veyu.R
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.domain.model.FlightSearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@HiltViewModel
class FlightListViewModel @Inject constructor(
    private val repository: FlightRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private var _departureFlights: List<FlightInfo> = emptyList()
    private val _dtFlights = MutableStateFlow<List<FlightInfo>>(emptyList())
    val dtFlights: StateFlow<List<FlightInfo>> = _dtFlights

    private var _returnFlights: List<FlightInfo> = emptyList()
    private val _rnFlights = MutableStateFlow<List<FlightInfo>>(emptyList())
    val rnFlights: StateFlow<List<FlightInfo>> = _rnFlights

    private var _allFlights: List<FlightInfo> = emptyList()
    private val _flights = MutableStateFlow<List<FlightInfo>>(emptyList())
    val flights: StateFlow<List<FlightInfo>> = _flights

    private var _isLogin = MutableStateFlow(true)
    val isLogin: StateFlow<Boolean> = _isLogin

    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentFilter = MutableStateFlow<FlightFilter?>(
        FlightFilter(
            minPrice = 0,
            maxPrice = 20_000_000,
            stops = emptySet(),
            departTimeSlots = emptySet(),
            arriveTimeSlots = emptySet(),
            airlines = emptySet()
        )
    )
    val currentFilter: StateFlow<FlightFilter?> = _currentFilter
    private val _sortOption = MutableStateFlow(SortOption.PRICE_LOWEST)
    val sortOption: StateFlow<SortOption> = _sortOption

    private val _flightBookingType = MutableStateFlow(FlightBookingType())
    val flightBookingType: StateFlow<FlightBookingType> = _flightBookingType

    private val _isFlightSelectedManually = MutableStateFlow(false)
    val isFlightSelectedManually: StateFlow<Boolean> = _isFlightSelectedManually

    private val _isDeparture = MutableStateFlow(true)
    val isDeparture: StateFlow<Boolean> = _isDeparture

    fun setIsDeparture(value: Boolean) {
        _isDeparture.value = value
    }

    fun setFlightSelectedManually(value: Boolean) {
        _isFlightSelectedManually.value = value
    }

    fun onChangeIsLogin() {
        viewModelScope.launch {
            val token = userPreferences.token.first()
            val isLoggedIn = !token.isNullOrEmpty()
            _isLogin.value = isLoggedIn
            Log.d("FlightListViewModel", "onChangeIsLogin: $isLoggedIn")
        }
    }

    fun resetIsLogin() {
        _isLogin.value = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchFlights(request: FlightSearchRequest) {
        if (!request.isRoundTrip) {
            fetchFlightsFromServer(
                departureCode = request.departureCode,
                arrivalCode = request.destinationCode,
                departureDate = request.departureDate,
            )
        } else {
            if (_isDeparture.value) {
                fetchFlightsFromServer(
                    departureCode = request.departureCode,
                    arrivalCode = request.destinationCode,
                    departureDate = request.departureDate,
                )
            }
            else (
                fetchFlightsFromServer(
                    departureCode = request.destinationCode,
                    arrivalCode = request.departureCode,
                    departureDate = request.returnDate,
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchFlightsFromServer(
        departureCode: String,
        arrivalCode: String,
        departureDate: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.searchFlights(departureCode, arrivalCode, departureDate)
                val flightResponses = result.getOrNull() ?: emptyList()

                _allFlights = flightResponses
                    .filter { !it.isFull }
                    .filter { it.status == "SCHEDULED" }
                    .filter { it.availableSeats >= totalPerson() }
                    .map { flight ->
                        FlightInfo(
                            departTime = convertIsoToReadable(flight.departureTime),
                            arriveTime = convertIsoToReadable(flight.arrivalTime),
                            code = flight.flightNumber,
                            airline = flight.airline.name,
                            price = formatPrice(flight.currentPrice),
                            departAirport = flight.departureAirport.iataCode,
                            arriveAirport = flight.arrivalAirport.iataCode,
                            type = "Bay thẳng",
                            partLogo = getLogoForAirline(flight.airline.name),
                            id = flight.id,
                            flightType = flight.flightType,
                            departAirportName = flight.departureAirport.city,
                            arriveAirportName = flight.arrivalAirport.city
                        )
                    }

                _allFlights.forEachIndexed { index, flight ->
                    Log.d("FlightInfo", "[$index] ➤ ${flight.code} | ${flight.departAirport} → ${flight.arriveAirport} | ${flight.departTime} → ${flight.arriveTime} | ${flight.airline} | ${flight.partLogo} | ${flight.price} | ${flight.type}")
                }

                // phân loại theo chuyến đi / chuyến về
                if (_isDeparture.value) {
                    _departureFlights = _allFlights
                    _dtFlights.value = _departureFlights
                } else {
                    _returnFlights = _allFlights
                    _rnFlights.value = _returnFlights
                }

                applyCurrentFilterAndSort()

                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("FlightListViewModel", "API error: ${e.message}")
                _allFlights = emptyList()
                _flights.value = emptyList()
            }
        }
    }

    fun updateFightBookingType(request: FlightSearchRequest) {
        _flightBookingType.value = FlightBookingType(
            isRoundTrip = request.isRoundTrip,
            departureDate = request.departureDate,
            returnDate = request.returnDate,
            passengerInfo = request.passengerInfo
        )
    }

    fun addFlightId(flightId: Long) {
        val current = _flightBookingType.value
        val currentIds = current.flightIds ?: emptyList() // Đảm bảo luôn là một list, dù rỗng

        val updatedFlightIds: List<Long>

        if (currentIds.isNotEmpty()) {
            updatedFlightIds = listOf(currentIds.first(), flightId)
        } else {
            updatedFlightIds = listOf(flightId)
        }

        // Chỉ cập nhật nếu danh sách mới khác danh sách cũ để tránh recomposition không cần thiết
        // (Mặc dù trong trường hợp này, nó sẽ luôn khác nếu logic if/else được kích hoạt đúng cách)
        if (updatedFlightIds != currentIds) {
            val updated = current.copy(
                flightIds = updatedFlightIds
            )
            _flightBookingType.value = updated
        }
    }


    fun removeFlightId(flightId: Long) {
        val current = _flightBookingType.value
        val updated = current.copy(
            flightIds = current.flightIds?.filter { it != flightId } ?: emptyList()
        )
        _flightBookingType.value = updated
    }

    fun onClinkYes(flightId: Long): Boolean {
        addFlightId(flightId)
        setIsChoice(flightId)

        val isRoundTrip = _flightBookingType.value.isRoundTrip
        val selectedCount = _flightBookingType.value.flightIds?.size ?: 0

        _isFlightSelectedManually.value = (!isRoundTrip || selectedCount == 2)
        Log.d("FlightBookingType", " ${_flightBookingType.value.flightIds.toString()}")
        Log.d("FlightBookingType", "[${_isFlightSelectedManually.value}")
        return !isRoundTrip || selectedCount == 2
    }

    fun onClinkDelete(flightId: Long) {
        removeFlightId(flightId)
        setIsChoice(flightId);
        Log.d("RemoteListFlight", "[$flightId]")
    }

    private fun setIsChoice(flightId: Long) {
        val current = _allFlights.toMutableList()

        val updatedList = current.map {flights ->
            if (flights.id == flightId) {
                flights.copy(isChoiced = !flights.isChoiced)
            } else {
                flights
            }
        }

        _allFlights = updatedList
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

    fun formatPrice(price: Double?): String {
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

    fun getFlightById(id: Long, lights: List<FlightInfo>): FlightInfo? {
        return lights.find { it.id == id }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
        applyCurrentFilterAndSort()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeSlot(timeStr: String): TimeSlot? {
        val time = parseTime(timeStr) ?: return null
        return TimeSlot.fromHour(time.hour)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyFilter(filter: FlightFilter) {
        _currentFilter.value = filter
        applyCurrentFilterAndSort()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyCurrentFilterAndSort() {
        val filter = _currentFilter.value
        var filtered = _allFlights

        if (filter != null) {
            filtered = _allFlights.filter { flight ->
                val price = parsePrice(flight.price)

                val matchPrice = price in filter.minPrice..filter.maxPrice
                val matchStop = filter.stops.isEmpty() || filter.stops.contains(flight.type)
                val matchAirline = filter.airlines.isEmpty() || filter.airlines.contains(flight.airline)

                val departSlot = getTimeSlot(flight.departTime)
                val arriveSlot = getTimeSlot(flight.arriveTime)

                val matchDepart = filter.departTimeSlots.isEmpty() || (departSlot != null && filter.departTimeSlots.contains(departSlot))
                val matchArrive = filter.arriveTimeSlots.isEmpty() || (arriveSlot != null && filter.arriveTimeSlots.contains(arriveSlot))

                matchPrice && matchStop && matchAirline && matchDepart && matchArrive
            }
        }

        _flights.value = sortFlights(filtered)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortFlights(flights: List<FlightInfo>): List<FlightInfo> {
        return when (_sortOption.value) {
            SortOption.PRICE_LOWEST -> flights.sortedBy { parsePrice(it.price) }

            SortOption.DEPART_EARLIEST -> flights.sortedBy {
                parseTime(it.departTime) ?: LocalTime.MAX
            }

            SortOption.DEPART_LATEST -> flights.sortedByDescending {
                parseTime(it.departTime) ?: LocalTime.MIN
            }

            SortOption.ARRIVE_EARLIEST -> flights.sortedBy {
                parseTime(it.arriveTime) ?: LocalTime.MAX
            }

            SortOption.ARRIVE_LATEST -> flights.sortedByDescending {
                parseTime(it.arriveTime) ?: LocalTime.MIN
            }

            SortOption.FLIGHT_SHORTEST -> flights.sortedBy {
                val depart = parseTime(it.departTime)
                val arrive = parseTime(it.arriveTime)
                if (depart != null && arrive != null) {
                    java.time.Duration.between(depart, arrive).toMinutes()
                } else Long.MAX_VALUE
            }
        }
    }

    private fun parsePrice(price: String): Int {
        return price.replace(".", "")
            .replace("đ", "")
            .replace(",", "")
            .trim()
            .toIntOrNull() ?: Int.MAX_VALUE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseTime(time: String): LocalTime? {
        val cleaned = time.trim()
        return try {
            if (cleaned.contains(":") && cleaned.length <= 5) {
                LocalTime.parse(cleaned.padStart(5, '0'), DateTimeFormatter.ofPattern("H:mm"))
            } else {
                // Dạng có ngày tháng
                val parts = cleaned.split(" ")
                if (parts.size == 2) {
                    LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("H:mm:ss"))
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun totalPerson(): Int {
        return _flightBookingType.value.passengerInfo.adults + _flightBookingType.value.passengerInfo.children + _flightBookingType.value.passengerInfo.infants
    }
}
