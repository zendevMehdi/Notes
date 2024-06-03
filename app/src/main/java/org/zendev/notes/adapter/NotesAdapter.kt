package org.zendev.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.zendev.notes.R
import org.zendev.notes.notes.Note
import org.zendev.notes.notes.addNewNote
import org.zendev.notes.notes.deleteNote
import org.zendev.notes.notes.getNote
import org.zendev.notes.utils.copyTextClipboard
import org.zendev.notes.utils.errorDialog
import org.zendev.notes.utils.variables

class NotesAdapter(private var context: Context, private var notes: List<Note>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNoteName: TextView = view.findViewById(R.id.tvNoteName)
        var cardCurrentNote: CardView = view.findViewById(R.id.cardCurrentNote)
        var btnNoteDelete: FloatingActionButton = view.findViewById(R.id.btnNoteDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_note, parent, false)

        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val noteName = notes[position].name

        if (noteName.length > 20) {
            holder.tvNoteName.text = notes[position].name.substring(0, 20).plus("...")
        } else {
            holder.tvNoteName.text = notes[position].name
        }

        holder.cardCurrentNote.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_note_dialog, null)

            val txtEditNoteName = dialogView.findViewById<TextInputEditText>(R.id.txtEditNoteName)
            val txtEditNoteContent = dialogView.findViewById<EditText>(R.id.txtEditNoteContent)
            val txtLayEditNoteName =
                dialogView.findViewById<TextInputLayout>(R.id.txtLayEditNoteName)

            val note = getNote(context, noteName)

            txtEditNoteName.setText(noteName)
            txtEditNoteContent.setText(note.content)

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Edit file")
                .setIcon(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_edit, null))
                .setCancelable(true)
                .setPositiveButton("Ok") { _, _ ->
                    if (txtEditNoteName.text?.isEmpty()!!) {
                        txtLayEditNoteName.error = "File name can't be empty."
                    } else {
                        txtLayEditNoteName.error = null
                        val newNote = Note(
                            txtEditNoteName.text.toString(),
                            txtEditNoteContent.text.toString()
                        )

                        if (!addNewNote(context, newNote)) {
                            errorDialog(context, "Edit failed", "Failed to change the note.")
                                .show()
                        }

                        if (txtEditNoteName.text.toString() != noteName) {
                            if (!deleteNote(context, noteName)) {
                                errorDialog(
                                    context, "Delete failed",
                                    "Failed to delete the old file.\nIf you can delete the file manually or contact the creator to solve the problem."
                                )
                                    .show()
                            }

                            /* update entire recyclerview or update the position of recyclerview item */
                            variables["updateNotes"] = "true"
                        }
                    }
                }.setNegativeButton("Cancel") { _, _ ->
                }
                .create()
                .show()
        }

        holder.cardCurrentNote.setOnLongClickListener {
            copyTextClipboard(
                context,
                "File \"$noteName\" content",
                getNote(context, noteName).content
            )

            return@setOnLongClickListener true
        }

        holder.btnNoteDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete note?")
                .setMessage("Are you sure you want to delete $noteName file?")
                .setIcon(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_delete, null))
                .setCancelable(true)
                .setPositiveButton("Ok") { _, _ ->
                    if (deleteNote(context, noteName)) {
                        variables["updateNotes"] = "true"
                    }
                }.setNegativeButton("Cancel") { _, _ ->
                }
                .create()
                .show()
        }
    }
}