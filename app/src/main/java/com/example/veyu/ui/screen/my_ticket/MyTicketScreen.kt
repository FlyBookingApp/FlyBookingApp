package com.example.veyu.ui.screen.my_ticket

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.veyu.R
import com.example.veyu.ui.screen.passenger_infor.FlightData
import com.example.veyu.ui.screen.seat.BookingNew
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyTicketScreen(
    viewModel: MyTicketViewModel = hiltViewModel(),

    ) {
    val bookingList by viewModel.bookingList.collectAsState()
    val uiFlights by viewModel.uiFlights.collectAsState()

    var isYesPay by remember { mutableStateOf(true) }
    var selectedDetail by remember { mutableStateOf<Long?>(null) }
    var isShowTicket by remember { mutableStateOf(false) }

    viewModel.init(if (isYesPay) "CONFIRMED" else "PENDING")

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        //onNavigateBack()
                    }
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Quản lý vé",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            //onNavigateBack()
                        },
                    text = "✕",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(47.dp))

            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable{
                                isYesPay = true
                            }
                    )

                    if (isYesPay) {
                        Divider(
                            color = Color(0xFF2B1CCC),
                            thickness = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable{
                                isYesPay = false
                            }
                    )

                    if (!isYesPay) {
                        Divider(
                            color = Color(0xFFF70C10),
                            thickness = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

            val filteredList = bookingList.filter {
                it.status == if (isYesPay) "CONFIRMED" else "PENDING"
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { booking ->
                    ItemBooking(!isYesPay, booking.tripType, selectedDetail = { selectedDetail = it }, booking, uiFlights, viewModel)
                }
            }

        }
    }

    selectedDetail?.let {
        BookingDetailDialog(
            onDismiss = { selectedDetail = null },
            bookingId = it
        )
    }

    if (isShowTicket) {
        //isShowDetail = false
        //SeatDetailDialog(true ,isShowDetail = { isShowDetail = it }, isTicket = { isShowTicket = it })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemBooking(
    isNoPayment: Boolean = false,
    isRoundTrip: Boolean = true,
    selectedDetail: (Long) -> Unit,
    booking: BookingNew,
    flights: List<FlightData>,
    viewModel: MyTicketViewModel
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 10.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable {
                selectedDetail(booking.bookingId)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Ngày đặt: ${formatDateTime(booking.createdAt)}",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            if(isNoPayment == false) Image(painter = painterResource(id = R.drawable.ispay), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(100.dp))
            else Image(painter = painterResource(id = R.drawable.notispay), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(100.dp))
        }
        if (!booking.tripType) {
            if (booking.flightIds != null && flights != null) {
                var flight = flights.firstOrNull{it.id == booking.flightIds[0]}
                if (flight != null) {
                    FlightItem(flight = flight, isReturnTrip = false)
                }
            }
        } else {
            if (booking.flightIds != null && flights != null) {
                var flight = flights.firstOrNull{it.id == booking.flightIds[0]}
                var filghtRound = flights.firstOrNull{it.id == booking.flightIds[1]}
                if (flight != null && filghtRound != null) {
                    FlightItem(flight = flight, isReturnTrip = false)
                    FlightItem(flight = filghtRound, isReturnTrip = true)
                }
            }
        }
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
    {
        if (isNoPayment) {
            Image(
                painter = painterResource(R.drawable.buttondelete),
                contentDescription = null,
                modifier = Modifier
                    .height(35.dp)
                    .padding(end = 8.dp)
                    .padding(bottom = 5.dp)
                    .clickable {
                        viewModel.onDelete(booking.bookingId)
                    }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.detail),
            contentDescription = null,
            modifier = Modifier
                .height(30.dp)
                .padding(end = 8.dp)
        )
    }
}

@Composable
fun FlightItem(flight: FlightData, isReturnTrip: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color(0x4D000000))
            .padding(horizontal = 3.dp, vertical = 3.dp)
    ) {
        flight.partLogo?.let { logoResId ->
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .padding(end = 8.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text ="${flight.departAirport} - ${flight.arriveAirport}",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            Row (verticalAlignment = Alignment.Bottom){
                Text(text = "${flight.departTime}",
                    fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                if(isReturnTrip == false) Image(painter = painterResource(id = R.drawable.ic_depart), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp)) else Image(painter = painterResource(id = R.drawable.ic_arrive), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(input: String): String {
    // Parser input
    val parsed = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    // Output pattern
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return parsed.format(formatter)
}