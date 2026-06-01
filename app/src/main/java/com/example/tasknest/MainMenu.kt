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

        // Access to SharedPreferences
        val sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

        // Initialization
        val layoutTasks = findViewById<LinearLayout>(R.id.layoutTasks)
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        val layoutNotes = findViewById<LinearLayout>(R.id.layoutNotes)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnChangePassword = findViewById<Button>(R.id.btnChangePassword)

        // Navigate to View Tasks activity
        layoutTasks.setOnClickListener {

            val intent = Intent(this, ViewTasks::class.java)
            startActivity(intent)
        }

        // Navigate to View Notes activity
        layoutNotes.setOnClickListener {

            val intent = Intent(this, ViewNotes::class.java)
            startActivity(intent)
        }

        // Navigate to Add Task activity
        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }

        // Navigate to Add Note activity
        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }

        // Navigate to Change Password activity
        btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        // Return to login screen
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // TextView to display  preview of tasks
        val txtTasks = findViewById<TextView>(R.id.txtTasks)

        // Retrieve saved tasks from SharedPreferences
        val savedTasks = sharedPreferences.getStringSet("tasks", setOf()) ?: setOf()

        // Display message if no tasks exist
        if (savedTasks.isEmpty()) {
            txtTasks.text = "No tasks saved yet."
        } else {
            val taskPreview = StringBuilder()

            // Display up to 3 tasks on the dashboard (random ones?)
            for (task in savedTasks.take(3)) {
                val parts = task.split("|")
                val title = parts.getOrNull(0) ?: "Untitled"
                val priority = parts.getOrNull(2) ?: ""

                // Add the task info to the preview
                taskPreview.append("$title | $priority\n\n")
            }

            // Display the task preview
            txtTasks.text = taskPreview.toString()
        }

        // TextView to display preview of notes
        val txtNotesList = findViewById<TextView>(R.id.txtNotesList)

        // Retrieve saved notes from SharedPreferences
        val savedNotes = sharedPreferences.getStringSet("notes", setOf()) ?: setOf()

        // Display message if no tasks exist
        if (savedNotes.isEmpty()) {
            txtNotesList.text = "No notes saved yet."
        } else {
            val notesText = StringBuilder()

            // Display up to 3 notes on preview
            for (note in savedNotes.take(3)) {
                val parts = note.split("|")
                val title = parts.getOrNull(0) ?: "Untitled"
                val content = parts.getOrNull(1) ?: ""

                // Add the note info to preview
                notesText.append("Title: $title\n")
                notesText.append("Note: $content\n\n")
            }

            // Display the note preview
            txtNotesList.text = notesText.toString()
        }
    }
}