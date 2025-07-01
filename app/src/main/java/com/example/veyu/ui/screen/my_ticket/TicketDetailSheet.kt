package com.example.veyu.ui.screen.my_ticket

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.veyu.R
import com.example.veyu.ui.screen.login.LoginScreen
import com.example.veyu.ui.screen.passenger_infor.FlightData
import com.example.veyu.ui.screen.payment.Ticket
import com.example.veyu.ui.screen.payment.TicketViewModel
import com.example.veyu.ui.screen.seat.BookingNew
import com.example.veyu.ui.screen.seat.Seat
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer


@Composable
fun SeatDetailDialog(
    tripType: Boolean,
    isShowDetail: (Boolean) -> Unit,
    isTicket: (Boolean) -> Unit,
    booking: BookingNew,
    uiFlights: List<FlightData>,
    seats: List<Seat>,
    viewModel: TicketViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    var isFirst by remember { mutableStateOf(true) }
    var isInitFirst by remember { mutableStateOf(true) }

    var flightNo by remember {mutableStateOf("")}

    val departureTickets by viewModel.departureTickets.collectAsState()
    val returnTickets by viewModel.returnTickets.collectAsState()

    LaunchedEffect(Unit) {
        if (isInitFirst) {
            viewModel.init(booking.bookingId)
            Log.d("DetailBookingSheet", "Init called")
            isInitFirst = false
        }
    }

    Log.d("DetailBookingSheet", "departureTickets: ${departureTickets.toString()}")
    Log.d("DetailBookingSheet", "returnTickets: ${returnTickets.toString()}")
    Log.d("DetailBookingSheet", "uiFlights: ${uiFlights.toString()}")
    Log.d("DetailBookingSheet", "seats: ${seats.toString()}")
    Log.d("DetailBookingSheet", "booking: ${booking.toString()}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Chi tiết booking", style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(20.dp)
                        .clickable {
                            isTicket(false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .background(Color(0x99C2F2F9))
                    .padding(10.dp)
                ,
                verticalArrangement = Arrangement.Center
            ) {
                val flightNoTemp: String?

                if (isLoading) {
                    FlightSkeletonItem()
                } else {
                    if (isFirst) {
                        FlightItem(flight = uiFlights.first(), isReturnTrip = false)
                        flightNoTemp = uiFlights.first().code
                    } else {
                        FlightItem(flight = uiFlights[1], isReturnTrip = true)
                        flightNoTemp = uiFlights[1].code
                    }

                    if (flightNoTemp != null) {
                        flightNo = flightNoTemp
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = "Thông tin vé",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Text(
                    text = " Mã chuyến bay: ${flightNo}\n Số lượng hành khách: ${booking.passengerCountInt}",
                    fontSize = 13.sp,
                    color = Color(0xA6000000)
                )
            }

            Box(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    .background(Color.White)
            ) {
                LazyColumn (
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .padding(bottom = 10.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 15.dp,
                                topEnd = 15.dp,
                                bottomStart = 15.dp,
                                bottomEnd = 15.dp
                            )
                        )
                        .border(1.5.dp, Color.Black, RoundedCornerShape(15.dp))

                ){
                    if (isLoading) {
                        items(2) { TicketSkeletonItemDt() }
                    }

                    if (isFirst) {
                        items(departureTickets.size) { index ->
                            Log.d("TicketViewModel", "departureTickets: ${departureTickets.toString()}")
                            Log.d("TicketViewModel", "seats: ${seats.toString()}")
                            val seat = seats.find { it.seatId == departureTickets[index].seatFlightId }
                            if (seat != null) {
                                TicketItem(departureTickets[index], seat)
                            }
                        }
                    } else {
                        items(returnTickets.size) { index ->
                            Log.d("TicketViewModel", "returnTickets: ${returnTickets.toString()}")
                            Log.d("TicketViewModel", "seats: ${seats.toString()}")
                            val seat = seats.find { it.seatId == returnTickets[index].seatFlightId }
                            if (seat != null) {
                                TicketItem(returnTickets[index], seat)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                if (!tripType) {
                    Spacer(modifier = Modifier.height(48.dp))
                } else {
                    Row(Modifier.fillMaxWidth()) {
                        OtherTicketButton(isFirst, isFist = { isFirst = it}, !tripType)
                        Spacer(modifier = Modifier.weight(1f))
                        OtherTicketButton(!isFirst, isFist = { isFirst = it}, tripType)
                    }
                }
            }
        }
    }
}

//seats.find { it.seatId == ticket.seatFlightId }
@Composable
fun TicketItem(
    ticket: Ticket,
    seat: Seat
) {
    var ticketClass: String = when(ticket.ticketClass) {
        "BUSINESS" -> "Hạng Thương gia"
        "ECONOMY" -> "Hạng Phổ thông"
        "PREMIUM_ECONOMY" -> "Hạng Phổ thông đặc biệt"
        else -> "Hạng Thương gia"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 3.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text ="Số ghế: ${seat.seatNumber}",
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text ="Hạng ghế: $ticketClass",
                        fontSize = 12.sp
                    )
                }

                Text(
                    text ="Giá tiền: ${ticket.price}",
                    fontSize = 12.sp
                )

                Text(
                    text ="Mã đặt vé: ${ticket.ticketNumber}",
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text ="Ngày đặt: ${ticket.createdAt}",
                    fontSize = 12.sp
                )
            }
        }

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }

}


@Composable
fun OtherTicketButton(
    isSelected: Boolean,
    isFist: (Boolean) -> Unit,
    isRoundTrip: Boolean
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF3F51B5) else Color.LightGray, // Xanh khi chọn
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { isFist(!isRoundTrip) }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isRoundTrip) {
                Image(
                    painter = painterResource(R.drawable.iconleft),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Chuyến đi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            } else {
                Text("Chuyến về", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.iconreight),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

        }
    }

}

@Composable
fun TicketSkeletonItemDt() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .placeholder(
                    visible = true,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = Color.White.copy(alpha = 0.6f)
                    )
                )
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FlightSkeletonItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(40.dp)
            .placeholder(
                visible = true,
                color = Color.LightGray.copy(alpha = 0.4f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White.copy(alpha = 0.6f)
                )
            )
    ) {}
}