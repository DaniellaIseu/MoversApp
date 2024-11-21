package com.example.plannersandmoversapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            PlannersAndMoversAppTheme {
                // Create a NavController to handle navigation between composables
                val navController = rememberNavController()

                // Set up the NavHost for composables
                NavHost(navController = navController, startDestination = "login") {
                    // Define your composable screens (Login, CompanyProfile, etc.)
                    composable("login") { LoginScreen(onLoginClick = { _, _ -> navController.navigate("companyprofile") {
                        // Remove all previous destinations from the back stack
                        popUpTo("login") { inclusive = true } } }, onSignUpRedirectClick = { navController.navigate("signup") }) }
                    composable("signup") { SignUpScreen(onSignUpClick = { _, _, _ -> navController.navigate("login")}, onLoginRedirectClick = { navController.navigate("login") }) }
                    composable("companyprofile") { CompanyHomePage() }
                    // Add other composable screens here as needed
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlannersAndMoversAppTheme {
        Greeting("Android")
        }
}
