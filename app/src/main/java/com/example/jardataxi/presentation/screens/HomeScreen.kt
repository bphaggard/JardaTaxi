package com.example.jardataxi.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.jardataxi.presentation.getCurrentWeek
import com.example.jardataxi.ui.theme.rubikFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DailyInputViewModel) {

    val rideIgor by viewModel.inputIgor
    val ridePacka by viewModel.inputPacka
    val ridePatrik by viewModel.inputPatrik

    val context = LocalContext.current
    val currentWeek = remember { getCurrentWeek() }
    val showDialog = remember { mutableStateOf(false) }

    val passengersList by viewModel.getAllPassengers().collectAsState(initial = emptyList())

    val packaWeeklyTotal by viewModel.packaWeeklyTotal.collectAsState()
    val igorWeeklyTotal by viewModel.igorWeeklyTotal.collectAsState()
    val patrikWeeklyTotal by viewModel.patrikWeeklyTotal.collectAsState()
    
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        item { Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Igor")
                            Text(text = "Packa")
                            Text(text = "Patrik")
                        } }
                        item { Spacer(modifier = Modifier.size(8.dp)) }
                        item { HorizontalDivider() }
                        item { Spacer(modifier = Modifier.size(8.dp)) }
                        items(passengersList) {passenger ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(50.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = passenger.igor.toString(),
                                        fontSize = 12.sp
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(50.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = passenger.packa.toString(),
                                        fontSize = 12.sp
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(50.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = passenger.patrik.toString(),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
            },
            confirmButton = { 
                Button(onClick = { 
                    showDialog.value = false }) 
                {
                    Text(text = "Zavřít")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog.value = false
                    viewModel.deleteDatabase()
                }) {
                    Text(text = "Smazat Databázi")
                }
            }
        )
    }

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
                            .fillMaxWidth(0.9f)
                            .height(250.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Týdenní cena:",
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Týden: $currentWeek",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Zaplaceno:",
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            RowItem(name = "IGOR", igorWeeklyTotal, viewModel)
                            Spacer(modifier = Modifier.height(4.dp))
                            RowItem(name = "PACKA", packaWeeklyTotal, viewModel)
                            Spacer(modifier = Modifier.height(4.dp))
                            RowItem(name = "PATRIK", patrikWeeklyTotal, viewModel)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Text(
                            text = "Zobrazit Databázi",
                            fontFamily = rubikFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun RowItem(
    name: String,
    value: Int,
    viewModel: DailyInputViewModel
) {
    val igorCheckBox by viewModel.checkBoxStateIgor.collectAsState()
    val packaCheckBox by viewModel.checkBoxStatePacka.collectAsState()
    val patrikCheckBox by viewModel.checkBoxStatePatrik.collectAsState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.3f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = value.toString())
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Checkbox(
                checked = when (name) {
                    "IGOR" -> igorCheckBox
                    "PACKA" -> packaCheckBox
                    "PATRIK" -> patrikCheckBox
                    else -> false
                },
                onCheckedChange = { checked ->
                    viewModel.setCheckBoxState(name, checked)
                }
            )
        }
    }
}