package com.mobile.computing.hw3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.computing.hw3.ui.theme.HW3Theme
import kotlinx.serialization.Serializable

@Serializable
object Cons;
@Serializable
object Profile;
@Serializable
object Writer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HW3Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Cons) {
                    composable<Cons> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            Conversation(
                                MessageDatabase.getDatabase(applicationContext).messageDao().getAll(),
                                onNavigateToProfile = {
                                    navController.navigate(route = Profile) {
                                        popUpTo(Profile) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onNavigateToWriter = {
                                    navController.navigate(route = Writer) {
                                        popUpTo(Writer) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                    composable<Profile> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            ProfileScreen(
                                onNavigateToConservation = {
                                    navController.navigate(route = Cons) {
                                        popUpTo(Cons) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }

                    composable<Writer> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            WriterScreen(
                                onNavigateToConservation = {
                                    navController.navigate(route = Cons) {
                                        popUpTo(Cons) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

