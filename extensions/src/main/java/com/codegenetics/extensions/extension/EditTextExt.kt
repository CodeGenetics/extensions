package com.codegenetics.extensions.extension

import android.text.InputFilter
import android.view.KeyEvent
import android.widget.EditText

fun EditText?.moveCursorToEnd() = this?.setSelection(this.text.length)

fun EditText.setFileRenameFilters() {
    val blockCharacterSet = "\\ / : * ? \" < > |"
    val etFilter = InputFilter { source, _, _, _, _, _ ->
        try {
            return@InputFilter if (blockCharacterSet.contains((source))) "" else source
        } catch (e: Exception) {
        }
        null
    }
    filters = arrayOf(etFilter)
}

fun EditText.textString(): String = text.toString()

fun EditText.setOnEnterListener(callback: () -> Unit) {
    this.setOnKeyListener { _, keyCode, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN) {
            callback()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}
