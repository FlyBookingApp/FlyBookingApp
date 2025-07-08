package com.example.veyu.ui.screen.home

import FilterSheet
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.R
import com.example.veyu.ui.screen.login.ui.theme.white_form
import com.example.veyu.ui.screen.home.ui.theme.LightGracho
import com.example.veyu.ui.screen.my_ticket.NothingItemInHere
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(),
               onNavigateToTicketType: () -> Unit,
               onNavigateToAccount: () -> Unit,
               onNavigateToMyTicket: () -> Unit,
               onNavigateToLogin: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isTicketTypeSelected)  {
        if(state.isTicketTypeSelected != false) {
            onNavigateToTicketType()
            viewModel.resetTicketTypeFlag()
        }
    }
    LaunchedEffect(state.isAccessSelsected)  {

        if(state.isAccessSelsected != false) {
            onNavigateToAccount()
            viewModel.resetAccountFlag()
        }
    }

    LaunchedEffect(state.isMyTicketSelected) {
        if (state.isMyTicketSelected != false) {
            onNavigateToMyTicket()
            viewModel.resetMyTicketFlag()
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

            Spacer(modifier = Modifier.height(45.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = Modifier
                        .background(white_form)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        HomeMenuItem(
                            icon = R.drawable.ic_plane,
                            label = "Mua vé",
                            onClick = { viewModel.onTicketTypeClick() }
                        )
                        HomeMenuItem(
                            icon = R.drawable.ic_ticket,
                            label = "Vé của tôi",
                            overlayIcon = R.drawable.ic_people_small,
                            onClick = { viewModel.onMyTicketClick() }
                        )
                        HomeMenuItem(
                            icon = R.drawable.ic_people_nbg,
                            label = "Tài khoản",
                            iconSize = 35.dp,
                            onClick = { viewModel.onAccountClick() }
                        )
                    }
                }
            }
        }
    }

    if (!state.isLogin) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {},
            contentAlignment = Alignment.BottomCenter) {
            isNotLoginDialog(
                onDismiss = { viewModel.resetIsLogin() },
                navLogin = {
                    viewModel.resetIsLogin()
                    onNavigateToLogin()
                }
            )
        }
    }
}

@Composable
fun HomeMenuItem(
    icon: Int,
    label: String,
    overlayIcon: Int? = null,
    iconSize: Dp = 45.dp,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .border(width = 1.dp, color = LightGracho, shape = CircleShape)
                .clickable { onClick() }
                .background(LightGracho, RoundedCornerShape(35.dp)),


            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )

            overlayIcon?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(17.dp)
                        .align(Alignment.Center)
                        .offset(x = -2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
fun isNotLoginDialog(onDismiss: () -> Unit, navLogin: () -> Unit) {
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
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Thông báo", style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(20.dp)
                        .clickable {
                            onDismiss()
                         },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕")
                }
            }

            NothingItemInHere("Chưa đăng nhập", "Đăng nhập để sử dụng tính năng này")

            Button(
                onClick = {
                    navLogin()
                },
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x8063C0FF),
                    contentColor = Color(0xBF2B1CCC)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loginicon),
                    contentDescription = "Login",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}