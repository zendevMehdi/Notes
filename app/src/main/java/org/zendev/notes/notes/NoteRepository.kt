package org.zendev.notes.notes

import android.content.Context
import org.apache.commons.io.FilenameUtils
import org.zendev.notes.io.getNotesFolder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files

fun getAllNotes(context: Context): List<Note> {
    val notes = mutableListOf<Note>()
    val files = getNotesFolder(context)

    for (file in files.listFiles()!!) {
        var fileContent = ""
        for (line in Files.readAllLines(file.toPath())) {
            fileContent += line
        }

        val note = Note(FilenameUtils.getBaseName(file.absolutePath), fileContent)
        notes.add(note)
    }

    return notes
}

fun addNewNote(context: Context, note: Note): Boolean {
    val fileName = "${note.name}.txt"
    val file = getNotesFolder(context)

    FileOutputStream("${file.absolutePath}/$fileName").write(note.content.toByteArray())
    return true
}

fun deleteNote(context: Context, noteName: String): Boolean {
    return File(getNotesFolder(context).absolutePath, "/$noteName.txt").delete()
}

fun getNote(context: Context, noteName: String) : Note {
    val fis = FileInputStream("${getNotesFolder(context).absolutePath}/${noteName}.txt")
    val strBuilder = StringBuilder()

    var content: Int
    while (fis.read().also { content = it } != -1) {
        strBuilder.append(content.toChar())
    }

    return Note(noteName, strBuilder.toString())
}