package com.example.veyu.ui.screen.seat

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.shimmer
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veyu.R
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.repository.SeatFlightRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.domain.model.Booking
import com.example.veyu.domain.model.BookingRequest
import com.example.veyu.ui.screen.login.ui.theme.button_color_blue
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChooseSeatScreen(
    viewModel: ChooseSeatViewModel = hiltViewModel(),
    request: Booking?,
    onNavigateToMain: (Long) -> Unit,
    onNavigateBackToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val booking by viewModel.booking.collectAsState()

    val bookingId by viewModel.bookingId.collectAsState()
    val seatsBusiness by viewModel.seatsBusiness.collectAsState()
    val seatsEconomy by viewModel.seatsEconomy.collectAsState()
    val seatsPremium by viewModel.seatsPremiuEconomy.collectAsState<List<Seat>>()

    val isDeparture by viewModel.isDeparture.collectAsState()

    LaunchedEffect(request, isDeparture) {
        request?.let {
            viewModel.loadingSeats(it)
            if (isDeparture) viewModel.updateBooking(it)
        }
    }

    // Khi bookingId thay đổi khác null => Navigate
    LaunchedEffect(bookingId) {
        bookingId?.let {
            Log.d("ChooseSeatScreen", "Điều hướng với bookingId mới: $it")
            onNavigateToMain(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
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
                        text = "Chọn chỗ ngồi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
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

            Spacer(modifier = Modifier.height(85.dp))

            Row (
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_sold),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .height(15.dp)
                    )
                    Text("Đã bán", fontSize = 12.sp, color = Color.Black)
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_available),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .height(15.dp)
                    )
                    Text("Còn trống", fontSize = 12.sp, color = Color.Black)
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_hold),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .height(15.dp)
                    )
                    Text("Tạm giữ", fontSize = 12.sp, color = Color.Black)
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_choose),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .height(15.dp)
                    )
                    Text("Đang chọn", fontSize = 12.sp, color = Color.Black)
                }

            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFC2F2F9))
                ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_people_nbg),
                    contentDescription = "Logout",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .height(15.dp)
                )

                Text(
                    text = "${booking.passengerCount.adult} người lớn, ${booking.passengerCount.child} trẻ em, ${booking.passengerCount.infant} em bé",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }

            if(!isDeparture) SeatChooseItem(booking, viewModel)

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            LazySeatBox(
                viewModel,
                seatsBusiness,
                seatsEconomy,
                seatsPremium,
                Modifier.weight(1f).padding(horizontal = 10.dp)
            )

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x4DC3CCD5))
                    .height(60.dp)
                ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Text(
                        text = "Tổng số tiền",
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "${booking.totalPrice}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F19D3),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.weight(3f))

                val context = LocalContext.current
                Button(
                    onClick = {
                            if (viewModel.onClickNext(context)) {
                                Log.d("ChooseSeatScreen", "onClickNext: ${bookingId}")
                            }
                        },
                    modifier = Modifier
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button_color_blue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Xác nhận")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable

fun LazySeatBox(viewModel: ChooseSeatViewModel,seatsBusiness: List<Seat>, seatsEconomy: List<Seat>, seatsPremium: List<Seat>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {

        item{
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (seatsBusiness.size == 0) {
            item {
                SeatLoadingSkeleton()
            }
        } else {
            item {
                SelectableListSeat("Hạng Thương gia\n(${seatsBusiness.firstOrNull()?.price}/ghế)", seatsBusiness, viewModel)
            }
        }

        item {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 16.dp)
            )
        }

        if (seatsPremium.size == 0) {
            item {
                SeatLoadingSkeleton()
            }
        } else {
            item {
                SelectableListSeat("Hạng Phổ thông đặc biệt\n(${seatsPremium.firstOrNull()?.price}/ghế)", seatsPremium, viewModel)
            }
        }

        item {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 16.dp)
            )
        }

        if (seatsEconomy.size == 0) {
            item {
                SeatLoadingSkeleton()
            }
        } else {
            item {
                SelectableListSeat("Hạng Phổ thông\n(${seatsEconomy.firstOrNull()?.price}/ghế)", seatsEconomy, viewModel)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SelectableSeatBox(seatNumber: String, status: String, onClick: () -> Unit) {
    val backgroundColor: Color = when (status) {
        "AVAILABLE" -> Color(0xFF6AECFF)
        "CHOOSE" -> Color(0xFF69FF87)
        "HOLD" -> Color(0xFFFDACAC)
        "BOOKED" -> Color(0xFFD8D7D7)
        "OCCUPIED" -> Color.Gray
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seatNumber,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun SelectableListSeat(title: String, seats: List<Seat>, viewModel: ChooseSeatViewModel) {
    val seatRows = seats.chunked(4)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        // Lặp qua từng nhóm ghế (từng hàng)
        seatRows.forEach { rowOfSeats ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                rowOfSeats.forEachIndexed { index, seat ->
                    SelectableSeatBox(
                        seatNumber = seat.seatNumber,
                        status = seat.status,
                        onClick = {
                            viewModel.onClickSeat(seat.seatNumber, seat.seatClass)
                            viewModel.updatePrice()
                        }
                    )

                    if (index == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else if (index < rowOfSeats.size - 1) {
                        Spacer(modifier = Modifier.width(25.dp))
                    }
                }

                val remainingSeatsInRow = 4 - rowOfSeats.size
                if (remainingSeatsInRow > 0 && remainingSeatsInRow < 3) {
                    (0 until remainingSeatsInRow).forEachIndexed { emptySeatIndex, _ ->
                        // Thêm Spacer trước mỗi ô trống giả (trừ ô đầu tiên của các ô trống giả)
                        if (!(remainingSeatsInRow == 2 && emptySeatIndex == 0)) {
                            Spacer(modifier = Modifier.width(25.dp))
                        }
                        SelectableSeatBox(
                            seatNumber = "",
                            status = "EMPTY",
                            onClick = { viewModel.onClickSeat("", "") }
                        )
                    }
                } else if (remainingSeatsInRow == 3) {
                    (0 until remainingSeatsInRow).forEachIndexed { emptySeatIndex, _ ->
                        // Thêm Spacer trước mỗi ô trống giả (trừ ô đầu tiên của các ô trống giả)
                        if (emptySeatIndex == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            Spacer(modifier = Modifier.width(25.dp))
                        }

                        SelectableSeatBox(
                            seatNumber = "",
                            status = "EMPTY",
                            onClick = { viewModel.onClickSeat("", "") }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SeatLoadingSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = " ",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .width(250.dp)
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

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(
                                    highlightColor = Color.White.copy(alpha = 0.6f)
                                ),
                                color = Color.LightGray
                            )
                    )

                    if (index == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else if (index < 3) {
                        Spacer(modifier = Modifier.width(25.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SeatChooseItem(booking: BookingNew, viewModel: ChooseSeatViewModel) {
    val seatText = buildString {
        val seats = booking.seatNumber
        seats.forEachIndexed { index, seat ->
            append(seat)
            if (index == 3 && seats.size > 4) {
                append("\n") // sau phần tử thứ 4 (index 3) thì xuống dòng
            } else if (index != seats.lastIndex) {
                append(", ") // thêm dấu phẩy trừ phần tử cuối
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.seat),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .padding(end = 8.dp)
        )

        Text(
            text = seatText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_depart),
            contentDescription = null,
            modifier = Modifier
                .height(30.dp)
                .padding(end = 8.dp)
        )

        Image(
            painter = painterResource(R.drawable.delete_ticket),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .padding(start = 8.dp)
                .clickable {
                    viewModel.onClickDelete()
                }
        )
    }
}