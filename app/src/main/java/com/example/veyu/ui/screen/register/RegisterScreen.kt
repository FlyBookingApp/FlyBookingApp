package com.example.veyu.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.veyu.R
import com.example.veyu.ui.screen.login.ui.theme.button_color_blue
import com.example.veyu.ui.screen.login.ui.theme.white_form

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onNavigateToMain()
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
                        text = "Đăng ký",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RegisterTextField(
                        label = "Email",
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        leadingIcon = R.drawable.ic_email,
                        iconSize = 30.dp,
                        hint = "Nhập Email",
                        keyboardType = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    RegisterTextField(
                        label = "Mật khẩu",
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        leadingIcon = R.drawable.ic_password,
                        iconSize = 20.dp,
                        hint = "Nhập mật khẩu",
                        isPassword = true
                    )

                    RegisterTextField(
                        label = "Xác nhận mật khẩu",
                        value = state.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        leadingIcon = R.drawable.ic_password,
                        iconSize = 20.dp,
                        hint = "Xác nhận mật khẩu",
                        isPassword = true
                    )

                    RegisterTextField(
                        label = "Số điện thoại",
                        value = state.phone,
                        onValueChange = viewModel::onPhoneChange,
                        leadingIcon = R.drawable.ic_call,
                        iconSize = 30.dp,
                        isPhone = true,
                        hint = "Số điện thoại",
                        keyboardType = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = viewModel::onRegisterClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = button_color_blue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        contentPadding = PaddingValues(horizontal = 50.dp, vertical = 10.dp)
                    ) {
                        Text("Đăng ký", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (state.errorMessage.isNotEmpty()) {
                        val isSuccess = state.errorMessage.contains("thành công", ignoreCase = true)
                        val messageColor = if (isSuccess) Color(0xFF2E7D32) else Color.Red

                        Text(
                            text = state.errorMessage,
                            color = messageColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Spacer(modifier = Modifier.height(20.dp))// Giữ chỗ khi chưa có message
                    }



                    Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Quên mật khẩu",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.clickable { onNavigateToForgotPassword() }
                                )
                                Text(
                                    text = "Đăng nhập",
                                    fontSize = 14.sp,
                                    color = Color(0xFF0D47A1),
                                    modifier = Modifier.clickable { onNavigateToLogin() }
                                )
                            }
                        }
                    }
                }
            }
        }



@Composable
fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: Int,
    hint: String,
    isPassword: Boolean = false,
    isPhone: Boolean = false,
    keyboardType: KeyboardOptions = KeyboardOptions.Default,
    iconSize: Dp = 20.dp
) {
    var passwordVisible by remember { mutableStateOf(!isPassword) }
    Text(text = label, fontSize = 14.sp, color = Color.Black)

    TextField(
        value = value,
        onValueChange = {
            if (isPhone && (it.all { c -> c.isDigit() } && it.length <= 10)) {
                onValueChange(it)
            }
            if (!isPhone) {
                onValueChange(it)
            }
        },
        placeholder = {
            val isDark = isSystemInDarkTheme()
            Text(
                text = hint,
                color = if (isDark) Color.Black else Color.Black
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility
                            else R.drawable.ic_visibility_off
                        ),
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                        tint = Color.Gray
                    )
                }
            }
        },
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardType,
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black, // hoặc Color.White tùy nền
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color(0xFFC2F2F9),
            unfocusedContainerColor = Color(0xFFC2F2F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}
