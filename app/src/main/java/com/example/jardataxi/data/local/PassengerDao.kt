package com.example.jardataxi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jardataxi.data.DailyInput
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface PassengerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassenger(dailyInput: DailyInput)

    @Query("SELECT * FROM daily_inputs")
    fun getAllPassengers(): Flow<List<DailyInput>>

    @Query("SELECT SUM(packa) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getPackaTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("SELECT SUM(igor) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getIgorTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("SELECT SUM(patrik) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getPatrikTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("DELETE FROM daily_inputs WHERE id = :id")
    suspend fun deletePassengerById(id: Int)

    @Query("DELETE FROM daily_inputs")
    suspend fun deleteAll()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'daily_inputs'")
    suspend fun deletePrimaryKeyIndex()
}