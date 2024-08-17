package com.example.jardataxi.presentation

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Dostupné, Nedostupné, Slábnoucí, Ztracené
    }
}