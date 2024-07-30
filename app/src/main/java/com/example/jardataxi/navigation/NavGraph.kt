package com.example.jardataxi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jardataxi.presentation.screens.HomeScreen
import com.example.jardataxi.presentation.screens.PassengerViewModel
import com.example.jardataxi.presentation.screens.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: PassengerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(navController)
        }
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(viewModel)
        }
    }
}