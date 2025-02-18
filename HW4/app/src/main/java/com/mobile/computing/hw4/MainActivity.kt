package com.mobile.computing.hw4

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.computing.hw4.ui.theme.HW4Theme
import kotlinx.serialization.Serializable

@Serializable
object Cons;


class MainActivity : ComponentActivity() {
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = Intent(this, TemperatureService::class.java)
        this.startForegroundService(intent)
        enableEdgeToEdge()
        setContent {
            HW4Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Cons) {
                    composable<Cons> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            DataLayout(
                                enableNotifications = {
                                    ActivityCompat.requestPermissions(this@MainActivity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS,
                                            Manifest.permission.FOREGROUND_SERVICE,
                                            Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE),
                                        3)
                                },
                                TemperatureViewModel.getInstance()!!
                            )
                        }
                    }
                }
            }
        }
    }

}

