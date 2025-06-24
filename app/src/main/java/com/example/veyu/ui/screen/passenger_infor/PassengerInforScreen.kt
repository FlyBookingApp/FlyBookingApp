package com.example.veyu.ui.screen.passenger_infor

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veyu.domain.model.PassengerCount
import com.example.veyu.R
import com.example.veyu.domain.model.Booking
import com.example.veyu.domain.model.BookingRequest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerInfoScreen(
    request: BookingRequest?,
    viewModel: PassengerInforViewModel = hiltViewModel(),
    navController: NavController,
    onNavigateBack: () -> Unit,
    onNavigateToMain: (Booking) -> Unit,
    ) {
    val isInitFirst by viewModel.isInitFirst.collectAsState()
    val bookingState by viewModel.bookingInfo.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var showContactSheet by remember { mutableStateOf(false) }

    LaunchedEffect(request) {
        if (isInitFirst) {
            request?.let {
                viewModel.initBookingInfo(request)
                Log.d("PassengerInfoScreen", "PassengerInfoScreen: ${viewModel.updatePassengerTicket(it).toString()}")
            }

            viewModel.updateIsInitFirst(false)
        }
    }

    if (editingIndex != null) {
        ModalBottomSheet(
            onDismissRequest = { editingIndex = null },
            sheetState = sheetState
        ) {
            PassengerInputForm(
                passenger = bookingState.tickets[editingIndex!!].passenger,
                onUpdate = {
                    viewModel.updatePassengerAt(editingIndex!!, it)
                    editingIndex = null
                },
                onClose = { editingIndex = null }
            )
        }
    }

    if (showContactSheet) {
        ModalBottomSheet(
            onDismissRequest = { showContactSheet = false },
            sheetState = sheetState
        ) {
            ContactInputForm(
                contact = bookingState.contact,
                onUpdate = {
                    viewModel.updateContactInfo(it)
                    showContactSheet = false
                }
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
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onNavigateBack() }
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Thông tin tài khoản",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            onNavigateBack()
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
                if (bookingState.tickets.isNotEmpty()) {
                    TicketItem(ticket = bookingState.tickets.first(), isReturnTrip = false)
                    TicketItem(ticket = bookingState.tickets.first(), isReturnTrip = true)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "Nhập thông tin hành khách",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }

                        Box(
                            modifier = Modifier
                                .heightIn(max = 200.dp)
                                .background(Color.White)
                        ) {
                            LazyColumn (
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .padding(bottom = 10.dp)
                                    .border(1.dp, Color(0xFF002AFF))
                            ){
                                itemsIndexed(bookingState.tickets) { index, ticket ->
                                    PassengerInputCard(
                                        index = index + 1,
                                        passenger = ticket.passenger,
                                        onClick = { editingIndex = index }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Nhập thông tin liên hệ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(vertical = 12.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Thông tin liên lạc sẽ được sử dụng để xác nhận đặt chỗ, hoàn tiền/đổi lịch bay hoặc các yêu cần liên quan",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        ContactInfoCard(
                            contact = bookingState.contact,
                            onClick = { showContactSheet = true }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Tổng tiền",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = viewModel.getTotalPrice(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                val context = LocalContext.current
                Button(
                    onClick = {
                        when {
                            !viewModel.isPassengerInfoValid() -> {
                                Toast.makeText(
                                    context,
                                    "Vui lòng nhập đầy đủ thông tin hành khách",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            !viewModel.isContactInfoValid() -> {
                                Toast.makeText(
                                    context,
                                    "Vui lòng nhập thông tin liên hệ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                val booking = Booking(
                                    flightIds = bookingState.tickets.flatMap { ticket ->
                                        buildList {
                                            ticket.roundTrip.outbound.id?.let { add(it) }
                                            ticket.roundTrip.returnTrip?.id?.let { add(it) }
                                        }
                                    }.distinct(),
                                    passengerCount = PassengerCount(
                                        adult = viewModel.flightBookingType.passengerInfo.adults,
                                        child = viewModel.flightBookingType.passengerInfo.children,
                                        infant = viewModel.flightBookingType.passengerInfo.infants
                                    ),
                                    status = "HOLD",
                                    tripType = viewModel.flightBookingType.isRoundTrip,
                                    passengerInfo = bookingState.tickets.map { it.passenger },
                                    contactInfo = bookingState.contact
                                )

                                Log.d("PassengerInfoScreen", "PassengerInfoScreen: ${booking.toString()}")
                                onNavigateToMain(booking)
                            }
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF42E5FA),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tiếp tục", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



@Composable
fun TicketItem(ticket: PassengerTicket, isReturnTrip: Boolean = false) {
    val flight = if (isReturnTrip) ticket.roundTrip.returnTrip else ticket.roundTrip.outbound
    if (flight != null) {
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
                    Text(text = "${flight?.departTime}", fontSize = 12.sp)
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
}

@Composable
fun StepIndicator(number: String, label: String, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = if (isActive) R.drawable.ic_green else R.drawable.ic_white),
                contentDescription = label
            )
            Text(
                text = number,
                color = if (isActive) Color.White else Color(0xff2B1CCC),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = label, fontSize = 14.sp, color = Color.White)
    }
}

@Composable
fun PassengerInputCard(index: Int, passenger: PassengerInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .border(1.dp, Color.Gray)
            .background(Color.White)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row() {
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

    }
}

@Composable
fun PassengerInputForm(
    passenger: PassengerInfo,
    onUpdate: (PassengerInfo) -> Unit,
    onClose: () -> Unit
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
            // Left side: Text + Radio Buttons
            Text(
                "Giới tính:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(Modifier.width(8.dp))
            RadioButton(
                selected = info.gender == "Nam",
                onClick = { info = info.copy(gender = "Nam") }
            )
            Text("Nam")
            Spacer(Modifier.width(16.dp))
            RadioButton(
                selected = info.gender == "Nữ",
                onClick = { info = info.copy(gender = "Nữ") }
            )
            Text("Nữ")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (info.firstName.isBlank() ||
                        info.lastName.isBlank() ||
                        info.dateOfBirth.isBlank() ||
                        info.gender.isBlank() ||
                        (info.idCard.isBlank() && info.passport.isBlank())
                    ) {
                        errorMessage = "Vui lòng nhập đầy đủ Họ, Tên đệm & Tên, Ngày sinh và CMND/CCCD/Mã định danh hoặc số Passport"
                    } else {
                        errorMessage = null
                        onUpdate(info)
                        onClose()
                    }
                }
            ) {
                Text("Lưu")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.lastName,
            onValueChange = { info = info.copy(lastName = it) },
            label = { Text("Họ") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.firstName,
            onValueChange = { info = info.copy(firstName = it) },
            label = { Text("Tên đệm & Tên") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        var dateOfBirthField by remember { mutableStateOf(TextFieldValue(info.dateOfBirth)) }

        OutlinedTextField(
            value = dateOfBirthField,
            onValueChange = { newValue ->
                val oldText = dateOfBirthField.text
                val newInput = newValue.text
                val isDeleting = newInput.length < oldText.length

                // Lọc chỉ lấy số
                val digits = newInput.filter { it.isDigit() }

                // Giới hạn độ dài
                val limitedDigits = digits.take(8)

                // Ghép thành định dạng yyyy-mm-dd
                val formatted = buildString {
                    limitedDigits.forEachIndexed { index, char ->
                        append(char)
                        if ((index == 3 || index == 5) && index != limitedDigits.lastIndex) {
                            append("-")
                        }
                    }
                }

                // Khi xoá thì không tự nhảy con trỏ về vị trí cố định, cứ để tự do
                val newCursorPosition = if (isDeleting) {
                    newValue.selection.end.coerceIn(0, formatted.length)
                } else {
                    // Khi thêm thì tự đẩy con trỏ về sau dấu '-'
                    val diff = formatted.length - newInput.length
                    (newValue.selection.end + diff).coerceIn(0, formatted.length)
                }

                // Cập nhật trạng thái
                dateOfBirthField = TextFieldValue(
                    text = formatted,
                    selection = TextRange(newCursorPosition)
                )
                info = info.copy(dateOfBirth = formatted)
            },
            label = { Text("Ngày sinh(yyyy-mm-dd)") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.phoneNumber,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 10) {
                    info = info.copy(phoneNumber = it)
                }
            },
            label = { Text("Số điện thoại") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.idCard,
            onValueChange = {
                if (it.all { c -> c.isLetterOrDigit() } && it.length <= 12) {
                    info = info.copy(idCard = it)
                }
            },
            label = { Text("CMND/CCCD/Mã định danh") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.passport,
            onValueChange = {
                if (it.all { c -> c.isLetterOrDigit() } && it.length <= 9) {
                    info = info.copy(passport = it)
                }
            },
            label = { Text("Số Passport") },
            modifier = Modifier.fillMaxWidth()
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
    contact: ContactInfo,
    onUpdate: (ContactInfo) -> Unit
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.phoneNumber,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 10) {
                    info = info.copy(phoneNumber = it)
                }
            },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.email,
            onValueChange = { info = info.copy(email = it) },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorMessage != null && !isValidEmail(info.email),
            modifier = Modifier.fillMaxWidth()
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
        Button(
            onClick = {
                if (info.fullName.isBlank()) {
                    errorMessage = "Vui lòng nhập Họ và tên"
                } else if (info.phoneNumber.isBlank() && info.email.isBlank()) {
                    errorMessage = "Vui lòng nhập ít nhất Số điện thoại hoặc Email"
                } else {
                    errorMessage = null
                    onUpdate(info)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Lưu liên hệ")
        }
    }
}

@Composable
fun ContactInfoCard(contact: ContactInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.Gray)
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row {
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
                        fontSize = 13.5.sp,
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

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}