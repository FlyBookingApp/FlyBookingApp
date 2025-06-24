package com.example.veyu.ui.screen.my_ticket

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.veyu.R


@Composable
fun SeatDetailDialog(
    tripType: Boolean,
    isShowDetail: (Boolean) -> Unit,
    isTicket: (Boolean) -> Unit
) {
    var isFist by remember { mutableStateOf(true) }

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
                if (!(isShowDetail == null)) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(20.dp)
                            .clickable {
                                isShowDetail(true)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

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
                if (!tripType) {
                    //FlightItem()
                } else {
                    //FlightItem()
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
                    text = " Mã chuyến bay: VJ12435\n Số lượng hành khách: 3 người",
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
                    item {
                        TicketItem()
                    }

                    item {
                        TicketItem()
                    }

                    item {
                        TicketItem()
                    }

                    item {
                        TicketItem()
                    }

                    item {
                        TicketItem()
                    }
                    item {
                        TicketItem()
                    }
                    item {
                        TicketItem()
                    }
                    item {
                        TicketItem()
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
                        OtherTicketButton(true, isFist = { isFist = it}, !tripType)
                        Spacer(modifier = Modifier.weight(1f))
                        OtherTicketButton(false, isFist = { isFist = it}, tripType)
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 3.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text ="Số ghế: 1A",
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text ="Hạng ghế: Thương gia",
                        fontSize = 12.sp
                    )
                }

                Text(
                    text ="Giá tiền: 1.200.000d",
                    fontSize = 12.sp
                )

                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text ="Mã đặt vé: EP310450",
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text ="Ngày đặt: 20/10/2023",
                        fontSize = 12.sp
                    )
                }
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
            .clickable { isFist(isRoundTrip) }
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