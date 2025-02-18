package com.mobile.computing.hw4

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemperatureViewModel private constructor(): ViewModel() {
    private val _temperatureState = MutableStateFlow(TemperatureUiState())
    val temperatureState: StateFlow<TemperatureUiState> = _temperatureState.asStateFlow()
    private var temperature: Float = 0.0F

    fun updateTemperature(temp: Float) {
        temperature = temp
        _temperatureState.value = TemperatureUiState(temperature)
    }

    companion object {
        @Volatile private var instance: TemperatureViewModel? = null

        fun getInstance(): TemperatureViewModel? {
            if (instance == null) {
                instance = TemperatureViewModel()
            }
            return instance
        }

    }

}