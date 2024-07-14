package com.example.jardataxi.presentation

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

fun getCurrentWeek(): Int {
    val currentDate = LocalDate.now()
    val weekFields = WeekFields.of(Locale.getDefault())
    return currentDate.get(weekFields.weekOfWeekBasedYear())
}