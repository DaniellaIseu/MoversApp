package com.example.plannersandmoversapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class CompanyProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                HamburgerMenuScreen(title = "Awesome Movers Inc.") { innerModifier ->
                    CompanyProfileContent(modifier = innerModifier)
                }
            }
        }
    }
}

@Composable
fun CompanyProfileContent(modifier: Modifier = Modifier) {
    // State variables to store user input
    var welcomeText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var promotions by remember { mutableStateOf("") }
    var whyChooseUs by remember { mutableStateOf("") }
    var services by remember { mutableStateOf("") }

    // Firestore instance
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current // Context for Toast messages

    // Fetch data from Firestore on initial load
    LaunchedEffect(Unit) {
        val docRef = firestore.collection("CompanyProfiles").document("AwesomeMoversInc") // replace with actual company doc ID
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                welcomeText = document.getString("welcomeText") ?: ""
                description = document.getString("description") ?: ""
                promotions = document.getString("promotions") ?: ""
                whyChooseUs = document.getString("whyChooseUs") ?: ""
                services = document.getString("services") ?: ""
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
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
                OutlinedTextField(
                    value = welcomeText,
                    onValueChange = { welcomeText = it },
                    label = { Text("Welcome Text") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Promotions Section
        SectionCard(title = "Current Promotions") {
            OutlinedTextField(
                value = promotions,
                onValueChange = { promotions = it },
                label = { Text("Promotions") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Why Choose Us Section
        SectionCard(title = "Why Choose Us?") {
            OutlinedTextField(
                value = whyChooseUs,
                onValueChange = { whyChooseUs = it },
                label = { Text("Why Choose Us") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Our Services Section
        SectionCard(title = "Our Services") {
            OutlinedTextField(
                value = services,
                onValueChange = { services = it },
                label = { Text("Services") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Save Button
        Button(
            onClick = {
                saveToFirestore(
                    firestore,
                    welcomeText,
                    description,
                    promotions,
                    whyChooseUs,
                    services,
                    context
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}

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

/**
 * Function to save updated data to Firestore.
 */
fun saveToFirestore(
    firestore: FirebaseFirestore,
    welcomeText: String,
    description: String,
    promotions: String,
    whyChooseUs: String,
    services: String,
    context: android.content.Context
) {
    val data = hashMapOf(
        "welcomeText" to welcomeText,
        "description" to description,
        "promotions" to promotions,
        "whyChooseUs" to whyChooseUs,
        "services" to services
    )

    firestore.collection("CompanyProfiles")
        .document("AwesomeMoversInc") // replace with actual company doc ID
        .set(data)
        .addOnSuccessListener {
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

/**
 * Preview for testing the layout.
 */
@Preview(showBackground = true)
@Composable
fun CompanyProfilePreview() {
    PlannersAndMoversAppTheme {
        HamburgerMenuScreen(title = "Awesome Movers Inc.") { innerModifier ->
            CompanyProfileContent(modifier = innerModifier)
        }
    }
}
