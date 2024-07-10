package com.example.jardataxi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jardataxi.DailyInputViewModel
import com.example.jardataxi.R
import com.example.jardataxi.ui.theme.rubikFamily

@Composable
fun PassengerCard(
    name: String,
    viewModel: DailyInputViewModel
) {

    val nameImage = imageMap[name] ?: R.drawable.taxi_passenger
    var rideHalfCount by remember { mutableIntStateOf(0) }
    var rideFullCount by remember { mutableIntStateOf(0) }
    val isHalfButtonEnabled by when (name) {
        "IGOR" -> viewModel.isIgorHalfButtonEnabled
        "PACKA" -> viewModel.isPackaHalfButtonEnabled
        "PATRIK" -> viewModel.isPatrikHalfButtonEnabled
        else -> viewModel.isIgorHalfButtonEnabled // Default case
    }
    val isFullButtonEnabled by when (name) {
        "IGOR" -> viewModel.isIgorFullButtonEnabled
        "PACKA" -> viewModel.isPackaFullButtonEnabled
        "PATRIK" -> viewModel.isPatrikFullButtonEnabled
        else -> viewModel.isIgorFullButtonEnabled // Default case
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .wrapContentHeight(Alignment.CenterVertically),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = nameImage),
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = {
                        when (name){
                            "IGOR" -> viewModel.setInputIgorHalf()
                            "PACKA" -> viewModel.setInputPackaHalf()
                            "PATRIK" -> viewModel.setInputPatrikHalf()
                        }
                        viewModel.setHalfButtonEnabled(name)
                        rideHalfCount ++
                    },
                    enabled = isHalfButtonEnabled && isFullButtonEnabled
                ) {
                    Text(
                        text = "Jednosměrná: $rideHalfCount",
                        fontFamily = rubikFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = {
                        when (name){
                            "IGOR" -> viewModel.setInputIgorFull()
                            "PACKA" -> viewModel.setInputPackaFull()
                            "PATRIK" -> viewModel.setInputPatrikFull()
                        }
                        viewModel.setFullButtonEnabled(name)
                        rideFullCount ++
                    },
                    enabled = isFullButtonEnabled && isHalfButtonEnabled
                ) {
                    Text(
                        text = "Zpáteční: $rideFullCount",
                        fontFamily = rubikFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}