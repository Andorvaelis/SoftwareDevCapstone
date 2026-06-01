package com.example.tasknest

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {

    private lateinit var edtNewUsername: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnRegisterAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtNewUsername = findViewById(R.id.edtNewUsername)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnRegisterAccount = findViewById(R.id.btnRegisterAccount)

        btnRegisterAccount.setOnClickListener {
            registerAccount()
        }
    }

    // Handles account registration
    private fun registerAccount() {
        // Retrieve user entered creds
        val username = edtNewUsername.text.toString().trim()
        val password = edtNewPassword.text.toString()
        val confirmPassword = edtConfirmPassword.text.toString()

        // Verify that required fields are completed
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify passwords match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify password meets security standards
        if (!isPasswordValid(password)) {
            Toast.makeText(
                this,
                "Password must be 8+ characters with uppercase, lowercase, number, and special character.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Account login storage
        val sharedPreferences = getSharedPreferences("TaskNestPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save UN and PW to storage
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()

        // Notify user that account has been created
        Toast.makeText(this, "Account registered successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    // Validates password strength with regex
    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        return passwordPattern.matches(password)
    }
}