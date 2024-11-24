package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            PlannersAndMoversAppTheme {
                LoginScreen(
                    onLoginClick = { email, password ->
                        handleLogin(email, password)
                    },
                    onSignUpRedirectClick = {
                        val intent = Intent(this, SignUpActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun handleLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        checkUserType(user.uid) // Fetch user type from Firestore
                    }
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUserType(userId: String) {
        val userRef = firestore.collection("Users").document(userId)

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot? = task.result
                if (document != null && document.exists()) {
                    val userType = document.getString("userType")
                    when (userType) {
                        "Company" -> {
                            // Redirect to Company Profile
                            val intent = Intent(this@LoginActivity, CompanyProfileActivity::class.java)
                            startActivity(intent)
                            finish()  // Close login activity
                        }
                        "Client" -> {
                            // Redirect to Normal User Home
                            val intent = Intent(this@LoginActivity, NormalUserHomeActivity::class.java)
                            startActivity(intent)
                            finish()  // Close login activity
                        }
                        else -> {
                            Toast.makeText(this@LoginActivity, "Invalid user type", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "User not found in database", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Error retrieving user type", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val user = firebaseAuth.currentUser
            user?.let {
                checkUserType(user.uid)
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onSignUpRedirectClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.loginbkg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.elevatedCardElevation(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(colorResource(id = R.color.white)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lavender),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_person_24),
                            contentDescription = "Email Icon"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_lock_24),
                            contentDescription = "Password Icon"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Login Button
                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.lavender)
                    )
                ) {
                    Text(
                        text = "Login",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sign-Up Redirect
                Text(
                    text = stringResource(id = R.string.not_yet_registered_signup),
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onSignUpRedirectClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    PlannersAndMoversAppTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onSignUpRedirectClick = {}
        )
    }
}
