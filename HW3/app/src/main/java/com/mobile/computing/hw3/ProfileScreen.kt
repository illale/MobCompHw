package com.mobile.computing.hw3

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onNavigateToConservation: () -> Unit) {
    Column(
        modifier = Modifier.padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.tintin),
            contentDescription = "Tintin",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tintin",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Hello. I am a Great Tit, " +
                    "and I reside in Finland.",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToConservation) {
            Text(text = "Conservation")
        }
    }
}