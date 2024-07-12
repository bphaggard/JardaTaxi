package com.example.jardataxi.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jardataxi.DailyInputViewModel
import com.example.jardataxi.data.DailyInput
import com.example.jardataxi.presentation.PassengerCard
import com.example.jardataxi.ui.theme.rubikFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DailyInputViewModel) {

    val rideIgor by viewModel.inputIgor
    val ridePacka by viewModel.inputPacka
    val ridePatrik by viewModel.inputPatrik
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val passengerList = viewModel.allPassengers.collectAsState(initial = emptyList()).value

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Cestující",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.scrim
                )
            },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PassengerCard(name = "IGOR", viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    PassengerCard(name = "PACKA", viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    PassengerCard(name = "PATRIK", viewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.addDailyInput(
                            DailyInput(
                                igor = rideIgor,
                                packa = ridePacka,
                                patrik = ridePatrik
                            )
                        )
                            viewModel.clearAllInputs()
                            Toast.makeText(context, "Jízdy uloženy!", Toast.LENGTH_SHORT).show()
                            viewModel.showDatabase()
                        }
                    ) {
                        Text(
                            text = "Uložit Jízdy",
                            fontFamily = rubikFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(300.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            items(passengerList) { passenger ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = passenger.igor.toString())
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = passenger.packa.toString())
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = passenger.patrik.toString())
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.deleteDatabase() }
                    ) {
                        Text(
                            text = "Smazat Databázi",
                            fontFamily = rubikFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    )
}