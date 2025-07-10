// TicketTypeScreen.kt
package com.example.veyu.ui.screen.ticket_type

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.R
import com.example.veyu.data.repository.AirportRepository
import com.example.veyu.domain.model.FlightSearchRequest
import com.example.veyu.ui.screen.login.ui.theme.button_color_blue
import com.example.veyu.ui.screen.login.ui.theme.white_form
import com.example.veyu.ui.theme.LightGrayBg
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketTypeScreen(
    onNavigateToMain: (FlightSearchRequest) -> Unit,
    viewModel: TicketTypeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val showLocationPicker = remember { mutableStateOf(false) }
    val showPassengerPicker = remember { mutableStateOf(false) }
    LaunchedEffect(state.isFlightList) {
        if (state.isFlightList) {
            val indexDt = state.airportLocations.indexOf(state.selectedDeparture)
            val indexDn = state.airportLocations.indexOf(state.selectedDestination)

            val request = FlightSearchRequest(
                isRoundTrip = state.isRoundTrip,
                departureCode = state.airportLocationsId.getOrNull(indexDt).orEmpty(),
                destinationCode = state.airportLocationsId.getOrNull(indexDn).orEmpty(),
                departureDate = convertDateFormatLegacy(removeDayOfWeek(state.departureDate)),
                returnDate = if (state.returnDate.isNotEmpty())
                    convertDateFormatLegacy(removeDayOfWeek(state.returnDate)) else state.returnDate,
                passengerInfo = state.passengerInfo
            )

            onNavigateToMain(request)
            viewModel.isFlightList(false)
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
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
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
                    text = "Tìm chuyến bay",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.width(200.dp).height(70.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(white_form)
                        .padding(16.dp)
                ) {
                    // Toggle Segment
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 25.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x00C5C5C5))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(2.dp)
                                .border(2.dp, LightGrayBg, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .background(LightGrayBg),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val isRoundTrip = state.isRoundTrip
                            SegmentedButton(
                                text = "Một chiều",
                                selected = !isRoundTrip,
                                onClick = { viewModel.onToggleTripType(false) }
                            )

                            SegmentedButton(
                                text = "Khứ hồi",
                                selected = isRoundTrip,
                                onClick = { viewModel.onToggleTripType(true) }
                            )

                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Điểm đi",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(LightGrayBg, RoundedCornerShape(8.dp))
                                    .clickable { showLocationPicker.value = true },
                                contentAlignment = Alignment.Center
                            ) {
                                val destinationText = state.selectedDeparture.ifEmpty { "Chọn điểm đến" }
                                val isSelected = state.selectedDeparture.isNotEmpty()

                                Text(
                                    text = destinationText,
                                    color = Color.Black,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            lineHeight = 20.sp
                                )
                            }
                            if (state.selectedDeparture.isNotEmpty()) {
                                val index = state.airportLocations.indexOf(state.selectedDeparture)
                                val code = state.airportLocationsId.getOrNull(index).orEmpty()

                                Text(
                                    text = code,
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(18.dp)) // giữ layout ổn định
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Điểm đến",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .background(LightGrayBg, RoundedCornerShape(8.dp))
                                    .clickable { showLocationPicker.value = true },
                                contentAlignment = Alignment.Center,

                            ) {
                                val destinationText = state.selectedDestination.ifEmpty { "Chọn điểm đến" }
                                val isSelected = state.selectedDestination.isNotEmpty()

                                Text(
                                    text = destinationText,
                                    color = Color.Black,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (state.selectedDestination.isNotEmpty()) {
                                val index = state.airportLocations.indexOf(state.selectedDestination)
                                val code = state.airportLocationsId.getOrNull(index).orEmpty()

                                Text(
                                    text = code,
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            } else {
                                Spacer(modifier = Modifier.height(18.dp)) // giữ layout ổn định
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    if (!state.isRoundTrip) {
                        // Nếu là một chiều, chỉ hiện ngày đi
                        DatePickerItem(
                            title = "Ngày đi",
                            value = if (state.departureDate.isBlank()) "Chọn ngày" else state.departureDate,
                            onClick = { viewModel.showDatePicker(false) }
                        )
                    } else {
                        // Nếu là khứ hồi, hiện cả ngày đi và ngày về
                        Row(modifier = Modifier.fillMaxWidth()) {
                            DatePickerItem(
                                title = "Ngày đi",
                                value = if (state.departureDate.isBlank()) "Chọn ngày" else state.departureDate,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.showDatePicker(false) }
                            )
                            DatePickerItem(
                                title = "Ngày về",
                                value = if (state.returnDate.isBlank()) "Chọn ngày" else state.returnDate,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    if (state.departureDate.isNotBlank()) {
                                        viewModel.showDatePicker(true)
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(modifier = Modifier.fillMaxWidth()
                        .clickable { showPassengerPicker.value = true },){
                        Column(modifier = Modifier.fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                top = 16.dp,
                                bottom = 5.dp),
                            ) {
                            Text(
                                text = "Hành khách",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(R.drawable.ic_double_people),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                                )
                                if(state.passengerInfo.adults >= 1 && state.passengerInfo.children > 0 && state.passengerInfo.infants > 0) {
                                    Text(
                                        text = "${state.passengerInfo.adults} hành khách ${state.passengerInfo.children} trẻ em ${state.passengerInfo.infants} sơ sinh",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                                else if(state.passengerInfo.adults >= 1 && state.passengerInfo.children > 0){
                                    Text(
                                        text = "${state.passengerInfo.adults} hành khách ${state.passengerInfo.children} trẻ em",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                                else if(state.passengerInfo.adults >= 1 && state.passengerInfo.infants > 0){
                                    Text(
                                        text = "${state.passengerInfo.adults} hành khách ${state.passengerInfo.infants} sơ sinh",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                                else if (state.passengerInfo.adults >= 1) {
                                    Text(
                                        text = "${state.passengerInfo.adults} hành khách",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }

                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {viewModel.isFlightList(true)},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = button_color_blue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .height(50.dp),
                            enabled = state.selectedDeparture.isNotEmpty()
                                    && state.selectedDestination.isNotEmpty()
                                    && if (state.isRoundTrip){
                                        state.returnDate.isNotEmpty() && state.departureDate.isNotEmpty()
                                    } else{
                                        state.departureDate.isNotEmpty()
                                    }


                        ) {
                            Text(text = "Tìm chuyến bay")
                        }
                    }

                }
            }
        }
        if (showLocationPicker.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {}
                    .background(Color(0x80000000)) // lớp nền mờ
                    .pointerInput(Unit) {}
            ) {
                LocationPickerSheet(
                    currentDeparture = state.selectedDeparture,
                    currentDestination = state.selectedDestination,
                    viewModel,
                    airportOptions = state.airportLocations,
                    onClose = { showLocationPicker.value = false },
                    onSelect = { dep, dest ->
                        viewModel.onSelectDeparture(dep)
                        viewModel.onSelectDestination(dest)
                        showLocationPicker.value = false
                    }, modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (state.showDatePicker) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {}
                    .background(Color(0x80000000)) // lớp nền mờ

            ) {
                DatePickerSheet(
                    isReturn = state.isReturnPicker,
                    onDateSelected = {
                        if (state.isReturnPicker) viewModel.onSelectReturnDate(it)
                        else viewModel.onSelectDepartureDate(it)
                        viewModel.hideDatePicker()
                    },
                    departureDate = if (state.isRoundTrip) state.departureDate else null,
                    onClose = { viewModel.hideDatePicker() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

        }
        if (showPassengerPicker.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {}
                    .background(Color(0x80000000)) // nền mờ
                    .pointerInput(Unit) {}

            ) {
                PassengerPickerSheet(
                    passengerInfo = state.passengerInfo,
                    onClose = { showPassengerPicker.value = false},
                    onConfirm = { newInfo ->
                        viewModel.setPassengerInfo(newInfo)
                        showPassengerPicker.value = false
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }
}

fun removeDayOfWeek(dateString: String): String {
    return dateString.substringAfter(", ").trim()
}

fun convertDateFormatLegacy(input: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(input)
    return outputFormat.format(date!!)
}

@Composable
fun SegmentedButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFC2F2F9) else LightGrayBg
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .width(130.dp)
            .height(40.dp)
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
fun DatePickerItem(title: String,
                   value: String,
                   onClick: () -> Unit,
                   modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .height(100.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 10.dp, top = 16.dp, bottom = 5.dp),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(end = 8.dp)
            )
            Text(text = value, fontSize = 16.sp, color = Color.Black)
        }
    }
}
