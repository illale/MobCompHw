package com.mobile.computing.hw4

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DataView(temperature: Float) {
    var temp by remember { mutableFloatStateOf(0.0F) }
    temp = temperature
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(4.dp, Color.Black)
    ) {
        Text(
            text = "Temperature",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
        Text(
            text = "$temp",
            fontSize = 45.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
fun DataLayout(enableNotifications: () -> Unit,
               temperatureViewModel: TemperatureViewModel = viewModel()) {
    val temperatureUiState by temperatureViewModel.temperatureState.collectAsState()
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DataView(temperatureUiState.temperature)

        Button(onClick = enableNotifications) {
            Text(text = "Enable notifications")
        }
    }
}