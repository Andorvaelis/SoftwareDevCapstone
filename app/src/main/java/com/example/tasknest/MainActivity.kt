package com.example.tasknest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

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

        sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

        if (!sharedPreferences.contains("username")) {

            sharedPreferences.edit()
                .putString("username", "admin")
                .putString("password", "admin")
                .apply()
        }

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val enteredUsername = txtUsername.text.toString()
            val enteredPassword = txtPassword.text.toString()

            val savedUsername = sharedPreferences.getString("username", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (enteredUsername == savedUsername &&
                enteredPassword == savedPassword) {

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)

            } else {

                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}