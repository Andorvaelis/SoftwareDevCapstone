package com.example.tasknest

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewTasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_tasks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        val layoutTasksList = findViewById<LinearLayout>(R.id.layoutTasksList)
        val editSearchTasks = findViewById<EditText>(R.id.editSearchTasks)
        val btnSearchTasks = findViewById<Button>(R.id.btnSearchTasks)
        val btnShowAllTasks = findViewById<Button>(R.id.btnShowAllTasks)
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)

        // Go to main menu/dashboard
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // Access to tasks stored in SharedPreferences
        val sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

        // Search and display tasks
        fun loadTasks(searchQuery: String = "") {
            // Clear the current list before reloading
            layoutTasksList.removeAllViews()

            // Pull saved tasks from SharedPreferences
            val savedTasks = sharedPreferences
                .getStringSet("tasks", setOf())
                ?.toMutableSet() ?: mutableSetOf()

            // Clean search input by standardizing
            val cleanSearch = searchQuery
                .replace("*", "")
                .lowercase()
                .trim()

            // Filter tasks based on the search query
            val filteredTasks = if (cleanSearch.isEmpty()) {
                savedTasks
            } else {
                savedTasks.filter { task ->
                    task.lowercase().contains(cleanSearch)
                }.toMutableSet()
            }

            // Display a message if no matching tasks are found
            if (filteredTasks.isEmpty()) {
                val emptyText = TextView(this)
                emptyText.text = if (searchQuery.isEmpty()) {
                    "No tasks saved yet."
                } else {
                    "No matching tasks found."
                }
                layoutTasksList.addView(emptyText)
                return
            }

            // Loops through each task that matches the search
            for (task in filteredTasks) {

                // Splits saved task string into title, description, and prio
                val parts = task.split("|")

                // Pulls task info or returns the given defaults
                val title = parts.getOrNull(0) ?: "Untitled Task"
                val description = parts.getOrNull(1) ?: ""
                val priority = parts.getOrNull(2) ?: ""

                // Create a TextView to display task info
                val taskText = TextView(this)

                // Format and display the task details
                taskText.text =
                    "Task: $title\n" +
                            "Description: $description\n" +
                            "Priority: $priority"

                // Styling and spacing for the task display
                taskText.textSize = 16f
                taskText.setPadding(0, 12, 0, 8)

                // Delete button with label
                val deleteButton = Button(
                    ContextThemeWrapper(this, R.style.DeleteButtonStyle)
                )
                deleteButton.text = "Delete Task"

                // Add edit note button to note list
                val editButton = Button(
                    ContextThemeWrapper(this,R.style.DeleteButtonStyle)
                )
                editButton.text = "Edit Task"

                // Handle note editing
                editButton.setOnClickListener {
                    val intent = Intent(this, AddTask::class.java)
                    intent.putExtra("oldTask", task)
                    startActivity(intent)
                }

                // Delete button listener
                deleteButton.setOnClickListener {

                    // Pull current saved tasks
                    val currentTasks = sharedPreferences
                        .getStringSet("tasks", setOf())
                        ?.toMutableSet() ?: mutableSetOf()

                    // Delete the selected task
                    currentTasks.remove(task)

                    // Save the updated list
                    sharedPreferences.edit()
                        .putStringSet("tasks", currentTasks)
                        .apply()

                    // Notify user the task has been deleted
                    Toast.makeText(
                        this,
                        "Task deleted.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Refresh the task list while preserving the search filter
                    loadTasks(editSearchTasks.text.toString())
                }

                // Create horizontal layout for buttons
                val buttonLayout = LinearLayout(this)
                buttonLayout.orientation = LinearLayout.HORIZONTAL

                // Center the buttons in the layout
                buttonLayout.gravity = Gravity.CENTER_HORIZONTAL

                // Button formatting
                val buttonParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

                buttonParams.marginEnd = 16

                editButton.layoutParams = buttonParams

                deleteButton.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

                // Add buttons to view
                buttonLayout.addView(editButton)
                buttonLayout.addView(deleteButton)

                // Add padding between Edit and Delete buttons
                editButton.setPadding(0, 0, 16, 0)

                layoutTasksList.addView(taskText)
                layoutTasksList.addView(buttonLayout)
            }
        }

        // Functionality for search
        btnSearchTasks.setOnClickListener {
            loadTasks(editSearchTasks.text.toString())
        }

        // Functionality for show all
        btnShowAllTasks.setOnClickListener {
            editSearchTasks.text.clear()
            loadTasks()
        }

        // Initial task list when activity is opened
        loadTasks()

        // Navigate to the Add Task activity
        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }
    }
}