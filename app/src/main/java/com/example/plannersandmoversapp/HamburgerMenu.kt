package com.example.plannersandmoversapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HamburgerMenuScreen(
    title: String,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent { selectedItem ->
                    scope.launch { drawerState.close() } // Close the drawer after item selection
                    when (selectedItem) {
                        "Home" -> {
                            val intent = Intent(context, CompanyProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Profile" -> {
                            val intent = Intent(context, CompanyUserProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Bookings" -> {
                            val intent = Intent(context, CalendarManagerActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Log Out" -> {
                            logOutUser(context) // Handle logout
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
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
                    content(Modifier.padding(innerPadding))
                }
            )
        }
    )
}

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val menuItems = listOf("Home", "Profile", "Bookings", "Log Out")
        menuItems.forEach { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

/**
 * Clears the user session from shared preferences.
 */
fun clearUserSession(context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
}

/**
 * Logs out the user by clearing the session and navigating to the login screen.
 */
fun logOutUser(context: Context) {
    // Clear the user session
    clearUserSession(context)

    // Sign out from Firebase
     FirebaseAuth.getInstance().signOut()

    // Navigate to login activity and clear the entire activity stack
    val intent = Intent(context, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)

    // Finish the current activity to remove it from the back stack
    if (context is Activity) {
        context.finish()
    }
}