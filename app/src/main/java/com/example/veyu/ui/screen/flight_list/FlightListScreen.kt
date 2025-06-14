package com.example.veyu.ui.screen.flight_list

import FilterSheet
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.CardDefaults.shape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veyu.R
import com.example.veyu.ui.theme.LightBlue
import com.example.veyu.ui.theme.LightGrayBg
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightListScreen(
    navController: NavController,
    viewModel: FlightListViewModel = viewModel(),
    onNavigateToMain: () -> Unit
) {


    val flights by viewModel.flights.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    // biến để lưu vé sẽ chọn
    var selectedFlight by remember { mutableStateOf<FlightInfo?>(null) }



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
            TopHeader()
            Spacer(modifier = Modifier.height(15.dp))
            DateTabs()
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
                    onSortOptionSelected = { viewModel.updateSortOption(it) },
                    onShowFilter = { showFilterSheet = true }
                )

            }
        }
    }
    if (selectedFlight != null) {
        FlightDetailDialog(
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
                onApplyFilter = { filter ->
                    viewModel.applyFilter(filter)
                    showFilterSheet = false
                },
                onDismiss = { showFilterSheet = false }
            )
        }
    }

}

@Composable
fun TopHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
    ) {
        Text("Hồ Chí Minh → Nha Trang", style = MaterialTheme.typography.titleMedium)
        Text("1 Khách", style = MaterialTheme.typography.bodySmall)
    }
}
@Composable
fun DateTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateItem("Ngày đi", "12/05/2025")
        DateItem("Ngày về", "13/05/2025")
    }
}

@Composable
fun DateItem(label: String, date: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = date, color = MaterialTheme.colorScheme.primary)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightList(flights: List<FlightInfo>, onFlightClick: (FlightInfo) -> Unit) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
    ) {
        items(flights) { flight ->
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
            val airlineLogo = if (flight.airline == "VietJet Air") {
                R.drawable.vietjetair
            } else {
                R.drawable.vietnamairline
            }

            Image(
                painter = painterResource(id = airlineLogo),
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
    onSortOptionSelected: (SortOption) -> Unit,
    onShowFilter: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .padding(12.dp)
                .clickable { showSheet = true },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { showSheet = true },
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

        // Bottom sheet thủ công
        if (showSheet) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBlue.copy(alpha = 1f))
                    .padding(vertical = 30.dp)
                    .align(Alignment.BottomCenter)
                    .pointerInput(Unit) {}
            )

            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightBlue.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        // Nền: chữ "Sắp xếp theo" căn giữa
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sắp xếp theo",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(40.dp)
                                .clickable { showSheet = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("X")
                        }
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
                                    showSheet = false
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


