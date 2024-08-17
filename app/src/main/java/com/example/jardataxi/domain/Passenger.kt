package com.example.jardataxi.domain

import com.google.firebase.Timestamp

data class Passenger(
    val id: Int = 0,
    val igor: Int = 0,
    val packa: Int = 0,
    val patrik: Int = 0,
    val date: Timestamp = Timestamp.now()
)

data class PassengersWeekValues(
    val week: Int = 0,
    val igorWeek: Int = 0,
    val packaWeek: Int = 0,
    val patrikWeek: Int = 0
)