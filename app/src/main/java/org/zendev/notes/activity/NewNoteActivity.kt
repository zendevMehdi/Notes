package org.zendev.notes.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import org.zendev.notes.R
import org.zendev.notes.databinding.ActivityNewNoteBinding
import org.zendev.notes.io.fileExists
import org.zendev.notes.notes.Note
import org.zendev.notes.notes.addNewNote
import org.zendev.notes.utils.errorDialog

class NewNoteActivity : AppCompatActivity() {
    private lateinit var b: ActivityNewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(b.root)

        saveNote()
    }

    private fun saveNote() {
        b.btnSaveNewNote.setOnClickListener {
            if (b.txtNewNoteName.text?.isEmpty()!!) {
                b.txtLayNewNoteName.error = "File name can't be empty."
            } else {
                b.txtLayNewNoteName.error = null

                val newNote = Note(
                    b.txtNewNoteName.text.toString(),
                    b.txtNewNoteContent.text.toString()
                )

                if (fileExists(this, b.txtNewNoteName.text.toString())) {
                    AlertDialog.Builder(this)
                        .setTitle("Note already exists")
                        .setMessage("The note ${b.txtNewNoteName.text.toString()} already exists.\nDo you want to overwrite the file content?")
                        .setCancelable(true)
                        .setIcon(ResourcesCompat.getDrawable(this.resources, R.drawable.ic_file, null))
                        .setPositiveButton("Ok") { _, _ ->
                            createNoteFile(newNote)
                        }.setNegativeButton("Cancel") { _, _ ->
                        }
                        .create()
                        .show()
                } else {
                    createNoteFile(newNote)
                }
            }
        }
    }

    private fun createNoteFile(note: Note) {
        if (addNewNote(this, note)) {
            finish()
        } else {
            errorDialog(this, "Save failed",
                "Error Failed to add new note.\n\nContact the creator to solve the problem.")
                .show()
        }
    }
}