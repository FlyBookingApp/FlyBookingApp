package com.example.veyu.ui.screen.navigation

import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.veyu.data.local.UserPreferences
import com.example.veyu.data.remote.NetworkModule
import com.example.veyu.data.repository.AirportRepository
import com.example.veyu.data.repository.FlightRepository
import com.example.veyu.data.repository.SeatFlightRepository
import com.example.veyu.data.repository.UserRepository
import com.example.veyu.domain.model.BookingRequest
import com.example.veyu.ui.screen.account.AccountScreen
import com.example.veyu.ui.screen.flight_list.FlightListScreen
import com.example.veyu.ui.screen.home.HomeScreen
import com.example.veyu.ui.screen.login.LoginScreen
import com.example.veyu.ui.screen.my_ticket.MyTicketScreen
import com.example.veyu.ui.screen.passenger_infor.PassengerInfoScreen
import com.example.veyu.ui.screen.passenger_infor.PassengerInforViewModel
import com.example.veyu.ui.screen.payment.PaymentScreen
import com.example.veyu.ui.screen.register.RegisterScreen
import com.example.veyu.ui.screen.reset_password.NewPasswordScreen
import com.example.veyu.ui.screen.reset_password.NewPasswordViewModel
import com.example.veyu.ui.screen.reset_password.VerifyEmailScreen
import com.example.veyu.ui.screen.reset_password.VerifyOTPScreen
import com.example.veyu.ui.screen.seat.ChooseSeatScreen
import com.example.veyu.ui.screen.splash.SplashScreen
import com.example.veyu.ui.screen.splash.SplashViewModel
import com.example.veyu.ui.screen.ticket_type.TicketTypeScreen
import java.util.Map.entry


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    val sharedSearchViewModel: SharedSearchViewModel = viewModel()
    val bookingViewModel: BookingViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash",modifier=modifier){
        composable("splash"){
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(
                viewModel = splashViewModel,
                onNavigateNext = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Forgot password flow bắt đầu
        navigation(
            startDestination = "verifyEmail",
            route = "forgotPasswordFlow"
        ) {
            composable("verifyEmail") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("forgotPasswordFlow")
                }
                val viewModel: NewPasswordViewModel = hiltViewModel(parentEntry)

                VerifyEmailScreen(
                    viewModel = viewModel,
                    onNavigateToOTP = {
                        navController.navigate("verifyOTP")
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onNavigateToLogin = {
                        navController.navigate("login")
                    }
                )
            }

            composable("verifyOTP") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("forgotPasswordFlow")
                }
                val viewModel: NewPasswordViewModel = hiltViewModel(parentEntry)

                VerifyOTPScreen(
                    viewModel = viewModel,
                    onNavigateToNewPW = {
                        navController.navigate("newPassword")
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onNavigateToLogin = {
                        navController.navigate("login")
                    }
                )
            }

            composable("newPassword") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("forgotPasswordFlow")
                }
                val viewModel: NewPasswordViewModel = hiltViewModel(parentEntry)

                NewPasswordScreen(
                    viewModel = viewModel,
                    onNavigateToMain = {
                        navController.popBackStack("forgotPasswordFlow", inclusive = true)
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onNavigateToLogin = {
                        navController.navigate("login")
                    }
                )
            }
        }

        // Login và các Screen khác....
        composable("login") {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigateUp()
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgotPasswordFlow")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToMain = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgotPasswordFlow")
                }
            )
        }

        composable("home") {
            HomeScreen (
                onNavigateToTicketType ={
                    navController.navigate("ticketType")
                },
                onNavigateToAccount ={
                    navController.navigate("account")
                },
                onNavigateToMyTicket ={
                    navController.navigate("myTicket")
                },
                onNavigateToLogin ={
                    navController.navigate("login")
                }
            )
        }
        composable("ticketType") {
            TicketTypeScreen(
                onNavigateToMain = { request ->
                    sharedSearchViewModel.setRequest(request)
                    navController.navigate("flightList")
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable ("flightList"){
            FlightListScreen(
                navController = navController,
                request = sharedSearchViewModel.request.collectAsState().value,
                onNavigateToMain = { request ->
                    bookingViewModel.setRequest(request)
                    navController.navigate("passengerInfor")
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }

        composable ("passengerInfor"){
            PassengerInfoScreen(
                request = bookingViewModel.request.collectAsState().value,
                onNavigateBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToMain = { request ->
                    bookingViewModel.seatRequestSeat(request)
                    navController.navigate("seat")
                }
            )
        }

        composable("seat") {
            ChooseSeatScreen (
                request = bookingViewModel.requestSeat.collectAsState().value,
                onNavigateToMain = { request ->
                    bookingViewModel.bookingIdRequest(request)
                    navController.navigate("payment")
                },
                onNavigateBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable("payment") {
            PaymentScreen(
                request = bookingViewModel.bookingId.collectAsState().value,
                navController = navController,
            )
        }

        composable("myTicket") {
            MyTicketScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable("account") {
            AccountScreen (
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("account") { inclusive = true }
                    }
                }
            )
        }


    }
}