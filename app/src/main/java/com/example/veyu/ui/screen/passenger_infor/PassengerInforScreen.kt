package com.example.veyu.ui.screen.passenger_infor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.veyu.R

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    PassengerInfoScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerInfoScreen(viewModel: PassengerInforViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val bookingState by viewModel.bookingInfo.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var showContactSheet by remember { mutableStateOf(false) }

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
            painter = painterResource(id = R.drawable.background_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Thông tin đặt vé",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .background(Color(0x8000275B), RoundedCornerShape(16.dp))
                    .padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepIndicator("1", "Hành khách", true)
                StepIndicator("2", "Dịch vụ", false)
                StepIndicator("3", "Thanh toán", false)
            }

            if (bookingState.tickets.isNotEmpty()) {
                TicketItem(ticket = bookingState.tickets.first(), isReturnTrip = false)
                TicketItem(ticket = bookingState.tickets.first(), isReturnTrip = true)
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
                                .heightIn(max = 240.dp)
                                .background(Color.White)
                        ) {
                            LazyColumn {
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

                Button(
                    onClick = { /* TODO */ },
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        val logoRes = when (flight.airline) {
            "Vietnam Airlines" -> R.drawable.vietnamairline
            "VietJet Air" -> R.drawable.vietjetair
            else -> null
        }
        logoRes?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(30.dp)
                    .padding(end = 8.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(
                    text = "${flight.departAirport} (${flight.departAirportId}) - ${flight.arriveAirport} (${flight.arriveAirportId})",
                    fontSize = 14.sp
                )
                if(isReturnTrip == false) Image(painter = painterResource(id = R.drawable.ic_depart), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp).height(30.dp)) else Image(painter = painterResource(id = R.drawable.ic_arrive), contentDescription = null, modifier = Modifier.padding(start = 15.dp).width(50.dp).height(30.dp))
            }
            Text(text = "${flight.departTime} - ${flight.arriveTime}", fontSize = 12.sp)
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
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .border(1.dp, Color.Gray)
            .background(Color.White)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Hành khách $index",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF00275B),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Mở thông tin"
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "${passenger.lastName} ${passenger.middleName} ${passenger.firstName}",
            fontSize = 14.sp
        )
    }
}

@Composable
fun PassengerInputForm(
    passenger: PassengerInfo,
    onUpdate: (PassengerInfo) -> Unit,
    onClose: () -> Unit
) {
    var info by remember { mutableStateOf(passenger) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text("Chỉnh sửa hành khách", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = info.lastName, onValueChange = { info = info.copy(lastName = it) }, label = { Text("Họ") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = info.middleName, onValueChange = { info = info.copy(middleName = it) }, label = { Text("Tên đệm") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = info.firstName, onValueChange = { info = info.copy(firstName = it) }, label = { Text("Tên") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = info.dateOfBirth, onValueChange = { info = info.copy(dateOfBirth = it) }, label = { Text("Ngày sinh") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = info.email, onValueChange = { info = info.copy(email = it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = info.idCard, onValueChange = { info = info.copy(idCard = it) }, label = { Text("CMND/CCCD") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onUpdate(info)
            onClose()
        }, modifier = Modifier.align(Alignment.End)) {
            Text("Lưu")
        }
    }
}
@Composable
fun ContactInputForm(
    contact: ContactInfo,
    onUpdate: (ContactInfo) -> Unit
) {
    var info by remember { mutableStateOf(contact) }

    Column(modifier = Modifier
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
            onValueChange = { info = info.copy(phoneNumber = it) },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = info.email,
            onValueChange = { info = info.copy(email = it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onUpdate(info) },
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Người liên hệ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF00275B),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Mở chỉnh sửa"
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "${contact.fullName.ifBlank { "(Chưa có tên)" }} - ${contact.phoneNumber}",
            fontSize = 14.sp
        )
    }
}
