package com.example.jardataxi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jardataxi.data.DailyInput
import com.example.jardataxi.data.repository.PassengerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DailyInputViewModel @Inject constructor(
    private val repository: PassengerRepository
): ViewModel() {
    // Half Button States
    private val _isIgorHalfButtonEnabled = mutableStateOf(true)
    val isIgorHalfButtonEnabled: State<Boolean> get() = _isIgorHalfButtonEnabled

    private val _isPackaHalfButtonEnabled = mutableStateOf(true)
    val isPackaHalfButtonEnabled: State<Boolean> get() = _isPackaHalfButtonEnabled

    private val _isPatrikHalfButtonEnabled = mutableStateOf(true)
    val isPatrikHalfButtonEnabled: State<Boolean> get() = _isPatrikHalfButtonEnabled

    // Full Button States
    private val _isIgorFullButtonEnabled = mutableStateOf(true)
    val isIgorFullButtonEnabled: State<Boolean> get() = _isIgorFullButtonEnabled

    private val _isPackaFullButtonEnabled = mutableStateOf(true)
    val isPackaFullButtonEnabled: State<Boolean> get() = _isPackaFullButtonEnabled

    private val _isPatrikFullButtonEnabled = mutableStateOf(true)
    val isPatrikFullButtonEnabled: State<Boolean> get() = _isPatrikFullButtonEnabled

    // Toggle Half Button Enabled State
    fun setHalfButtonEnabled(name: String) {
        when (name) {
            "IGOR" -> _isIgorHalfButtonEnabled.value = !_isIgorHalfButtonEnabled.value
            "PACKA" -> _isPackaHalfButtonEnabled.value = !_isPackaHalfButtonEnabled.value
            "PATRIK" -> _isPatrikHalfButtonEnabled.value = !_isPatrikHalfButtonEnabled.value
        }
    }

    // Toggle Full Button Enabled State
    fun setFullButtonEnabled(name: String) {
        when (name) {
            "IGOR" -> _isIgorFullButtonEnabled.value = !_isIgorFullButtonEnabled.value
            "PACKA" -> _isPackaFullButtonEnabled.value = !_isPackaFullButtonEnabled.value
            "PATRIK" -> _isPatrikFullButtonEnabled.value = !_isPatrikFullButtonEnabled.value
        }
    }

    private val _inputIgor = mutableIntStateOf(0)
    val inputIgor: State<Int> get() = _inputIgor

    fun setInputIgorHalf() {
        _inputIgor.intValue += 50
    }

    fun setInputIgorFull() {
        _inputIgor.intValue += 100
    }

    private val _inputPacka = mutableIntStateOf(0)
    val inputPacka: State<Int> get() = _inputPacka

    fun setInputPackaHalf() {
        _inputPacka.intValue += 50
    }

    fun setInputPackaFull() {
        _inputPacka.intValue += 100
    }

    private val _inputPatrik = mutableIntStateOf(0)
    val inputPatrik: State<Int> get() = _inputPatrik

    fun setInputPatrikHalf() {
        _inputPatrik.intValue += 50
    }

    fun setInputPatrikFull() {
        _inputPatrik.intValue += 100
    }

    fun addDailyInput(dailyInput: DailyInput) {
        viewModelScope.launch {
            repository.addInput(dailyInput)
        }
    }

    fun getWeeklyTotal(field: String, startDate: LocalDateTime, endDate: LocalDateTime, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val total = when (field) {
                "packa" -> repository.getPackaTotalForWeek(startDate, endDate)
                "igor" -> repository.getIgorTotalForWeek(startDate, endDate)
                "patrik" -> repository.getPatrikTotalForWeek(startDate, endDate)
                else -> 0
            }
            callback(total)
        }
    }

    fun clearAllInputs() {
        _inputIgor.intValue = 0
        _inputPacka.intValue = 0
        _inputPatrik.intValue = 0
        _isIgorHalfButtonEnabled.value = true
        _isIgorFullButtonEnabled.value = true
        _isPackaHalfButtonEnabled.value = true
        _isPackaFullButtonEnabled.value = true
        _isPatrikHalfButtonEnabled.value = true
        _isPatrikFullButtonEnabled.value = true
    }
}