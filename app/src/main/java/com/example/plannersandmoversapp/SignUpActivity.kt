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
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            PlannersAndMoversAppTheme {
                SignUpScreen(
                    onSignUpClick = { email, password, confirmPassword, userType ->
                        handleSignUp(email, password, confirmPassword, userType)
                    },
                    onLoginRedirectClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun handleSignUp(email: String, password: String, confirmPassword: String, userType: String?) {
        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && userType != null) {
            if (password == confirmPassword) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid
                            val user = hashMapOf(
                                "email" to email,
                                "userType" to userType
                            )
                            if (userId != null) {
                                firestore.collection("Users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        firebaseAuth.signOut()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Firestore error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                task.exception?.message ?: "Sign-Up Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String, String?) -> Unit,
    onLoginRedirectClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedUserType by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.loginbkg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Foreground Content (Sign-Up Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .align(Alignment.Center), // Center alignment
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.elevatedCardElevation(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(colorResource(id = R.color.white)), // Replace with your desired color
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Sign Up",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lavender),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_email_24),
                            contentDescription = "Email Icon"
                        )
                    },
                    singleLine = true
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
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password Input
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_lock_24),
                            contentDescription = "Confirm Password Icon"
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // User Type Selection
                Text("Select User Type", fontWeight = FontWeight.Bold, color = Color.Black)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RadioButton(
                        selected = selectedUserType == "Company",
                        onClick = { selectedUserType = "Company" }
                    )
                    Text("Company", Modifier.clickable { selectedUserType = "Company" })

                    RadioButton(
                        selected = selectedUserType == "Client",
                        onClick = { selectedUserType = "Client" }
                    )
                    Text("Client", Modifier.clickable { selectedUserType = "Client" })
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Sign-Up Button
                Button(
                    onClick = { onSignUpClick(email, password, confirmPassword, selectedUserType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.lavender)
                    )
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Login Redirect Text
                Text(
                    text = stringResource(id = R.string.already_a_user_login),
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onLoginRedirectClick() }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen")
@Composable
fun SignUpScreenPreview() {
    PlannersAndMoversAppTheme {
        SignUpScreen(
            onSignUpClick = { _, _, _, _ -> },
            onLoginRedirectClick = {}
        )
    }
}
