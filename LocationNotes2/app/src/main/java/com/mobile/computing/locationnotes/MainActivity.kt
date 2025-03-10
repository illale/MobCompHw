package com.mobile.computing.locationnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.computing.locationnotes.ui.theme.LocationNotesTheme
import kotlinx.serialization.Serializable

@Serializable
object MapScreen

@Serializable
object ListScreen

class MainActivity : ComponentActivity() {
    private lateinit var markerDatabase: MarkerDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        markerDatabase = MarkerDatabase.getDatabase(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()
        setContent {
            LocationNotesTheme {
                val navController = rememberNavController()
                var isMapSelected by remember { mutableStateOf(true) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                if (isMapSelected) {
                                    Text(text = "Location")
                                } else {
                                    Text(text = "Notes")
                                }
                            },
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { padding ->
                    NavHost(navController = navController, startDestination = MapScreen) {
                        composable<MapScreen> {
                            isMapSelected = true
                            Map(LocalContext.current, this@MainActivity, padding, markerDatabase)
                        }
                        composable<ListScreen> {
                            isMapSelected = false
                            LocationList(padding, markerDatabase)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var isMapSelected by remember { mutableStateOf(true) }
    NavigationBar {
        NavigationBarItem(
            icon = {
               Icon(Icons.Outlined.LocationOn, "Map")
            },
            label = {
                Text(text = "Map")
            },
            selected = isMapSelected,
            onClick = {
                isMapSelected = true
                navController.navigate(route = MapScreen) {
                    popUpTo(MapScreen) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(Icons.AutoMirrored.Outlined.List, "Notes")
            },
            label = {
                Text(text = "Notes")
            },
            selected = !isMapSelected,
            onClick = {
                isMapSelected = false
                navController.navigate(route = ListScreen) {
                    popUpTo(ListScreen) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
