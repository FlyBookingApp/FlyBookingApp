package com.example.veyu.ui.screen.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.veyu.R
import com.example.veyu.ui.screen.seat.BookingNew


@Composable
fun PaymentDialog(
    isShowDetail: (Boolean) -> Unit,
    booking: BookingNew,
    viewModel: DetailBookingViewModel
) {
    var selected by remember { mutableStateOf(false) }
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
                Text("Phương thức thanh toán", style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(20.dp)
                        .clickable {
                            isShowDetail(false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row (modifier = Modifier.fillMaxWidth())
                {
                    BankCardSelector(
                        isSelected = selected,
                        onClick = { selected = true },
                        logo = painterResource(id = R.drawable.mb),
                        bankName = "MBBank",
                        maskedNumber = "****4756"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BankCardSelector(
                        isSelected = !selected,
                        onClick = { },
                        logo = painterResource(id = R.drawable.paymeth),
                        bankName = "Thêm",
                        maskedNumber = "liên kết"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "Tổng số tiền:",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 13.sp,
                        )

                        Text(
                            text = booking.totalPrice,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B1CCC),
                            fontSize = 16.sp,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val totalAmount =  booking.totalPrice
                            .replace(".", "")
                            .replace("đ", "")
                            .trim()
                            .toDouble()
                            viewModel.onConfirm(booking.bookingId, totalAmount)
                            isShowDetail(false)
                        },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .height(48.dp)
                            .width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF63C0FF),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Thanh toán", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BankCardSelector(
    isSelected: Boolean,
    onClick: () -> Unit,
    logo: Painter,
    bankName: String,
    maskedNumber: String
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
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(bankName, fontSize = 14.sp, color = Color.Black)
                Text(maskedNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}