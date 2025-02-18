package com.mobile.computing.hw4

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemperatureViewModel: ViewModel() {
    private val _temperatureState = MutableStateFlow(TemperatureUiState())
    val temperatureState: StateFlow<TemperatureUiState> = _temperatureState.asStateFlow()
    private var temperature: Float = 0.0F

    fun updateTemperature(temp: Float) {
        temperature = temp
        _temperatureState.value = TemperatureUiState(temperature)
    }

    init {
        updateTemperature(12.0F)
    }

}