package com.mobile.computing.hw2

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
import com.mobile.computing.hw2.ui.theme.HW2Theme
import kotlinx.serialization.Serializable

@Serializable
object Cons;
@Serializable
object Profile;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HW2Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Cons) {
                    composable<Cons> {
                        Surface(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
                            Conversation(CrowTitConversation.conversation,
                                onNavigateToProfile = {
                                    navController.navigate(route = Profile) {
                                        popUpTo(Profile) {
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
                }
            }
        }
    }
}

