package com.example.veyu.ui.screen.ticket_type

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun LocationPickerSheet(
    currentDeparture: String,
    currentDestination: String,
    viewModel: TicketTypeViewModel,
    airportOptions: List<String>,
    onSelect: (String, String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier

) {
    val isLoading by viewModel.isLoading.collectAsState()
    var departure by remember { mutableStateOf(currentDeparture) }
    var destination by remember { mutableStateOf(currentDestination) }

    var searchDep by remember { mutableStateOf("") }
    var searchDest by remember { mutableStateOf("") }

    val filteredDep = airportOptions
        .filter { it != destination } // Không trùng với điểm đến
        .filter { it.contains(searchDep, ignoreCase = true) }

    val filteredDest = airportOptions
        .filter { it != departure }   // Không trùng với điểm đi
        .filter { it.contains(searchDest, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Chọn điểm đi & điểm đến",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Text("✕", modifier = Modifier.clickable { onClose() })
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            //Cột chọn điểm đi
            Column(modifier = Modifier.weight(1f)) {
                Text("Điểm đi", fontWeight = FontWeight.Medium)
                BasicTextField(
                    value = searchDep,
                    onValueChange = { searchDep = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    decorationBox = { innerTextField ->
                        if (searchDep.isEmpty()) {
                            Text("Tìm điểm đi...", fontSize = 14.sp, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .heightIn(max = 280.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (isLoading) {
                        repeat(5) { AirportSkeletonItem() }
                    } else {
                        filteredDep.forEach {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        departure = it
                                        if (it == destination) destination = ""
                                        searchDep = ""
                                    }
                                    .padding(8.dp),
                                color = if (departure == it) Color.Blue else Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Cột chọn điểm đến
            Column(modifier = Modifier.weight(1f)) {
                Text("Điểm đến", fontWeight = FontWeight.Medium)
                BasicTextField(
                    value = searchDest,
                    onValueChange = { searchDest = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    decorationBox = { innerTextField ->
                        if (searchDest.isEmpty()) {
                            Text("Tìm điểm đến...", fontSize = 14.sp, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .heightIn(max = 280.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (isLoading) {
                        repeat(5) { AirportSkeletonItem() }
                    } else {
                        filteredDest.forEach {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        destination = it
                                        if (it == departure) departure = ""
                                        searchDest = ""
                                    }
                                    .padding(8.dp),
                                color = if (destination == it) Color.Red else Color.Black
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSelect(departure, destination) },
            modifier = Modifier.fillMaxWidth(),
            enabled = departure.isNotEmpty() && destination.isNotEmpty()
        ) {
            Text("Xác nhận")
        }
    }
}


@Composable
fun AirportSkeletonItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(20.dp)
            .placeholder(
                visible = true,
                color = Color.LightGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White.copy(alpha = 0.6f)
                )
            )
    )
}