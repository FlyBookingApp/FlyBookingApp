package com.example.veyu.ui.screen.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.R
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserFromLocalStore()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hình nền
        Image(
            painter = painterResource(id = R.drawable.backgroundacc),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = "Thông tin tài khoản",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Avatar
            Image(
                painter = painterResource(id = R.drawable.icon_user_circle),
                contentDescription = "ImageAccount",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Tên người dùng
            Box(
                modifier = Modifier
                    .background(Color(0x6600275B), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp)
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = user?.username ?: "Đang tải...",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 120.dp, start = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = "Email icon",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = "Email: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = user?.email ?: "Đang tải...",
                        fontSize = 16.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.phone),
                        contentDescription = "Phone icon",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = "Phone: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = user?.phone ?: "Đang tải...",
                        fontSize = 16.sp
                    )
                }
            }

            // Divider
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Nút đăng xuất
            Button(
                onClick = {
                    scope.launch {
                        viewModel.onLogoutClick()
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x26FF0004),
                    contentColor = Color(0xFFF70C10)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Đăng xuất", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
