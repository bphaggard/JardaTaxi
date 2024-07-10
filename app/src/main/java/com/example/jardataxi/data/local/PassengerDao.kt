package com.example.jardataxi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jardataxi.data.DailyInput
import java.time.LocalDateTime

@Dao
interface PassengerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPassenger(dailyInput: DailyInput)

    @Query("SELECT SUM(packa) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getPackaTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("SELECT SUM(igor) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getIgorTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("SELECT SUM(patrik) FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getPatrikTotalForWeek(startDate: LocalDateTime, endDate: LocalDateTime): Int

    @Query("DELETE FROM daily_inputs WHERE id = :id")
    suspend fun deletePassengerById(id: Int)
}