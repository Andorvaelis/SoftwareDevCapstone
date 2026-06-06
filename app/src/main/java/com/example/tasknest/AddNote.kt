package com.example.tasknest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialization and navigation to main menu
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)

        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // Initialization
        val edtNoteTitle = findViewById<EditText>(R.id.edtNoteTitle)
        val edtNoteContent = findViewById<EditText>(R.id.edtNoteDesc)
        val btnSaveNote = findViewById<Button>(R.id.btnSaveNote)
        val txtPageTitle = findViewById<TextView>(R.id.txtNotePageTitle)

        // // Adds ability to edit AND add through the same activity
        val oldNote = intent.getStringExtra("oldNote")
        val isEditMode = oldNote != null

        if (isEditMode) {
            btnSaveNote.text = "Update Note"
            txtPageTitle.text = "Edit Note"

            val noteParts = oldNote!!.split("|", limit = 2)

            if (noteParts.size == 2) {
                edtNoteTitle.setText(noteParts[0])
                edtNoteContent.setText(noteParts[1])
            }
        }

        // Handles saving the note
        btnSaveNote.setOnClickListener {

            // Retrieve and clean user input
            val title = edtNoteTitle.text.toString().trim()
            val content = edtNoteContent.text.toString().trim()

            // Verify all fields are complete
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please enter a title and note.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Access to SharedPreferences where notes are stored
            val sharedPreferences =
                getSharedPreferences("TaskNestPrefs", MODE_PRIVATE)

            // Retrieves existing notes
            val savedNotes =
                sharedPreferences.getStringSet("notes", mutableSetOf())
                    ?.toMutableSet() ?: mutableSetOf()

            // Format note for storage
            val note = "$title|$content"

            // Add the new note to the set or edit current note
            if (isEditMode) {
                savedNotes.remove(oldNote)
                savedNotes.add(note)

                // Notify the user that the note has been updated
                Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
            } else {
                savedNotes.add(note)

                // Notify the user that the note has been saved
                Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            }

            // Save updated notes set
            sharedPreferences.edit()
                .putStringSet("notes", savedNotes)
                .apply()

            //Navigate to View Notes activity
            val intent = Intent(this, ViewNotes::class.java)
            startActivity(intent)
            finish()
        }
    }
}