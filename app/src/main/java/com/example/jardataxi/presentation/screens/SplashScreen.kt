package com.example.jardataxi.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jardataxi.R
import com.example.jardataxi.navigation.Screen
import com.example.jardataxi.ui.theme.JardaTaxiTheme
import com.example.jardataxi.ui.theme.blackOpsFont
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        navController.navigate(Screen.Home.route)
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "JARDA TAXI",
            fontFamily = blackOpsFont,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.taxi),
            contentDescription = "Splash Image"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LottiePreview() {
    JardaTaxiTheme{
        SplashScreen(navController = rememberNavController())
    }
}