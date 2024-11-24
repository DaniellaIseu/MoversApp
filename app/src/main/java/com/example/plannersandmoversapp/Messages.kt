package com.example.plannersandmoversapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme


class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                HamburgerMenuScreen(title = "Messages") { innerModifier ->
                    MessagesPage(modifier = innerModifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesPage(modifier: Modifier = Modifier) {
    val database = FirebaseFirestore.getInstance()
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        database.collection("contact_us")
            .get()
            .addOnSuccessListener { documents ->
                val fetchedMessages = documents.map { doc ->
                    Message(
                        name = doc.getString("name") ?: "Unknown",
                        email = doc.getString("email") ?: "No Email",
                        message = doc.getString("message") ?: "No Message",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
                messages = fetchedMessages
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messages") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    if (messages.isEmpty()) {
                        Text(
                            text = "No messages found.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(messages) { message ->
                                MessageCard(message = message)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MessageCard(message: Message) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Name: ${message.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Email: ${message.email}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Message: ${message.message}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Timestamp: ${message.timestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

data class Message(
    val name: String,
    val email: String,
    val message: String,
    val timestamp: Long
)

@Preview(showBackground = true)
@Composable
fun MessagesPagePreview() {
    PlannersAndMoversAppTheme {
        MessagesPage()
    }
}}