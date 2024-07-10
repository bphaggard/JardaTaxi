package com.example.jardataxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "daily_inputs")
data class DailyInput(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val packa: Int,
    val igor: Int,
    val patrik: Int,
    val date: LocalDateTime = LocalDateTime.now()
)