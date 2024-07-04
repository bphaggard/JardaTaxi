package com.example.jardataxi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jardataxi.data.DailyInput
import java.util.Date

@Dao
interface PassengerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPassenger(dailyInput: DailyInput)

    @Query("SELECT * FROM daily_inputs WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getWeeklyInputs(startDate: Date, endDate: Date): List<DailyInput>

    @Query("DELETE FROM daily_inputs WHERE id = :id")
    suspend fun deletePassengerById(id: Int)
}