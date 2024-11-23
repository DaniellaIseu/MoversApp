package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plannersandmoversapp.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupRedirectText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // After successful login, check if user is a company or a regular user
                        val intent = if (isCompanyUser()) {
                            Intent(this, CompanyProfileActivity::class.java)
                        } else {
                            Intent(this, UserProfileActivity::class.java)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            // Check if the user is a company or normal user
            val intent = if (isCompanyUser()) {
                Intent(this, CompanyProfileActivity::class.java)
            } else {
                Intent(this, UserProfileActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private fun isCompanyUser(): Boolean {
        // You can check from Firebase Database/Firestore if this user is a company or user
        // For now, we are assuming they are a regular user (Modify this as needed)
        // Example:
        // val userType = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid!!).get().result.get("userType")
        return false // For demonstration, assume false for normal user
    }
}
