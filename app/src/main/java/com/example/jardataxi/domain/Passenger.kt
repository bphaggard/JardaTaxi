package com.example.jardataxi.domain

import com.google.firebase.Timestamp

data class Passenger(
    val id: Int = 0,
    val igor: Int = 0,
    val packa: Int = 0,
    val patrik: Int = 0,
    val date: Timestamp = Timestamp.now()
)

data class PassengerCheckBoxState(
    val igorBox: Boolean = false,
    val packaBox: Boolean = false,
    val patrikBox: Boolean = false,
    val date: Timestamp = Timestamp.now()
)