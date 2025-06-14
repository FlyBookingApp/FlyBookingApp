package com.example.veyu.ui.screen.flight_list

import FlightFilter
import TimeSlot
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FlightListViewModel : ViewModel() {
    private val _allFlights = listOf(
        FlightInfo("2025-06-13 01:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 03:00:00", "2025-06-13 06:00:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 02:00:00", "2025-06-13 17:00:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 05:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 06:40:00", "2025-06-13 15:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 07:40:00", "2025-06-13 6:40:00","VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 05:40:00", "2025-06-13 6:40:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 01:40:00", "2025-06-13 6:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 06:40:00", "2025-06-13 6:40:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 01:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 03:00:00", "2025-06-13 06:00:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 02:00:00", "2025-06-13 17:00:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 05:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 06:40:00", "2025-06-13 15:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 07:40:00", "2025-06-13 6:40:00","VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 05:40:00", "2025-06-13 6:40:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 01:40:00", "2025-06-13 6:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 06:40:00", "2025-06-13 6:40:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 01:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 03:00:00", "2025-06-13 06:00:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 02:00:00", "2025-06-13 17:00:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU", "Bay thẳng"),
        FlightInfo("2025-06-13 12:00:00", "2025-06-13 11:00:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 13:40:00", "2025-06-13 15:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 16:40:00", "2025-06-13 6:40:00","VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 19:40:00", "2025-06-13 6:40:00", "VJ206", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 20:40:00", "2025-06-13 6:40:00", "VN100", "Vietnam Airlines", "810.000đ","HEHE","HUHU","Bay thẳng"),
        FlightInfo("2025-06-13 22:40:00", "2025-06-13 6:40:00", "VJ256", "VietJet Air", "810.000đ","HEHE","HUHU","Bay thẳng"),
    )

    private val _flights = MutableStateFlow(_allFlights)
    val flights: StateFlow<List<FlightInfo>> = _flights

    private var currentFilter: FlightFilter? = null

    private val _sortOption = MutableStateFlow(SortOption.PRICE_LOWEST)
    val sortOption: StateFlow<SortOption> = _sortOption

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
        currentFilter = filter
        applyCurrentFilterAndSort()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyCurrentFilterAndSort() {
        val filter = currentFilter
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
}
