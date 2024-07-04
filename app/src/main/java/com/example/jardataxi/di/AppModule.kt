package com.example.jardataxi.di

import android.app.Application
import androidx.room.Room
import com.example.jardataxi.data.local.PassengerDao
import com.example.jardataxi.data.local.PassengerDatabase
import com.example.jardataxi.data.repository.PassengerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePassengerRepository(dao: PassengerDao): PassengerRepository {
        return PassengerRepository(dao)
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application): PassengerDatabase {
        return Room.databaseBuilder(
            app,
            PassengerDatabase::class.java,
            "passenger_database"
        ).build()
    }

    @Singleton
    @Provides
    fun providePassengerDao(database: PassengerDatabase): PassengerDao {
        return database.passengerDao()
    }
}