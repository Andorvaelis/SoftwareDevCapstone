package com.example.tasknest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_notes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialization
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        val editSearchNotes = findViewById<EditText>(R.id.editSearchNotes)
        val btnSearchNotes = findViewById<Button>(R.id.btnSearchNotes)
        val btnShowAllNotes = findViewById<Button>(R.id.btnShowAllNotes)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)

        // Go to main menu
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        val layoutNotesList = findViewById<LinearLayout>(R.id.layoutNotesList)
        val sharedPreferences = getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

        // Function to load and display notes based on search query
        fun loadNotes(searchQuery: String = "") {
            // Clear current display before reloading
            layoutNotesList.removeAllViews()

            // Pull saved notes from SharedPreferences
            val savedNotes = sharedPreferences
                .getStringSet("notes", setOf())
                ?.toMutableSet() ?: mutableSetOf()

            // Clean and standardize
            val cleanSearch = searchQuery
                .replace("*", "")
                .lowercase()
                .trim()

            // Filter notes that match search
            val filteredNotes = if (cleanSearch.isEmpty()) {
                savedNotes
            } else {
                savedNotes.filter { note ->
                    note.lowercase().contains(cleanSearch)
                }.toMutableSet()
            }

            // Display messaged if no notes are found
            if (filteredNotes.isEmpty()) {
                val emptyText = TextView(this)
                emptyText.text = if (searchQuery.isEmpty()) {
                    "No notes saved yet."
                } else {
                    "No matching notes found."
                }
                layoutNotesList.addView(emptyText)
                return
            }

            // Loop through notes that match search criteria
            for (note in filteredNotes) {
                // Split note into title and content
                val parts = note.split("|")
                val title = parts.getOrNull(0) ?: "Untitled"
                val content = parts.getOrNull(1) ?: ""

                // Create TextView to display notes
                val noteText = TextView(this)
                noteText.text = "Title: $title\nNote: $content"
                noteText.textSize = 16f
                noteText.setPadding(0, 12, 0, 8)

                // Add delete note button to note list
                val deleteButton = Button(
                    ContextThemeWrapper(this, R.style.DeleteButtonStyle)
                )
                deleteButton.text = "Delete Note"

                // Handle note deletion
                deleteButton.setOnClickListener {
                    val currentNotes = sharedPreferences
                        .getStringSet("notes", setOf())
                        ?.toMutableSet() ?: mutableSetOf()

                    // Remove selected note
                    currentNotes.remove(note)

                    // Save updated list
                    sharedPreferences.edit()
                        .putStringSet("notes", currentNotes)
                        .apply()

                    // Notify user of deletion
                    Toast.makeText(this, "Note deleted.", Toast.LENGTH_SHORT).show()

                    loadNotes(editSearchNotes.text.toString())
                }

                layoutNotesList.addView(noteText)
                layoutNotesList.addView(deleteButton)
            }
        }

        // Search button
        btnSearchNotes.setOnClickListener {
            loadNotes(editSearchNotes.text.toString())
        }

        // Initial activity load
        loadNotes()

        btnShowAllNotes.setOnClickListener {
            editSearchNotes.text.clear()
            loadNotes()
        }

        // Navigate to Add Note activity
        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }
    }
}