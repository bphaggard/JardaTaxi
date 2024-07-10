package com.example.jardataxi.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jardataxi.data.DailyInput
import java.time.LocalDateTime

@Database(entities = [DailyInput::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PassengerDatabase: RoomDatabase() {
    abstract fun passengerDao(): PassengerDao

    companion object {
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db : SupportSQLiteDatabase) {
                db.execSQL("")
            }

        }

        @Volatile
        private var INSTANCE: PassengerDatabase? = null
        fun getDatabase(context: Context): PassengerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    PassengerDatabase::class.java, "passenger_database")
                    .addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}