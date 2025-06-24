import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

enum class TimeSlot(val label: String, val startHour: Int, val endHour: Int) {
    EARLY_MORNING("Sáng sớm", 0, 6),
    MORNING("Sáng", 6, 12),
    AFTERNOON("Chiều", 12, 18),
    EVENING("Tối", 18, 24);

    companion object {
        fun fromLabel(label: String): TimeSlot? = values().find { it.label == label }
        fun fromHour(hour: Int): TimeSlot? = values().find { hour in it.startHour until it.endHour }
    }
}

data class FlightInfo(
    val departureTime: String,
    val arrivalTime: String,
    val flightCode: String,
    val airline: String,
    val price: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val stops: String
)

data class FlightFilter(
    val minPrice: Int,
    val maxPrice: Int,
    val stops: Set<String>,
    val departTimeSlots: Set<TimeSlot>,
    val arriveTimeSlots: Set<TimeSlot>,
    val airlines: Set<String>
)

fun filterFlights(flights: List<FlightInfo>, filter: FlightFilter): List<FlightInfo> {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    return flights.filter { flight ->
        val price = flight.price.replace(".", "").replace("đ", "").replace(",", "").trim().toIntOrNull() ?: 0
        val departTime = formatter.parse(flight.departureTime)
        val arriveTime = formatter.parse(flight.arrivalTime)

        val departSlot = TimeSlot.fromHour(departTime.hours)
        val arriveSlot = TimeSlot.fromHour(arriveTime.hours)

        val matchPrice = price in filter.minPrice..filter.maxPrice
        val matchStop = filter.stops.isEmpty() || flight.stops in filter.stops
        val matchDepart = filter.departTimeSlots.isEmpty() || (departSlot != null && departSlot in filter.departTimeSlots)
        val matchArrive = filter.arriveTimeSlots.isEmpty() || (arriveSlot != null && arriveSlot in filter.arriveTimeSlots)
        val matchAirline = filter.airlines.isEmpty() || flight.airline in filter.airlines

        matchPrice && matchStop && matchDepart && matchArrive && matchAirline
    }
}

@Composable
fun FilterSheet(
    initialFilter: FlightFilter,
    onApplyFilter: (FlightFilter) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var stops by remember { mutableStateOf(initialFilter.stops.toMutableSet()) }
    val allStops = listOf("Bay thẳng", "1 điểm dừng")

    val timeSlots = TimeSlot.values().map { it.label }
    var departTimeSlots by remember { mutableStateOf(initialFilter.departTimeSlots.map { it.label }.toMutableSet()) }
    var arriveTimeSlots by remember { mutableStateOf(initialFilter.arriveTimeSlots.map { it.label }.toMutableSet()) }

    val airlines = listOf("VietJet Air", "Pacific Airlines", "Vietnam Airlines", "Bamboo Airways")
    var selectedAirlines by remember { mutableStateOf(initialFilter.airlines.toMutableSet()) }

    var priceRange by remember { mutableStateOf(initialFilter.minPrice.toFloat()..initialFilter.maxPrice.toFloat()) }

    fun formatCurrency(value: Float): String {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return formatter.format(value.toInt()) + " đ"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .weight(3f),
                    text = "Bộ lọc",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            onDismiss()
                        }
                        .weight(1f),
                    text = "✕",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Khoảng giá (VND)", style = MaterialTheme.typography.titleSmall, color = Color.Black)

            RangeSlider(
                value = priceRange,
                onValueChange = { priceRange = it },
                valueRange = 0f..20_000_000f,
                steps = 100,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color(0xFF1E88E5),
                    inactiveTrackColor = Color(0xFFE0E0E0),
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tối thiểu: ${formatCurrency(priceRange.start)}", color = Color.DarkGray)
                Text("Tối đa: ${formatCurrency(priceRange.endInclusive)}", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Số điểm dừng", color = Color.Black)
            Row {
                allStops.forEach { stop ->
                    FilterChip(
                        label = stop,
                        selected = stops.contains(stop),
                        onClick = {
                            stops = stops.toMutableSet().apply {
                                if (contains(stop)) remove(stop) else add(stop)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Giờ cất cánh", color = Color.Black)
            Row {
                timeSlots.forEach { slot ->
                    TimeFilterChip(slot, departTimeSlots) { departTimeSlots = it.toMutableSet() }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Giờ hạ cánh", color = Color.Black)
            Row {
                timeSlots.forEach { slot ->
                    TimeFilterChip(slot, arriveTimeSlots) { arriveTimeSlots = it.toMutableSet() }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Hãng hàng không", color = Color.Black)
            Column {
                airlines.forEach { airline ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedAirlines.contains(airline),
                            onCheckedChange = {
                                selectedAirlines = selectedAirlines.toMutableSet().apply {
                                    if (it) add(airline) else remove(airline)
                                }
                            },
                            colors = CheckboxDefaults.colors(uncheckedColor = Color.Black)
                        )
                        Text(airline)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val filter = FlightFilter(
                            minPrice = priceRange.start.toInt(),
                            maxPrice = priceRange.endInclusive.toInt(),
                            stops = stops.toSet(),
                            departTimeSlots = departTimeSlots.mapNotNull { TimeSlot.fromLabel(it) }.toSet(),
                            arriveTimeSlots = arriveTimeSlots.mapNotNull { TimeSlot.fromLabel(it) }.toSet(),
                            airlines = selectedAirlines.toSet()
                        )
                        onApplyFilter(filter)
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .height(48.dp)
                        .weight(2f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF63C0FF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Áp dụng", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val filter = FlightFilter(
                            minPrice = 0,
                            maxPrice = 20_000_000,
                            stops = emptySet(),
                            departTimeSlots = emptySet(),
                            arriveTimeSlots = emptySet(),
                            airlines = emptySet()
                        )
                        onApplyFilter(filter)
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0004),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(text = "Đặt lại", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .border(1.dp, if (selected) Color.Blue else Color.Gray, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Text(label, color = if (selected) Color.Blue else Color.Gray)
    }
}

@Composable
fun TimeFilterChip(label: String, value: Set<String>, onValueChange: (Set<String>) -> Unit) {
    FilterChip(
        label = label,
        selected = value.contains(label),
        onClick = {
            val newSet = value.toMutableSet().apply {
                if (contains(label)) remove(label) else add(label)
            }
            onValueChange(newSet)
        }
    )
}
