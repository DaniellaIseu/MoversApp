package com.example.plannersandmoversapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plannersandmoversapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the isCompany flag passed from the previous activity
        val isCompany = intent.getBooleanExtra("isCompany", false)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Toggle visibility of the company name EditText based on isCompany flag
        if (isCompany) {
            binding.companyName.visibility = android.view.View.VISIBLE
        } else {
            binding.companyName.visibility = android.view.View.GONE
        }

        // Redirect to LoginActivity when the login redirect TextView is clicked
        binding.loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Handle the sign-up button click
        binding.signup.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.confirm.text.toString()

            // Get user type based on the isCompany flag
            val userType = if (isCompany) "company" else "user"

            // Check if fields are filled
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    // Create user with email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {

                        if (it.isSuccessful) {
                            // Save user type to Firestore (optional, but recommended for categorization)
                            // firebaseFirestore.collection("users").add(User(email, userType))

                            firebaseAuth.signOut()
                            // Redirect to different activities based on user type
                            val intent = if (userType == "company") {
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
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


class UserProfileActivity {

}
