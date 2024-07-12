package com.example.jardataxi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jardataxi.data.DailyInput
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(entities = [DailyInput::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PassengerDatabase: RoomDatabase() {
    abstract fun passengerDao(): PassengerDao

    companion object {

        @Volatile
        private var INSTANCE: PassengerDatabase? = null
        fun getDatabase(context: Context): PassengerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PassengerDatabase::class.java, "passenger_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            try {
                LocalDateTime.parse(it, formatter)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}