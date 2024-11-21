package com.example.plannersandmoversapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import kotlinx.coroutines.launch
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme

class CompanyProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                CompanyHomePage()  // Composable UI for the company profile
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyHomePage() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    //navigation logic
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent { selectedItem ->
                    scope.launch { drawerState.close() }
                    when (selectedItem) {  // Use 'when' to handle different menu options
                        "Log Out" -> {
                            // Handle logout
                            clearUserSession(context)  // Clear session
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.finish() // Finish current activity
                        }}
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Awesome Movers Inc.") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color(0xFF6200EE),
                            titleContentColor = Color.White
                        )
                    )
                },
                content = { innerPadding ->
                    HomeContent(modifier = Modifier.padding(innerPadding))
                }
            )
        }
    )
}

fun clearUserSession(context: Context) {
    // Clear shared preferences or any stored user data
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}

//drawer menu logic
@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        val menuItems = listOf("Home", "Profile", "Services", "Contact Us", "Settings", "Log Out")
        menuItems.forEach { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                            onItemClick(item)
                        }
                    .padding(vertical = 8.dp)
            )
        }
    }
}
//home page body logic
@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {



        // Greeting Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Welcome to Awesome Movers!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6200EE)
                )
                Text(
                    text = "Your trusted moving partner across the country.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        // Promotions Section
        SectionCard(title = "Current Promotions") {
            Text(
                text = "50% off on long-distance moves!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Why Choose Us Section
        SectionCard(title = "Why Choose Us?") {
            Text(
                text = "We provide reliable, efficient, and affordable moving services. With over 10 years of experience, we ensure every move is a smooth experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Our Services Section
        SectionCard(title = "Our Services") {
            Text(
                text = "• Full Move\n• Packing & Unpacking\n• Storage Solutions\n• Furniture Assembly\n• Commercial Moves",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
//UI logic
@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyHomePagePreview() {
    PlannersAndMoversAppTheme {
        CompanyHomePage()
    }
}
