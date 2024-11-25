package com.example.plannersandmoversapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userEmail = currentUser?.email

                if (userEmail != null) {
                    NormalUserMenuScreen(title = "Booking List") { innerModifier ->
                        BookingListPage(userEmail = userEmail, modifier = innerModifier)
                    }
                } else {
                    // Handle case where user is not signed in
                    Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingListPage(userEmail: String,modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    val bookings = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        db.collection("Bookings")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                val bookingList = documents.map { it.data }
                bookings.value = bookingList
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch bookings: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Bookings for $userEmail", style = MaterialTheme.typography.titleMedium)

            if (bookings.value.isEmpty()) {
                Text(text = "No bookings found", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(bookings.value) { booking ->
                        BookingCard(booking)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Map<String, Any>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Booking Type: ${booking["bookingType"] ?: "N/A"}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Property Type: ${booking["propertyType"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Pickup Location: ${booking["pickupLocation"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Drop-Off Location: ${booking["dropOffLocation"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Pickup Date: ${booking["pickupDate"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Drop-Off Date: ${booking["dropOffDate"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Payment Method: ${booking["paymentMethod"] ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BookingListPreview() {
    PlannersAndMoversAppTheme {
        BookingListPage(userEmail = "testuser@example.com")
    }
}

