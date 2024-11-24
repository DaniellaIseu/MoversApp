package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.auth.FirebaseAuth

class NormalUserHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                NormalUserMenuScreen(
                    title = "Home",
                    /** onLogout = {
                    logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    }**/
                ) { innerModifier ->
                    NormalUserHomeContent(modifier = innerModifier)
                }
            }
        }
    }

    /**   private fun logout() {
    val auth = FirebaseAuth.getInstance()
    auth.signOut()

    // Clear SharedPreferences
    val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    }**/
            }


@Composable
fun NormalUserHomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Introduction Section
        SectionHeader(title = "About the App")
        Text(
            text = "Welcome to Planners and Movers! Our app connects users with reliable moving companies to make your relocation seamless. Whether you're moving homes or offices, we've got you covered.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )

        // Features Section
        SectionHeader(title = "Features")
        FeatureList(
            features = listOf(
                "Easily book moving services from top-rated companies.",
                "Track your move in real-time.",
                "Manage your inventory with ease.",
                "Access secure storage solutions."
            )
        )

        // Steps to Use Section
        SectionHeader(title = "How to Use")
        StepList(
            steps = listOf(
                "1. Create an account or log in to your existing account.",
                "2. Explore available moving services and book one that suits your needs.",
                "3. Manage your inventory and track your move.",
                "4. Enjoy a hassle-free relocation experience!"
            )
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFF6200EE)
    )
}

@Composable
fun FeatureList(features: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        features.forEach { feature ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Feature",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun StepList(steps: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        steps.forEach { step ->
            Text(
                text = step,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalUserMenuScreen(
    title: String,
    onLogout: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6200EE)),
                actions = {
                   /** IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout", tint = Color.White)
                    }**/
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                content(Modifier.padding(16.dp))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NormalUserHomePreview() {
    PlannersAndMoversAppTheme {
        NormalUserMenuScreen(
            title = "Home",
            onLogout = {}
        ) { innerModifier ->
            NormalUserHomeContent(modifier = innerModifier)
        }
    }
}
