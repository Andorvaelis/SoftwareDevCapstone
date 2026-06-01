package com.example.tasknest

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChangePassword : AppCompatActivity() {

    private lateinit var edtCurrentPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmNewPassword: EditText
    private lateinit var btnChangePassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Initialize UI
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        // Button to change password
        btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    // Function that handles password change validation and storage
    private fun changePassword() {
        // Retrieve user entered values
        val currentPassword = edtCurrentPassword.text.toString()
        val newPassword = edtNewPassword.text.toString()
        val confirmNewPassword = edtConfirmNewPassword.text.toString()

        // Access saved account info
        val sharedPreferences = getSharedPreferences("TaskNestPrefs", Context.MODE_PRIVATE)
        // Retrieve currently stored password
        val savedPassword = sharedPreferences.getString("password", "")

        // Verify all required fields have been completed
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify current password matches saved password
        if (currentPassword != savedPassword) {
            Toast.makeText(this, "Current password is incorrect.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify new password and confirmation password match
        if (newPassword != confirmNewPassword) {
            Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify new password meets security requirements
        if (!isPasswordValid(newPassword)) {
            Toast.makeText(
                this,
                "Password must be 8+ characters with uppercase, lowercase, number, and special character.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Save the updated password to the accounts in SharedPreferences
        sharedPreferences.edit()
            .putString("password", newPassword)
            .apply()

        // Notify user that password has been changed
        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    // Validates password strength using regex
    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        return passwordPattern.matches(password)
    }
}