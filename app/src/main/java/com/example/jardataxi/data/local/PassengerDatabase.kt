package com.example.jardataxi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.jardataxi.data.DailyInput
import java.util.Date

@Database(entities = [DailyInput::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PassengerDatabase: RoomDatabase() {
    abstract fun passengerDao(): PassengerDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}