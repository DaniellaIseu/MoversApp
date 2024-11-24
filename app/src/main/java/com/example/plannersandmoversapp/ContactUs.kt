package com.example.plannersandmoversapp

import android.content.Intent
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


class ContactUsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                ContactUsPage()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsPage() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NormalUserDrawerContent { selectedItem ->
                    scope.launch { drawerState.close() } // Close drawer after item selection
                    when (selectedItem) {
                        "Home" -> {
                            val intent = Intent(context, NormalUserHomeActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Bookings" -> {
                            val intent = Intent(context, BookingActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Tracking" -> {
                            // Uncomment and add your TrackingActivity here
                            // val intent = Intent(context, TrackingActivity::class.java)
                            // context.startActivity(intent)
                        }
                        "Contact Page" -> {
                            val intent = Intent(context, ContactUsActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Moving Companies" -> {
                            // Uncomment and add your MovingCompaniesActivity here
                            // val intent = Intent(context, MovingCompaniesActivity::class.java)
                            // context.startActivity(intent)
                        }
                        "Log Out" -> {
                            logOutNormalUser(context) // Handle logout
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Contact Us") },
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
                    ContactUsContent(modifier = Modifier.padding(innerPadding))
                }
            )
        }
    )
}


@Composable
fun ContactUsContent(modifier: Modifier = Modifier) {
    val database = FirebaseFirestore.getInstance()

    // Form state
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val statusMessage = remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Contact Information
        SectionCard(title = "Contact Information") {
            Text(
                text = "📞 Phone: +254 112 216 902",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "📧 Email: support@awesomemovers.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )

        }

        // Contact Form
        SectionCard(title = "Send Us a Message") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name Field
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Email Field
                OutlinedTextField(
                    value =  email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Message Field
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    label = { Text("Message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )

                // Submit Button
                Button(
                    onClick = {
                        val contactData = mapOf(
                            "name" to name.value,
                            "email" to email.value,
                            "message" to message.value,
                            "timestamp" to System.currentTimeMillis()
                        )
                        database.collection("contact_us")
                            .add(contactData)
                            .addOnSuccessListener {
                                statusMessage.value = "Message sent successfully!"
                                // Clear the form
                                name.value = ""
                                email.value = ""
                                message.value = ""
                            }
                            .addOnFailureListener { exception ->
                                statusMessage.value = "Failed to send message: ${exception.message}"
                            }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Send")
                }
                if (statusMessage.value.isNotEmpty()) {
                    Text(
                        text = statusMessage.value,
                        color = if (statusMessage.value.startsWith("Failed")) Color.Red else Color.Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }}

@Preview(showBackground = true)
@Composable
fun ContactUsPreview() {
    PlannersAndMoversAppTheme {
        ContactUsPage()
    }
}
