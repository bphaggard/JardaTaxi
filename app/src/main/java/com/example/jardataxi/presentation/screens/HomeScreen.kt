package com.example.jardataxi.presentation.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.jardataxi.domain.Passenger
import com.example.jardataxi.presentation.PassengerCard
import com.example.jardataxi.presentation.getCurrentWeek
import com.example.jardataxi.ui.theme.rubikFamily
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PassengerViewModel,
    darkTheme: MutableState<Boolean>
) {
    val rideIgor by viewModel.inputIgor
    val ridePacka by viewModel.inputPacka
    val ridePatrik by viewModel.inputPatrik

    val context = LocalContext.current
    val currentWeek = remember { getCurrentWeek() }
    val showDayDialog = remember { mutableStateOf(false) }
    val showWeekDialog = remember { mutableStateOf(false) }

    val passengersList by viewModel.getAllPassengers().collectAsState(initial = emptyList())
    val weekValuesList by viewModel.getAllWeekValues().collectAsState(initial = emptyList())

    val packaWeeklyTotal by viewModel.packaWeeklyTotal.collectAsState()
    val igorWeeklyTotal by viewModel.igorWeeklyTotal.collectAsState()
    val patrikWeeklyTotal by viewModel.patrikWeeklyTotal.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    
    if (showDayDialog.value) {
        AlertDialog(
            onDismissRequest = { showDayDialog.value = false },
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DialogueText(title = "Igor")
                        DialogueText(title = "Packa")
                        DialogueText(title = "Patrik")
                        DialogueText(title = "Datum / Čas")
                    }
                    HorizontalDivider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(passengersList) {passenger ->
                            val instant = passenger.date.toDate().toInstant()
                            val dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
                            val formatter = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))

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
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = formatter,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDayDialog.value = false })
                {
                    Text(text = "Zavřít")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDayDialog.value = false
                    viewModel.deleteDatabase()
                }) {
                    Text(text = "Smazat Databázi")
                }
            }
        )
    }

    if (showWeekDialog.value) {
        AlertDialog(
            onDismissRequest = { showWeekDialog.value = false },
            title = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DialogueText(title = "Igor")
                        DialogueText(title = "Packa")
                        DialogueText(title = "Patrik")
                        DialogueText(title = "Týden")
                    }
                    HorizontalDivider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(weekValuesList) {week ->
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
                                        text = week.igorWeek.toString(),
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
                                        text = week.packaWeek.toString(),
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
                                        text = week.patrikWeek.toString(),
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
                                        text = week.week.toString(),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showWeekDialog.value = false })
                {
                    Text(text = "Zavřít")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showWeekDialog.value = false
                    viewModel.deleteWeekValues()
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
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                actions = {
                    ThemeSwitcher(darkTheme = darkTheme)
                    Spacer(modifier = Modifier.width(15.dp))
                }
            )
        },
        content = { innerPadding ->
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = viewModel::refreshWeeklyTotals
            ) {
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
                                Passenger(
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
                                .height(220.dp),
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
                                        text = "Celkem za týden:",
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                RowItem(name = "IGOR", igorWeeklyTotal)
                                Spacer(modifier = Modifier.height(8.dp))
                                RowItem(name = "PACKA", packaWeeklyTotal)
                                Spacer(modifier = Modifier.height(8.dp))
                                RowItem(name = "PATRIK", patrikWeeklyTotal)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    showDayDialog.value = true
                                }
                            ) {
                                Text(
                                    text = "Databáze Jízd",
                                    fontFamily = rubikFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Button(
                                onClick = {
                                    showWeekDialog.value = true
                                }
                            ) {
                                Text(
                                    text = "Databáze Cen",
                                    fontFamily = rubikFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun RowItem(
    name: String,
    value: Int

) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$value Kč",
                fontFamily = rubikFamily,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DialogueText(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ThemeSwitcher(
    darkTheme: MutableState<Boolean>
) {
    Switch(
        checked = darkTheme.value,
        onCheckedChange = { darkTheme.value = it },
        colors = SwitchDefaults.colors(
            checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            uncheckedTrackColor = MaterialTheme.colorScheme.onSecondaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}