package com.example.tasknest

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialization
        val spinner: Spinner = findViewById(R.id.spinnerPriority)
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        val txtTaskTitle = findViewById<EditText>(R.id.txtTaskTitle)
        val txtTaskDesc = findViewById<EditText>(R.id.txtTaskDesc)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)

        // Return to main menu
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // Priority options for add task spinner
        val items = arrayOf(
            "High Priority",
            "Medium Priority",
            "Low Priority"
        )

        // Configuration of spinner adapter
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_text,
            items
        )

        // Layout to dropdown items
        adapter.setDropDownViewResource(R.layout.spinner_text)

        // Attach adapter to spinner
        spinner.adapter = adapter

        // Button to save tasks
        btnSaveTask.setOnClickListener {
            // Retrieve and clean user input
            val title = txtTaskTitle.text.toString().trim()
            val description = txtTaskDesc.text.toString().trim()
            val priority = spinner.selectedItem.toString()

            // Validation of data entry
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter a task title and description.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Access to SharedPreferences
            val sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

            // Pull existing saved tasks from SharedPreferences
            val savedTasks = sharedPreferences
                .getStringSet("tasks", setOf())
                ?.toMutableSet() ?: mutableSetOf()

            // Format task data for storage
            val task = "$title|$description|$priority"

            // Add the new task to the set
            savedTasks.add(task)

            // Save updated task list
            sharedPreferences.edit()
                .putStringSet("tasks", savedTasks)
                .apply()

            // Notify user the task has been saved
            Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show()

            // Clear form fields
            txtTaskTitle.text.clear()
            txtTaskDesc.text.clear()
            spinner.setSelection(0)

            // Navigate to View Tasks activity
            val intent = Intent(this, ViewTasks::class.java)
            startActivity(intent)
            finish()
        }
    }
}