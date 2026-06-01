package com.example.tasknest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // SharedPreferences to store account info
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Access to SharedPreferences storage
        sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

        // Create a default account if one does not exist
        if (!sharedPreferences.contains("username")) {

            sharedPreferences.edit()
                .putString("username", "admin")
                .putString("password", "admin")
                .apply()
        }

        // Initialization of login controls
        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtRegister = findViewById<TextView>(R.id.txtRegister)

        // Handles login attempts
        btnLogin.setOnClickListener {
            // Retrieve login creds
            val enteredUsername = txtUsername.text.toString()
            val enteredPassword = txtPassword.text.toString()

            // Retrieve stored account creds
            val savedUsername = sharedPreferences.getString("username", "")
            val savedPassword = sharedPreferences.getString("password", "")

            // Verify entered creds against stored account info
            if (enteredUsername == savedUsername &&
                enteredPassword == savedPassword) {

                // Notify user of successful login
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // Navigate to Main Menu
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)

            } else {
                // Notify user that login creds are invalid
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to account registration activity
        txtRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}