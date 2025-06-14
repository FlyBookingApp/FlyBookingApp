package com.example.veyu.ui.screen.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.veyu.ui.screen.flight_list.FlightListScreen
import com.example.veyu.ui.screen.home.HomeScreen
import com.example.veyu.ui.screen.login.LoginScreen
import com.example.veyu.ui.screen.passenger_infor.PassengerInfoScreen
import com.example.veyu.ui.screen.register.RegisterScreen
import com.example.veyu.ui.screen.splash.SplashScreen
import com.example.veyu.ui.screen.ticket_type.TicketTypeScreen



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash",modifier=modifier){
        composable("splash"){
            SplashScreen(
                onNavigateNext = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToMain = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToMain = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
        composable("home") {
            HomeScreen (
                onNavigateToTicketType ={
                    navController.navigate("ticketType")
                }
            )
        }
        composable ("ticketType"){
            TicketTypeScreen(
                onNavigateToMain = {
                    navController.navigate("flightList")
                }
            )
        }
        composable ("flightList"){
            FlightListScreen(
                navController = navController,
                onNavigateToMain = {
                    navController.navigate("passengerInfor")
                }
            )
        }
        composable ("passengerInfor"){
            PassengerInfoScreen(

            )
        }
    }
}