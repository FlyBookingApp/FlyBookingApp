package com.example.veyu.ui.screen.flight_list

import FilterSheet
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.veyu.R
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.domain.model.BookingRequest
import com.example.veyu.domain.model.FlightSearchRequest
import com.example.veyu.ui.screen.ticket_type.TicketTypeViewModel
import com.example.veyu.ui.screen.ticket_type.toReadableString
import com.example.veyu.ui.theme.LightBlue
import com.example.veyu.ui.theme.LightGrayBg
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightListScreen(
    navController: NavController,
    viewModel: FlightListViewModel = hiltViewModel(),
    request: FlightSearchRequest?,
    onNavigateToMain: (BookingRequest) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateBackToHome: () -> Unit
) {
    val currentFilter by viewModel.currentFilter.collectAsState()

    val isDeparture by viewModel.isDeparture.collectAsState()

    val departureFlights by viewModel.dtFlights.collectAsState()
    val returnFlights by viewModel.rnFlights.collectAsState()
    val flights by viewModel.flights.collectAsState()
    val flightBookingType by viewModel.flightBookingType.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    // biến để lưu vé sẽ chọn
    var selectedFlight by remember { mutableStateOf<FlightInfo?>(null) }

    LaunchedEffect(request, isDeparture) {
        request?.let {
            viewModel.searchFlights(it)
            if (isDeparture) viewModel.updateFightBookingType(it)
        }
    }

    val isFlightSelectedManually by viewModel.isFlightSelectedManually.collectAsState()

    LaunchedEffect(isFlightSelectedManually) {
        if (!isFlightSelectedManually) return@LaunchedEffect

        val ids = flightBookingType.flightIds ?: return@LaunchedEffect
        if (ids.isEmpty()) return@LaunchedEffect

        val outbound = viewModel.getFlightById(ids.getOrNull(0) ?: return@LaunchedEffect, departureFlights)
        val returnTrip = if (ids.size == 2) viewModel.getFlightById(ids.getOrNull(1) ?: 0, flights) else null

        if (outbound != null) {
            val bookingRequest = BookingRequest(
                outbound = outbound,
                flightBookingType = flightBookingType,
                returnTrip = returnTrip
            )
            Log.d("BookingRequest", "Co: ${isFlightSelectedManually} | BookingRequest: $bookingRequest")
            onNavigateToMain(bookingRequest)

            viewModel.setFlightSelectedManually(false)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            TopHeader(flights, flightBookingType, onNavigateBack, onNavigateBackToHome)
            Spacer(modifier = Modifier.height(15.dp))
            DateTabs(flightBookingType)

            //Hien thi ve da chon
            if (flightBookingType.isRoundTrip) {
                flightBookingType.flightIds?.firstOrNull()?.let { id ->
                    viewModel.getFlightById(id, departureFlights)?.let { flight ->
                        TicketItem(flight, false, viewModel)
                    }
                }
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .background(LightGrayBg)
                .padding(15.dp)
            ) {
                FlightList(flights = flights, onFlightClick = { flight ->
                    selectedFlight = flight
                })

                BottomBarWithSheet(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(15.dp))
                        .background(LightBlue.copy(alpha = 0.3f)),
                    onShowSheetChange = { showSheet = it },
                    onShowFilter = { showFilterSheet = true }
                )

            }
        }
    }
    if (selectedFlight != null) {
        FlightDetailDialog(
            viewModel = viewModel,
            flight = selectedFlight!!,
            onDismiss = { selectedFlight = null },
            navController = navController
        )
    }

    if (showFilterSheet) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {},
            contentAlignment = Alignment.BottomCenter) {
            FilterSheet(
                initialFilter = currentFilter!!,
                onApplyFilter = {
                    viewModel.applyFilter(it)
                    showFilterSheet = false
                },
                onDismiss = { showFilterSheet = false }
            )
        }
    }

    if (showSheet) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {},
            contentAlignment = Alignment.BottomCenter) {
            FilterShort(
                onShowSheetChange = { showSheet = it },
                onSortOptionSelected = { viewModel.updateSortOption(it) }
            )
        }

    }



}

@Composable
fun TopHeader(flights: List<FlightInfo> = emptyList(), flightBookingType: FlightBookingType, onNavigateBack: () -> Unit = {}, onNavigateBackToHome: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .padding(end = 5.dp)
                .clickable { onNavigateBack() }
        )

        if (flights.isNotEmpty()) {
            val firstFlight = flights.first()
            Column() {
                Text(text = "${firstFlight.departAirport} → ${firstFlight.arriveAirport}")
                Text("${flightBookingType.passengerInfo.toReadableString()}", style = MaterialTheme.typography.bodySmall)
            }

        } else {
            Text(text = "Không tìm thấy chuyến bay")
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable {
                    onNavigateBackToHome()
                },
            text = "✕",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
@Composable
fun DateTabs(flightBookingType: FlightBookingType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        if (!flightBookingType.isRoundTrip) {
            DateItem("Ngày đi", convertIsoToReadable(flightBookingType.departureDate), Color(0xFF2B1CCC), Modifier.weight(1f))
            DateItem("Ngày về", "__/__/____", Color(0xFFF70C10), Modifier.weight(1f))
        } else {
            DateItem("Ngày đi", convertIsoToReadable(flightBookingType.departureDate), Color(0xFF2B1CCC), Modifier.weight(1f))
            DateItem("Ngày về", convertIsoToReadable(flightBookingType.returnDate ?: ""), Color(0xFFF70C10), Modifier.weight(1f))
        }
    }
}

@Composable
fun DateItem(label: String, date: String, color: Color, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Column (horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Black)
            Text(text = date, color = color)
        }
        Box(modifier = Modifier.padding(top = 10.dp).fillMaxWidth().height(1.dp).background(Color.Black))
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightList(flights: List<FlightInfo>, onFlightClick: (FlightInfo) -> Unit) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
    ) {
        items(flights.filter { !it.isChoiced }) { flight ->
            FlightItem(flight, onClick = { onFlightClick(flight) })
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightItem(flight: FlightInfo, onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val depart = try {
        LocalDateTime.parse(flight.departTime, inputFormatter).format(timeFormatter)
    } catch (e: Exception) {
        "--:--"
    }

    val arrive = try {
        LocalDateTime.parse(flight.arriveTime, inputFormatter).format(timeFormatter)
    } catch (e: Exception) {
        "--:--"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .border(width = 1.dp, color = Color.Black, shape = shape)
            .clip(shape)
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "$depart → $arrive",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                flight.price,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(flight.departAirport)
                Text(" → ")
                Text(flight.arriveAirport)
            }
            Text(flight.type, style = MaterialTheme.typography.bodySmall)
        }

        DashedDivider(color = Color.Black, thickness = 1f)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = flight.partLogo),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(30.dp)
                    .padding(end = 8.dp)
            )

            Text(
                text = "${flight.airline} • ${flight.code}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun BottomBarWithSheet(
    modifier: Modifier = Modifier,
    onShowSheetChange: (Boolean) -> Unit,
    onShowFilter: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .padding(12.dp)
                .clickable { onShowSheetChange(true)  },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onShowSheetChange(true)  },
                contentAlignment = Alignment.Center
            ) {
                Text("Sắp xếp", textAlign = TextAlign.Center)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onShowFilter() },
                contentAlignment = Alignment.Center
            ) {
                Text("Bộ lọc", textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun FilterShort(
    onShowSheetChange: (Boolean) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.BottomCenter
    )

    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nền: chữ "Sắp xếp theo" căn giữa
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .weight(3f),
                    text = "Sắp xếp theo",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )


                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            onShowSheetChange(false)
                        }
                        .weight(1f),
                    text = "✕",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            SortOption.values().forEachIndexed { index, option ->
                Text(
                    text = when (option) {
                        SortOption.PRICE_LOWEST -> "Giá thấp nhất"
                        SortOption.DEPART_EARLIEST -> "Khởi hành sớm nhất"
                        SortOption.DEPART_LATEST -> "Khởi hành muộn nhất"
                        SortOption.ARRIVE_EARLIEST -> "Hạ cánh sớm nhất"
                        SortOption.ARRIVE_LATEST -> "Hạ cánh muộn nhất"
                        SortOption.FLIGHT_SHORTEST -> "Thời gian bay ngắn nhất"
                    },
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 26.dp)
                        .clickable {
                            onSortOptionSelected(option)
                            onShowSheetChange(false)
                        }
                )
                DashedDivider(
                    color = Color.Black,
                    thickness = 1f,
                    dashLength = 8f,
                    gapLength = 4f
                )
            }
        }
    }
}

@Composable
fun DashedDivider(
    color: Color = Color.Gray,
    thickness: Float = 2f,
    dashLength: Float = 10f,
    gapLength: Float = 6f
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness.dp)
    ) {
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x + dashLength, 0f),
                strokeWidth = thickness
            )
            x += dashLength + gapLength
        }
    }
}

@Composable
fun TicketItem(flight: FlightInfo, isReturnTrip: Boolean, viewModel: FlightListViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Image(
            painter = painterResource(id = flight.partLogo),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .padding(end = 8.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = "${flight.departAirport} - ${flight.arriveAirport}",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                if(isReturnTrip == false) Image(painter = painterResource(id = R.drawable.ic_depart), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp)) else Image(painter = painterResource(id = R.drawable.ic_arrive), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp))
            }
            Row (verticalAlignment = Alignment.Bottom){
                Text(text = "${flight.departTime}", fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${flight.price}/vé",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.delete_ticket),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .padding(start = 8.dp)
                .clickable {
                    viewModel.onClinkDelete(flight.id)
                    viewModel.setIsDeparture(true)
                }
        )
    }
}

private fun convertIsoToReadable(input: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date: Date = inputFormat.parse(input) ?: return "Invalid date"
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}