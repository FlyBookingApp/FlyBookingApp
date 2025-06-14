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

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    RegisterScreen(
        viewModel = RegisterViewModel(),
        onNavigateToLogin = {},
        onNavigateToMain = {}
    )
}

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
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
                        label = "Tên người dùng",
                        value = state.fullName,
                        onValueChange = viewModel::onFullNameChange,
                        leadingIcon = R.drawable.ic_people,
                        iconSize = 30.dp,
                        hint = "Nhập tên người dùng"
                    )

                    RegisterTextField(
                        label = "Email",
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        leadingIcon = R.drawable.ic_email,
                        iconSize = 30.dp,
                        hint = "Nhập Email"
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
                        label = "Số điện thoại",
                        value = state.phone,
                        onValueChange = viewModel::onPhoneChange,
                        leadingIcon = R.drawable.ic_call,
                        iconSize = 30.dp,
                        hint = "Số điện thoại",
                        keyboardType = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
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
                    Text(
                        text = if (state.errorMessage.isNotEmpty()) state.errorMessage else " ", // Giữ chiều cao
                        color = if (state.errorMessage.isNotEmpty()) Color.Red else Color.Transparent,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )



                    Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
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
    keyboardType: KeyboardOptions = KeyboardOptions.Default,
    iconSize: Dp = 20.dp
) {
    Text(text = label, fontSize = 14.sp, color = Color.Black)

    TextField(
        value = value,
        onValueChange = onValueChange,
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
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
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
