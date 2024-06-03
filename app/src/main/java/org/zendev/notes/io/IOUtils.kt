package org.zendev.notes.io

import android.content.Context
import java.io.File

fun getNotesFolder(context: Context) : File {
    return context.getDir("Notes", Context.MODE_PRIVATE)
}

fun checkNotesFolder(context: Context): Boolean {
    val noteFolder = getNotesFolder(context)
    if (!noteFolder.exists()) {
        return noteFolder.mkdir();
    }

    return true
}

fun fileExists(context: Context, fileName: String) : Boolean {
    return File(getNotesFolder(context).absolutePath, "/$fileName.txt").exists()
}

