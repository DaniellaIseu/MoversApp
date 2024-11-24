package com.example.plannersandmoversapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class CompanyUserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                HamburgerMenuScreen(title = "Company Profile") { innerModifier ->
                    CompanyUserProfileContent(modifier = innerModifier)
                }
            }
        }
    }
}

@Composable
fun CompanyUserProfileContent(modifier: Modifier = Modifier) {
    // Detect if running in Compose Preview
    val isInPreview = LocalInspectionMode.current

    // State variables for user input
    var userName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var companyEmail by remember { mutableStateOf("") }
    var companyLocation by remember { mutableStateOf("") }

    // Firestore instance (only initialized if not in Preview)
    val firestore = if (!isInPreview) FirebaseFirestore.getInstance() else null
    val context = LocalContext.current // Context for Toast messages

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input fields
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = companyEmail,
            onValueChange = { companyEmail = it },
            label = { Text("Company Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = companyLocation,
            onValueChange = { companyLocation = it },
            label = { Text("Company Location") },
            modifier = Modifier.fillMaxWidth()
        )

        // Save Button
        Button(
            onClick = {
                if (!isInPreview) {
                    // Call the function to save data to Firestore
                    saveToFirestore(
                        firestore,
                        userName,
                        companyName,
                        phoneNumber,
                        companyEmail,
                        companyLocation,
                        context
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

/**
 * Function to save user profile data to Firestore.
 */
fun saveToFirestore(
    firestore: FirebaseFirestore?,
    userName: String,
    companyName: String,
    phoneNumber: String,
    companyEmail: String,
    companyLocation: String,
    context: android.content.Context
) {
    // Validation: Ensure no fields are empty
    if (userName.isBlank() || companyName.isBlank() || phoneNumber.isBlank() || companyEmail.isBlank() || companyLocation.isBlank()) {
        Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
        return
    }

    // Prepare data to be saved
    val data = hashMapOf(
        "userName" to userName,
        "companyName" to companyName,
        "phoneNumber" to phoneNumber,
        "companyEmail" to companyEmail,
        "companyLocation" to companyLocation
    )

    // Save data to Firestore under the "CompanyProfiles" collection
    firestore?.collection("CompanyProfileData")
        ?.add(data)
        ?.addOnSuccessListener {
            // Success feedback to user
            Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
        }
        ?.addOnFailureListener { e ->
            // Failure feedback to user
            Toast.makeText(context, "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

/**
 * Preview for testing the layout.
 */
@Preview(showBackground = true)
@Composable
fun CompanyUserProfilePreview() {
    PlannersAndMoversAppTheme {
        HamburgerMenuScreen(title = "Company Profile") { innerModifier ->
            // Pass mocked data for the preview
            CompanyUserProfileContent(modifier = innerModifier)
        }
    }
}
