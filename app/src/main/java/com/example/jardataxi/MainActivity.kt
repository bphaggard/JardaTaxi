package com.example.jardataxi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jardataxi.navigation.NavGraph
import com.example.jardataxi.presentation.ConnectivityObserver
import com.example.jardataxi.presentation.NetworkConnectivityObserver
import com.example.jardataxi.presentation.screens.PassengerViewModel
import com.example.jardataxi.ui.theme.JardaTaxiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel: PassengerViewModel by viewModels()
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        enableEdgeToEdge()
        setContent {
            val darkTheme = rememberSaveable { mutableStateOf(false) }

            JardaTaxiTheme(darkTheme = darkTheme.value) {
                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Nedostupné
                )
                Toast.makeText(LocalContext.current, "Připojení k internetu: $status", Toast.LENGTH_SHORT).show()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    NavGraph(navController, viewModel, darkTheme)
                }
            }
        }
    }
}