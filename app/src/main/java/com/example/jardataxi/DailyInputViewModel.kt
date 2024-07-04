package com.example.jardataxi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jardataxi.data.DailyInput
import com.example.jardataxi.data.repository.PassengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DailyInputViewModel @Inject constructor(
    private val repository: PassengerRepository
): ViewModel() {

    fun addDailyInput(dailyInput: DailyInput) {
        viewModelScope.launch {
            repository.addInput(dailyInput)
        }
    }

    fun getWeeklyTotal(startDate: Date, endDate: Date, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val total = repository.getWeeklyTotal(startDate, endDate)
            callback(total)
        }
    }
}