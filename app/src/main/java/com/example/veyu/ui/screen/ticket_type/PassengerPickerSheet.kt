package com.example.veyu.ui.screen.ticket_type

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PassengerPickerSheet(
    passengerInfo: PassengerInfo,
    onClose: () -> Unit,
    onConfirm: (PassengerInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    var adults by remember { mutableStateOf(passengerInfo.adults) }
    var children by remember { mutableStateOf(passengerInfo.children) }
    var infants by remember { mutableStateOf(passengerInfo.infants) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Hành khách", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                "Đóng",
                color = Color.Blue,
                modifier = Modifier.clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        PassengerRow("Người lớn", "Từ đủ 12 tuổi", adults,
            onDecrement = { if (adults > 1) adults-- },
            onIncrement = { adults++ }
        )

        PassengerRow("Trẻ em", "2 đến dưới 12 tuổi", children,
            onDecrement = { if (children > 0) children-- },
            onIncrement = { children++ }
        )

        PassengerRow("Em bé", "Dưới 2 tuổi", infants,
            onDecrement = { if (infants > 0) infants-- },
            onIncrement = { infants++ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onConfirm(PassengerInfo(adults, children, infants)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
@Composable
fun PassengerRow(
    label: String,
    description: String,
    count: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Text(description, fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrement) {
                Icon(Icons.Default.Remove, contentDescription = "Giảm")
            }
            Text(
                text = count.toString(),
                fontSize = 16.sp,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = onIncrement) {
                Icon(Icons.Default.Add, contentDescription = "Tăng")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
