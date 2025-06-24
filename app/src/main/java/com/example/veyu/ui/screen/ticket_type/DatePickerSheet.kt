package com.example.veyu.ui.screen.ticket_type

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerSheet(
    isReturn: Boolean,
    onDateSelected: (String) -> Unit,
    departureDate: String? = null,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    // Danh sách ngày tháng
    val dates = remember(currentMonth) {
        val firstDayOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
        val totalDays = currentMonth.lengthOfMonth()
        val blankDays = List(firstDayOfWeek) { null }
        val days = List(totalDays) { day -> currentMonth.atDay(day + 1) }
        val allDays = blankDays + days
        val totalCells = 42
        allDays + List(totalCells - allDays.size) { null }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .background(Color.White)
            .padding(15.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Chọn thời gian",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Text("✕", modifier = Modifier.clickable { onClose() })
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 75.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ngày đi", color = if (!isReturn) Color.Blue else Color.Gray)
            Text("|")
            Text("Ngày về", color = if (isReturn) Color.Red else Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Điều khiển chuyển tháng
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "<",
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { currentMonth = currentMonth.minusMonths(1) }
                    .padding(8.dp)
            )

            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("vi", "VN"))}, ${currentMonth.year}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Text(
                text = ">",
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { currentMonth = currentMonth.plusMonths(1) }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hàng tiêu đề thứ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Lưới ngày
        LazyVerticalGrid(columns = GridCells.Fixed(7), content = {
            items(dates) { date ->
                val isToday = date == today
                val isSelected = date != null && date == selectedDate
                val isDisabled = date != null && date.isBefore(today)

                val backgroundColor = when {
                    isSelected -> Color(0xFF007BFF)
                    isToday -> Color(0xFFAAF683) // xanh lá cho hôm nay
                    else -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(40.dp)
                        .background(
                            color = backgroundColor,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable(
                            enabled = date != null && !isDisabled
                        ) {
                            date?.let { selectedDate = it }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date?.dayOfMonth?.toString() ?: "",
                        color = when {
                            isDisabled -> Color.LightGray
                            isSelected -> Color.White
                            else -> Color.Black
                        }
                    )
                }
            }
        })

        Spacer(modifier = Modifier.height(5.dp))

        val formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale("vi", "VN"))
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp
            )
        } ?: Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = {
                errorMessage = null
                if (!isReturn || departureDate == null) {
                    onDateSelected(selectedDate.format(formatter))
                } else {
                    val departure = LocalDate.parse(
                        departureDate,
                        formatter
                    )
                    if (selectedDate.isAfter(departure)) {
                        onDateSelected(selectedDate.format(formatter))
                    } else {
                        errorMessage = "Vui lòng chọn ngày sau ngày đi (${departure.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))})"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !selectedDate.isBefore(today) // Disable nếu chọn sai
        ) {
            Text("Xác nhận")
        }


    }
}
