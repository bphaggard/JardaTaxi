package com.example.jardataxi.presentation.screens

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jardataxi.domain.Passenger
import com.example.jardataxi.domain.PassengersWeekValues
import com.example.jardataxi.domain.model.PassengerRepository
import com.example.jardataxi.presentation.getCurrentWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class PassengerViewModel @Inject constructor(
    private val repository: PassengerRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    //Pull To Refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        refreshWeeklyTotals()
    }

    fun refreshWeeklyTotals() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000L)
            fetchWeeklyTotals()
            _isRefreshing.value = false
        }
    }

    // Ride Counter
    private val _rideIgorHalfCount = mutableIntStateOf(0)
    val rideIgorHalfCount: State<Int> = _rideIgorHalfCount

    private val _rideIgorFullCount = mutableIntStateOf(0)
    val rideIgorFullCount: State<Int> = _rideIgorFullCount

    private val _ridePackaHalfCount = mutableIntStateOf(0)
    val ridePackaHalfCount: State<Int> = _ridePackaHalfCount

    private val _ridePackaFullCount = mutableIntStateOf(0)
    val ridePackaFullCount: State<Int> = _ridePackaFullCount

    private val _ridePatrikHalfCount = mutableIntStateOf(0)
    val ridePatrikHalfCount: State<Int> = _ridePatrikHalfCount

    private val _ridePatrikFullCount = mutableIntStateOf(0)
    val ridePatrikFullCount: State<Int> = _ridePatrikFullCount

    fun setRideHalfCount(name: String) {
        when (name) {
            "IGOR" -> _rideIgorHalfCount.intValue = 1
            "PACKA" -> _ridePackaHalfCount.intValue = 1
            "PATRIK" -> _ridePatrikHalfCount.intValue = 1
        }
    }

    fun setFullRideCount(name: String) {
        when (name) {
            "IGOR" -> _rideIgorFullCount.intValue = 1
            "PACKA" -> _ridePackaFullCount.intValue = 1
            "PATRIK" -> _ridePatrikFullCount.intValue = 1
        }
    }

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

    fun addDailyInput(dailyInput: Passenger) {
        viewModelScope.launch {
            repository.addInput(dailyInput, context)
            fetchWeeklyTotals() // Fetch weekly totals after adding data
        }
    }

    fun addWeekValue(weekValue: PassengersWeekValues) {
        viewModelScope.launch {
            repository.addWeekValue(weekValue, context)
        }
    }

    // Weekly Total for Passengers
    private val _packaWeeklyTotal = MutableStateFlow(0)
    val packaWeeklyTotal: StateFlow<Int> = _packaWeeklyTotal

    private val _igorWeeklyTotal = MutableStateFlow(0)
    val igorWeeklyTotal: StateFlow<Int> = _igorWeeklyTotal

    private val _patrikWeeklyTotal = MutableStateFlow(0)
    val patrikWeeklyTotal: StateFlow<Int> = _patrikWeeklyTotal

    init {
        fetchWeeklyTotals()
    }

    private fun fetchWeeklyTotals() {
        val nowUtc = LocalDateTime.now(ZoneOffset.UTC) // Get current time in UTC
        val startDate = nowUtc.with(DayOfWeek.MONDAY).with(LocalTime.MIN) // Start of the week (Monday) in UTC
        val endDate = nowUtc.with(DayOfWeek.SUNDAY).with(LocalTime.MAX) // End of the week (Sunday) in UTC

        val startInstant = startDate.toInstant(ZoneOffset.UTC)
        val endInstant = endDate.toInstant(ZoneOffset.UTC)

        getWeeklyTotal("packa", startInstant, endInstant) { total ->
            _packaWeeklyTotal.value = total
        }
        getWeeklyTotal("igor", startInstant, endInstant) { total ->
            _igorWeeklyTotal.value = total
        }
        getWeeklyTotal("patrik", startInstant, endInstant) { total ->
            _patrikWeeklyTotal.value = total
        }
        saveWeeklyTotals()
    }

    private fun getWeeklyTotal(field: String, startDate: Instant, endDate: Instant, callback: (Int) -> Unit) {
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

    private suspend fun areWeekValuesAlreadySaved(): Boolean {
        val currentWeek = getCurrentWeek()
        return repository.checkIfValuesExistForWeek(currentWeek)
    }

    //Saving weekly values to database
    private fun saveWeeklyTotals() {
        viewModelScope.launch {
            val nowUtc = LocalDateTime.now(ZoneOffset.UTC)
            val currentWeek = getCurrentWeek()

            // Check if it's Saturday and values for the current week are not already stored
            if (nowUtc.dayOfWeek == DayOfWeek.SATURDAY && !areWeekValuesAlreadySaved()) {
                val weeklyValues = PassengersWeekValues(
                    igorWeek = _igorWeeklyTotal.value,
                    packaWeek = _packaWeeklyTotal.value,
                    patrikWeek = _patrikWeeklyTotal.value,
                    week = currentWeek
                )

                try {
                    addWeekValue(weeklyValues)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getAllPassengers(): Flow<List<Passenger>> {
        return repository.getAllPassengers()
    }

    fun getAllWeekValues(): Flow<List<PassengersWeekValues>> {
        return repository.getAllWeekValues()
    }

    fun deleteDatabase() {
        viewModelScope.launch {
            repository.deletePassengers()
        }
    }

    fun clearAllInputs() {
        val inputs = listOf(_inputIgor, _inputPacka, _inputPatrik)
        val buttonStates = listOf(
            _isIgorHalfButtonEnabled,
            _isIgorFullButtonEnabled,
            _isPackaHalfButtonEnabled,
            _isPackaFullButtonEnabled,
            _isPatrikHalfButtonEnabled,
            _isPatrikFullButtonEnabled
        )
        val rideCounts = listOf(
            _rideIgorHalfCount,
            _rideIgorFullCount,
            _ridePackaHalfCount,
            _ridePackaFullCount,
            _ridePatrikHalfCount,
            _ridePatrikFullCount
        )

        inputs.forEach { it.intValue = 0 }
        buttonStates.forEach { it.value = true }
        rideCounts.forEach { it.intValue = 0 }
    }
}