package com.example.plannersandmoversapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

class UserMovingCompanyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                NormalUserMenuScreen(title = "Moving Company") { innerModifier ->
                    MovingCompanyScreen(modifier = innerModifier)
                }
            }
        }
    }
}

@Composable
fun MovingCompanyScreen(modifier: Modifier = Modifier) {
    // State variables to store Firestore data
    var welcomeText by remember { mutableStateOf("Loading...") }
    var description by remember { mutableStateOf("Loading...") }
    var promotions by remember { mutableStateOf("Loading...") }
    var whyChooseUs by remember { mutableStateOf("Loading...") }
    var services by remember { mutableStateOf("Loading...") }

    // Firestore instance
    val firestore = FirebaseFirestore.getInstance()

    // Fetch data from Firestore
    LaunchedEffect(Unit) {
        val docRef = firestore.collection("CompanyProfiles").document("AwesomeMoversInc") // Replace with dynamic doc ID if needed
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                welcomeText = document.getString("welcomeText") ?: "Welcome text not available"
                description = document.getString("description") ?: "Description not available"
                promotions = document.getString("promotions") ?: "Promotions not available"
                whyChooseUs = document.getString("whyChooseUs") ?: "Why Choose Us text not available"
                services = document.getString("services") ?: "Services not available"
            }
        }.addOnFailureListener {
            welcomeText = "Failed to load data"
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Section
        SectionCard1(title = "Welcome") {
            Text(
                text = welcomeText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }

        // Description Section
        SectionCard1(title = "About Us") {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }

        // Promotions Section
        SectionCard1(title = "Promotions") {
            Text(
                text = promotions,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }

        // Why Choose Us Section
        SectionCard1(title = "Why Choose Us?") {
            Text(
                text = whyChooseUs,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }

        // Services Section
        SectionCard1(title = "Our Services") {
            Text(
                text = services,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun SectionCard1(title: String, content: @Composable ColumnScope.() -> Unit) {
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
fun MovingCompanyPreview() {
    PlannersAndMoversAppTheme {
        MovingCompanyScreen()
    }
}