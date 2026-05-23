package com.example.tasknest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutTasks = findViewById<LinearLayout>(R.id.layoutTasks)
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        val layoutNotes = findViewById<LinearLayout>(R.id.layoutNotes)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        layoutTasks.setOnClickListener {

            val intent = Intent(this, ViewTasks::class.java)
            startActivity(intent)

        }

        layoutNotes.setOnClickListener {

            val intent = Intent(this, ViewNotes::class.java)
            startActivity(intent)

        }

        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }

        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}