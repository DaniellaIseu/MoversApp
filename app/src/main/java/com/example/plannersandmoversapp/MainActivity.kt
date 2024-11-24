package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // User Login Screen
                        composable("login") {
                            UserLoginScreen(navController)
                        }

                        // User SignUp Screen
                        composable("signup") {
                            UserSignUpScreen(navController)
                        }

                        // User Homepage Screen
                        composable("userHome") {
                            startActivity(Intent(this@MainActivity, NormalUserHomeActivity::class.java))
                        }

                        // Company Profile Screen
                        composable("companyProfile") {
                            startActivity(Intent(this@MainActivity, CompanyProfileActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserLoginScreen(navController: NavController) {
    // Placeholder logic for navigating to UserHome or SignUp
    Button(onClick = {
        navController.navigate("userHome")
    }) {
        Text("Log In as User")
    }

    Button(onClick = {
        navController.navigate("signup")
    }) {
        Text("SignUp")
        }
}
@Composable
fun UserSignUpScreen(navController: NavController) {
    // Placeholder UI for the Sign-Up screen
    Button(onClick = {
        // Navigate back to login after signing up
        navController.navigate("login")
    }) {
        Text("Sign Up")
    }
}