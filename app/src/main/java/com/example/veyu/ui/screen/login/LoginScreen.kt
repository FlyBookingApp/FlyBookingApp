package com.example.veyu.ui.screen.login

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.R
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.ui.screen.login.ui.theme.button_color_blue
import com.example.veyu.ui.screen.login.ui.theme.white_form

@Composable
fun WelcomePreview() {
    LoginScreen(
        onNavigateToMain = {},
        onNavigateToRegister = {}
    )
}
@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userPreferences)
    )

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onNavigateToMain()
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
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.width(200.dp).height(70.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .background(white_form)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Text(
                        text = "Đăng nhập",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "Email", fontSize = 14.sp, color = Color.Black)
                    TextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = {
                            Text(
                                text = "Nhập email",
                                color = Color.Black
                            )
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                contentScale = ContentScale.Fit
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color(0xFFC2F2F9),
                            focusedContainerColor = Color(0xFFC2F2F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Mật khẩu", fontSize = 14.sp, color = Color.Black)
                    TextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = {
                            Text(
                                text = "Nhập mật khẩu",
                                color = Color.Black
                            )
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_password),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                contentScale = ContentScale.Fit
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(50),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color(0xFFC2F2F9),
                            focusedContainerColor = Color(0xFFDC2F2F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = viewModel::onLoginClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = button_color_blue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .wrapContentWidth()
                            .height(48.dp)
                            .padding(horizontal = 52.dp)
                    ) {
                        Text("Đăng nhập", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (state.errorMessage.isNotEmpty()) state.errorMessage else "",
                        color = if (state.errorMessage.isNotEmpty()) Color.Red else Color.Transparent,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Quên mật khẩu",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.clickable { /* TODO */ }
                        )
                        Text(
                            text = "Đăng ký",
                            fontSize = 14.sp,
                            color = Color(0xFF03A9F4),
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }
                }
            }
        }
    }
}