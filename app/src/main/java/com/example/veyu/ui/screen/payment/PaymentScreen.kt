package com.example.veyu.ui.screen.payment

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.veyu.R
import com.example.veyu.ui.screen.passenger_infor.ContactInfo
import com.example.veyu.ui.screen.passenger_infor.FlightData
import com.example.veyu.ui.screen.passenger_infor.PassengerInfo
import kotlinx.coroutines.delay
import com.example.veyu.ui.screen.my_ticket.PassengerInputCard
import com.example.veyu.ui.screen.my_ticket.ContactInfoCard
import com.example.veyu.ui.screen.my_ticket.SeatDetailDialog

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentScreen(
    viewModel: DetailBookingViewModel = hiltViewModel(),
    request: Long?,
    navController: NavController,
) {
    // Khi nhấn nút back trên điện thoại
    BackHandler {
        navController.navigate("home") {
            // Xóa toàn bộ các screen trước đó
            popUpTo("home") {
                inclusive = true
            }
        }
    }

    val booking by viewModel.booking.collectAsState()
    val uiFlights by viewModel.uiFlights.collectAsState()
    val seats by viewModel.seats.collectAsState()
    val uiPassenger by viewModel.uiPassenger.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var showContactSheet by remember { mutableStateOf(false) }

    var isShowDetail by remember { mutableStateOf(false) }
    var isShowTicket by remember { mutableStateOf(false) }

    LaunchedEffect(request) {
        request?.let {
            viewModel.init(it)
        }

        Log.d("PaymentScreen", "PaymentScreen: $request")
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



    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_2_1),
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
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Thanh toán",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            navController.navigate("home") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        },
                    text = "✕",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier
                    .height(150.dp)
                    .background(Color(0xFFC2F2F9)),
                verticalArrangement = Arrangement.Center
            ) {
                if (uiFlights.isNotEmpty()) {
                    // Chuyến đi
                    FlightItemPay(flight = uiFlights.first(), isReturnTrip = false)

                    // Chuyến về (nếu có tripType == true && đủ 2 phần tử trong uiFlights)
                    if (booking.tripType && uiFlights.size > 1) {
                        FlightItemPay(flight = uiFlights[1], isReturnTrip = true)
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))


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
                            text = "Chiều về:",
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

            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            Text(
                                text = "Thông tin hành khách",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )

                            Text(
                                text = "Số lượng hành khách: ${booking.passengerCountInt}",
                                fontSize = 13.sp,
                                color = Color(0xA6000000),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .heightIn(max = 180.dp)
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
                                //itemsIndexed(bookingState.tickets) { index, ticket ->
                                //PassengerInputCard(
                                //index = index + 1,
                                //passenger = ticket.passenger,
                                //onClick = { editingIndex = index }
                                //)
                                //}


                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Nhập thông tin liên hệ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                        Text(
                            text = "Thông tin liên lạc sẽ được sử dụng để xác nhận đặt chỗ, hoàn tiền/đổi lịch bay hoặc các yêu cần liên quan",
                            fontSize = 13.sp,
                            color = Color(0xA6000000),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        ContactInfoCard(
                            contact = parseContactInfo(booking.note),
                            onClick = { showContactSheet = true }
                        )
                    }
                }
            }

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
                    Row {
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = "Tổng tiền",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = booking.totalPrice,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (booking.status == "PENDING") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {

                        Button(
                            onClick = {
                                isShowDetail = true
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(2f)
                                .padding(start = 8.dp, end = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF42E5FA),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Thanh toán", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.onDelete(booking.bookingId)
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF0000),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Hủy vé", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else if (booking.status == "CONFIRMED") {
                    Button(
                        onClick = {
                            //
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
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }

    if (isShowDetail) {
        PaymentDialog(isShowDetail = { isShowDetail = it }, booking, viewModel)
    }

    if (isShowTicket) {
        //SeatDetailDialog(tripType = booking.tripType ,isShowDetail = {null}, isTicket = { isShowTicket = it })
    }
}

@Composable
fun FlightItemPay(flight: FlightData ,isReturnTrip: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 10.dp)
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

        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(
                    text = "${flight?.departAirport} (${flight?.departAirportId}) - ${flight?.arriveAirport} (${flight?.arriveAirportId})",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isReturnTrip == false) Image(
                    painter = painterResource(id = R.drawable.ic_depart),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(50.dp)
                ) else Image(
                    painter = painterResource(id = R.drawable.ic_arrive),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(50.dp)
                )
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "${flight?.departTime}",
                    fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${flight?.price}/vé",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun PassengerInputForm(
    passenger: PassengerInfo
) {
    var info by remember { mutableStateOf(passenger) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Chỉnh sửa hành khách", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Giới tính:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.width(8.dp))
            RadioButton(
                selected = info.gender == "Nam",
                onClick = {},
                enabled = false
            )
            Text("Nam")
            Spacer(Modifier.width(16.dp))
            RadioButton(
                selected = info.gender == "Nữ",
                onClick = {},
                enabled = false
            )
            Text("Nữ")
        }

        OutlinedTextField(
            value = info.lastName,
            onValueChange = { info = info.copy(lastName = it) },
            label = { Text("Họ") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.firstName,
            onValueChange = { info = info.copy(firstName = it) },
            label = { Text("Tên đệm & Tên") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.dateOfBirth.split("T").first() ,
            onValueChange = { info = info.copy(dateOfBirth = it) },
            label = { Text("Ngày sinh(yyyy-mm-dd)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.phoneNumber,
            onValueChange = { info = info.copy(phoneNumber = it) },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.idCard,
            onValueChange = { info = info.copy(idCard = it) },
            label = { Text("CMND/CCCD/Mã định danh") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.passport,
            onValueChange = { info = info.copy(passport = it) },
            label = { Text("Số Passport") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ContactInputForm(
    contact: ContactInfo
) {
    var info by remember { mutableStateOf(contact) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Thông tin liên hệ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = info.fullName,
            onValueChange = { info = info.copy(fullName = it) },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.phoneNumber,
            onValueChange = { info = info.copy(phoneNumber = it) },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.email,
            onValueChange = { info = info.copy(email = it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}





fun parseContactInfo(input: String): ContactInfo {
    val parts = input.split("-") // ["fullName: Cao Tien Dat", "email: a@gmail.com", "phoneNumber: 0354464053"]

    val fullName = parts.getOrNull(0)?.split(": ")?.getOrNull(1) ?: ""
    val email = parts.getOrNull(1)?.split(": ")?.getOrNull(1) ?: ""
    val phoneNumber = parts.getOrNull(2)?.split(": ")?.getOrNull(1) ?: ""

    return ContactInfo(
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CountdownText(targetTimeStr: String) {
    if (targetTimeStr.isEmpty()) {
        Text(text = "", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        return
    }

    // Parse thời gian bắt đầu
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val startTime = try {
        LocalDateTime.parse(targetTimeStr, formatter)
    } catch (e: Exception) {
        // Trường hợp parse lỗi, hiển thị "00:00:00"
        Text(text = "00:00:00", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        return
    }

    // Thời gian kết thúc = bắt đầu + 15 phút
    val endTime = startTime.plusMinutes(15)

    var timeLeft by remember {
        mutableStateOf(Duration.between(LocalDateTime.now(), endTime))
    }

    LaunchedEffect(Unit) {
        while (timeLeft.toSeconds() > 0) {
            delay(1000)
            timeLeft = Duration.between(LocalDateTime.now(), endTime)
        }
    }

    // Khi hết giờ, hiển thị "00:00:00"
    val totalSeconds = timeLeft.toSeconds().coerceAtLeast(0)

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    Text(
        text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.White
    )
}

