package com.example.jardataxi.navigation

sealed class Screen(
    val title: String,
    val route: String
) {
    data object Home: Screen(
        title = "Home",
        route = "home_screen"
    )
    data object Splash: Screen(
        title = "Splash",
        route = "lottie_splash"
    )
}