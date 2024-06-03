package org.zendev.notes.utils

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import org.zendev.notes.R
import kotlin.system.exitProcess


val variables = mutableMapOf<String, Any>()

fun errorDialog(
    context: Context,
    title: String,
    message: String,
    finish: Boolean = false
): AlertDialog {
    val dialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setIcon(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_error, null))
        .setPositiveButton("Ok") { _, _ ->
            if (finish) {
                exitProcess(0)
            }
        }

    return dialog.create()
}

fun copyTextClipboard(context: Context, label: String, text: String) {
    val clipboard = getSystemService(context, ClipboardManager::class.java) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)

    clipboard.setPrimaryClip(clip)
}