package com.example.veyu.ui.screen.flight_list

import android.os.Build
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veyu.R
import com.example.veyu.ui.screen.login.ui.theme.button_color_blue
import com.example.veyu.ui.theme.LightGrayBg
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlightDetailDialog(flight: FlightInfo, onDismiss: () -> Unit, navController: NavController) {
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA5D9E8))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Chi tiết vé", style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(30.dp)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("X")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("${flight.departAirport} - ${flight.arriveAirport}")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(vertical = 10.dp)
                    .border(width = 1.dp, color = Color.Black)
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically // căn giữa hình & chữ theo chiều dọc
                ) {
                    if(flight.airline=="VietJet Air") {
                        Image(
                            painter = painterResource(id = R.drawable.vietjetair),
                            contentDescription = null,
                            modifier = Modifier
                                .width(50.dp)
                                .height(30.dp)
                                .padding(end = 8.dp)
                        )
                    }
                    else{
                        Image(
                            painter = painterResource(id = R.drawable.vietnamairline),
                            contentDescription = null,
                            modifier = Modifier
                                .width(50.dp)
                                .height(30.dp)
                                .padding(end = 8.dp)
                        )
                    }
                    Text(
                        text = "${flight.airline} • ${flight.code}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                DashedDivider(color = Color.Black, thickness = 1f)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(flight.departAirport, style = MaterialTheme.typography.bodyLarge)
                    Text(flight.arriveAirport, style = MaterialTheme.typography.bodyLarge)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text("1h0m", style = MaterialTheme.typography.bodySmall)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("$depart", style = MaterialTheme.typography.bodyLarge)
                    Text("————>", style = MaterialTheme.typography.bodyLarge)
                    Text("$arrive", style = MaterialTheme.typography.bodyLarge)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text("${flight.type} ", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 15.dp)
            ) {
                Text(text = "Thông tin hành lý:", fontWeight = FontWeight.Bold, color = Color.Black)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vali),
                        contentDescription = null,
                        Modifier.padding(end = 5.dp).size(20.dp)
                    )
                    Text("Hành lý ký gửi: Không bao gồm")
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backpack),
                        contentDescription = null,
                        Modifier.padding(end = 5.dp).size(20.dp)
                    )
                    Text("Hành lý xách tay: Tối đa 1 kiện 7kg")
                }
                Text(
                    text = "Thay đổi chuyến bay/ngày bay/hành trình",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "- Phí: 350.000đ (quốc nội) - 800.000đ (quốc tế)",
                    Modifier.padding(start = 10.dp)
                )
                Text(text = "- Thu chênh lệch giá vé", Modifier.padding(start = 10.dp))
                Text(
                    text = "- Thông báo trước tối thiểu 3 giờ so với giờ khởi hành dự kiến",
                    Modifier.padding(start = 10.dp)
                )
                Text(
                    text = "Thay đổi tên khách hàng",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(text = "- Không hỗ trợ", Modifier.padding(start = 10.dp))
                Text(text = "Nâng cao hạng vé", fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "- Thu phí thay đổi", Modifier.padding(start = 10.dp))
                Text(text = "- Thu chênh lệch giá vé", Modifier.padding(start = 10.dp))
                Text(
                    text = "Không có mặt làm thủ tục, hủy chỗ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(text = "- Không hoàn tiền", Modifier.padding(start = 10.dp))
                Text(
                    text = "Hoàn bảo lưu định danh",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(text = "- Có thu phí", Modifier.padding(start = 10.dp))
                Text(text = "Chi tiết giá", fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "${flight.price}", Modifier.padding(start = 10.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightGrayBg)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng số tiền: " + flight.price,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Button(
                    onClick = { navController.navigate("passengerInfor")},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button_color_blue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Chọn vé")
                }
            }
        }
    }
}

