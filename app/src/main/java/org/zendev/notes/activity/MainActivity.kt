package org.zendev.notes.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import org.zendev.notes.adapter.NotesAdapter
import org.zendev.notes.databinding.ActivityMainBinding
import org.zendev.notes.io.checkNotesFolder
import org.zendev.notes.notes.getAllNotes
import org.zendev.notes.utils.copyTextClipboard
import org.zendev.notes.utils.errorDialog
import org.zendev.notes.utils.variables
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (checkNotesFolder(this)) {
            loadNotes()
        } else {
            errorDialog(this, "No folder created",
                "Error Failed to create the notes folder.\n\nApplication will terminate after closing this dialog.",
                true)
                .show()
        }

        b.btnAddNewNote.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewNoteActivity::class.java));
        }

        b.btnRefreshNotes.setOnClickListener {
            loadNotes()
        }

        b.btnAbout.setOnClickListener {
            val message =
                "This application lets you save notes in your device easily.\nYou can also edit or delete notes.\n\n" +
                        "Creator Mehdi Lavasani\nEmail address mehdilavasani79@gmail.com"

            AlertDialog.Builder(this)
                .setTitle("About Notes")
                .setMessage(message)
                .setPositiveButton("Ok") { _, _ ->
                }
                .setNegativeButton("Copy") { _, _ ->
                    copyTextClipboard(this, "About Notes", message)
                }
                .create()
                .show()
        }

        val noteTimer = Timer()
        noteTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (variables["updateNotes"] == "true") {
                    variables["updateNotes"] = "false"

                   runOnUiThread {
                       loadNotes()
                   }
                }
            }
        }, 0, 500)
    }

    private fun loadNotes() {
        val notes = getAllNotes(this)

        if (notes.isEmpty()) {
            b.tvNotesNumber.text = "No notes exists"
        } else {
            b.tvNotesNumber.text = "Items ${notes.size}"
        }

        b.tvNotesEmpty.isVisible = notes.isEmpty()
        b.rcNotes.layoutManager = LinearLayoutManager(this)

        val adapter = NotesAdapter(this, notes)
        b.rcNotes.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }
}