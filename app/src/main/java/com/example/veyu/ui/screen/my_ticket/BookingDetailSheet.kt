package com.example.veyu.ui.screen.my_ticket

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.veyu.R
import com.example.veyu.ui.screen.passenger_infor.ContactInfo
import com.example.veyu.ui.screen.passenger_infor.PassengerInfo
import com.example.veyu.ui.screen.payment.ContactInputForm
import com.example.veyu.ui.screen.payment.CountdownText
import com.example.veyu.ui.screen.payment.DetailBookingViewModel
import com.example.veyu.ui.screen.payment.FlightItemPay
import com.example.veyu.ui.screen.payment.PassengerInputForm
import com.example.veyu.ui.screen.payment.PaymentDialog
import com.example.veyu.ui.screen.payment.parseContactInfo

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDetailDialog(
    onDismiss: () -> Unit,
    viewModel: DetailBookingViewModel = hiltViewModel(),
    bookingId: Long
) {
    val booking by viewModel.booking.collectAsState()
    val uiFlights by viewModel.uiFlights.collectAsState()
    val seats by viewModel.seats.collectAsState()
    val uiPassenger by viewModel.uiPassenger.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var showContactSheet by remember { mutableStateOf(false) }

    val sheetStateInfo = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingIndexInfo by remember { mutableStateOf<Int?>(null) }
    var showContactSheetInfo by remember { mutableStateOf(false) }
    var isFirst by remember { mutableStateOf(true) }
    var isShowDetail by remember { mutableStateOf(false) }
    var showTicket by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isFirst) {
            viewModel.init(bookingId)
            Log.d("DetailBookingSheet", "Init called")
            isFirst = false
        }
    }

    if (editingIndex != null) {
        ModalBottomSheet(
            onDismissRequest = { editingIndex = null },
            sheetState = sheetState
        ) {
            PassengerInputForm(
                passenger = uiPassenger[editingIndex!!],
            )
        }
    }

    if (showContactSheet) {
        ModalBottomSheet(
            onDismissRequest = { showContactSheet = false },
            sheetState = sheetState
        ) {
            ContactInputForm(
                contact = parseContactInfo(booking.note),
            )
        }
    }

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
                            onDismiss()
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
                if (uiFlights.isNotEmpty()) {
                    // Chuyến đi
                    FlightItem(flight = uiFlights.first(), isReturnTrip = false)

                    // Chuyến về (nếu có tripType == true && đủ 2 phần tử trong uiFlights)
                    if (booking.tripType && uiFlights.size > 1) {
                        FlightItem(flight = uiFlights[1], isReturnTrip = true)
                    }
                }
            }

            if (booking.status == "PENDING") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(top = 10.dp)
                ) {
                    val seatsOut = seats.filter { it.flightId == booking.flightIds.firstOrNull() }
                    val seatsRound = seats.filter { it.flightId == booking.flightIds.getOrNull(1) }

                    val seatOut = seatsOut.joinToString(", ") { it.seatNumber }
                    val seatRound = seatsRound.joinToString(", ") { it.seatNumber }

                    Text(
                        text = "Ghế đã đặt",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                R.drawable.seat
                                //id = logoResId
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .height(16.dp)
                                .padding(end = 3.dp)
                        )

                        Text(
                            text = "Chiều đi:",
                            fontSize = 13.5.sp,
                            modifier = Modifier.padding(end = 3.dp)
                        )

                        Text(
                            text = seatOut,
                            fontSize = 13.sp,
                        )
                    }

                    if (booking.tripType) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 3.dp, horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(
                                    R.drawable.seat
                                    //id = logoResId
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(16.dp)
                                    .padding(end = 3.dp)
                            )

                            Text(
                                text = "Chiều về: ",
                                fontSize = 13.5.sp,
                                modifier = Modifier.padding(end = 3.dp)
                            )

                            Text(
                                text = seatRound,
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = "Thông tin hành khách",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Text(
                    text = "Số lượng hành khách: ${booking.passengerCountInt}",
                    fontSize = 13.sp,
                    color = Color(0xA6000000)
                )
            }

            Box(
                modifier = Modifier
                    .heightIn(max = 150.dp)
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
                    itemsIndexed(uiPassenger) { index, passenger ->
                        PassengerInputCard(index = index + 1, uiPassenger[index], onClick = { editingIndex = index })
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = "Thông tin liên lạc",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Text(
                    text = " Thông tin liên lạc sẽ được sử dụng để xác nhận đặt chỗ,\n hoàn tiền/đổi lịch bay hoặc các yêu cần liên quan",
                    fontSize = 13.sp,
                    color = Color(0xA6000000)
                )
            }
            ContactInfoCard(
                contact = parseContactInfo(booking.note),
                onClick = { showContactSheet = true }
            )

            if (booking.status == "CONFIRMED") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00AD23))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.isyespay),
                        contentDescription = null,
                        modifier = Modifier
                            .height(30.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "Đã thanh toán",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF0004))
                        .padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row{
                        Image(
                            painter = painterResource(R.drawable.nopayis),
                            contentDescription = null,
                            modifier = Modifier
                                .height(22.dp)
                                .padding(end = 8.dp)
                        )

                        if (booking.status == "PENDING") {
                            Text(
                                text = "Chưa thanh toán",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Đã hủy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Divider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier.width(200.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(R.drawable.timeline),
                            contentDescription = null,
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 8.dp)
                        )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            CountdownText(booking.createdAt)
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x99A5D9E8))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Tổng tiền:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 13.sp,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = booking.totalPrice,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B1CCC),
                    fontSize = 16.sp,
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
            {
                if (booking.status == "CONFIRMED") {
                    Button(
                        onClick = {
                            showTicket = true
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF63C0FF),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Xem chi tiết vé", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (booking.status == "PENDING") {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                isShowDetail = true
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
                            Text(text = "Thanh toán", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.onDelete(booking.bookingId)
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF0004),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Huỷ vé", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }

    if (isShowDetail) {
        PaymentDialog(isShowDetail = { isShowDetail = it }, booking, viewModel)
    }

    if (showTicket) {
        SeatDetailDialog(booking.tripType, isShowDetail = { isShowDetail = it }, isTicket = { showTicket = it }, booking, uiFlights, seats)
    }
}

@Composable
fun PassengerInputCard(index: Int, passenger: PassengerInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Hành khách $index",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Color(0xFF00275B)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.usergray),
                        contentDescription = null,
                        modifier = Modifier
                            .height(30.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "${passenger.lastName} ${passenger.firstName}\n${passenger.dateOfBirth}",
                        fontSize = 13.5.sp,
                        color = Color(0xA6000000),
                        lineHeight = 16.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Mở thông tin"
            )
        }

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ContactInfoCard(contact: ContactInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Người liên hệ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Color(0xFF00275B),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.adresss),
                        contentDescription = null,
                        modifier = Modifier
                            .height(30.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "${contact.fullName.ifBlank { "(Chưa có tên)" }} \n${contact.phoneNumber}\n${contact.email}",
                        fontSize = 13.sp,
                        color = Color(0xA6000000),
                        lineHeight = 16.sp
                    )
                }
            }


            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Mở chỉnh sửa"
            )
        }

    }
}