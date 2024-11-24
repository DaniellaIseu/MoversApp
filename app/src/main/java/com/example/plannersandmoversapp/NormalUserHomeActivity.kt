package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.auth.FirebaseAuth

class NormalUserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                HamburgerMenuScreen(
                    title = "Home",
                    onLogout = {
                        logout()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                ) { innerModifier ->
                    NormalUserHomeContent(modifier = innerModifier)
                }
            }
        }
    }

    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        // Clear SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}

@Composable
fun NormalUserHomeContent(modifier: Modifier = Modifier) {
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Heading
        Text(
            text = "Welcome, Normal User!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF6200EE)
        )

        // Search or Input Field
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Enter a search term") },
            modifier = Modifier.fillMaxWidth()
        )

        // Search Button
        Button(
            onClick = { /* Handle Search Action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        // List of Services
        Text(
            text = "Available Services:",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF6200EE)
        )

        // Dummy list of services
        Column(modifier = Modifier.fillMaxWidth()) {
            ServiceCard(serviceName = "Packing Services")
            ServiceCard(serviceName = "Moving Assistance")
            ServiceCard(serviceName = "Storage Solutions")
        }
    }
}

@Composable
fun ServiceCard(serviceName: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = serviceName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamburgerMenuScreen(
    title: String,
    onLogout: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE)),
                actions = {
                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Logout", color = Color.Black)
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                content(Modifier.padding(16.dp))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NormalUserHomePreview() {
    PlannersAndMoversAppTheme {
        HamburgerMenuScreen(
            title = "Home",
            onLogout = {}
        ) { innerModifier ->
            NormalUserHomeContent(modifier = innerModifier)
        }
    }
}
