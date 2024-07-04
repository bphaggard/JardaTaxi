package com.example.jardataxi.data.repository

import com.example.jardataxi.data.DailyInput
import com.example.jardataxi.data.local.PassengerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class PassengerRepository(private val dao: PassengerDao) {
    suspend fun addInput(dailyInput: DailyInput) {
        withContext(Dispatchers.IO) {
            dao.insertPassenger(dailyInput)
        }
    }

    suspend fun deleteInputById(id: Int) {
        dao.deletePassengerById(id)
    }

    suspend fun getWeeklyTotal(startDate: Date, endDate: Date): Int {
        return withContext(Dispatchers.IO) {
            val inputs = dao.getWeeklyInputs(startDate, endDate)
            inputs.sumOf { it.amount }
        }
    }
}