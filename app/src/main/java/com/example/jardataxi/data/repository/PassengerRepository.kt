package com.example.jardataxi.data.repository

import com.example.jardataxi.data.DailyInput
import com.example.jardataxi.data.local.PassengerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class PassengerRepository(private val dao: PassengerDao) {
    fun getAllPassengers(): Flow<List<DailyInput>> {
        return dao.getAllPassengers()
    }

    suspend fun addInput(dailyInput: DailyInput) {
        withContext(Dispatchers.IO) {
            dao.insertPassenger(dailyInput)
        }
    }

    suspend fun deletePassengers() {
        dao.deleteAll()
        dao.deletePrimaryKeyIndex()
    }

    suspend fun deleteInputById(id: Int) {
        dao.deletePassengerById(id)
    }

    suspend fun getPackaTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        return withContext(Dispatchers.IO) {
            dao.getPackaTotalForWeek(startDate, endDate)
        }
    }

    suspend fun getIgorTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        return withContext(Dispatchers.IO) {
            dao.getIgorTotalForWeek(startDate, endDate)
        }
    }

    suspend fun getPatrikTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        return withContext(Dispatchers.IO) {
            dao.getPatrikTotalForWeek(startDate, endDate)
        }
    }
}