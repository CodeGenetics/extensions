package com.codegenetics.extensions.extension

import android.text.InputFilter
import android.view.KeyEvent
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

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


fun EditText.isEmailValid(): Boolean {
    val pattern: Pattern
    val emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
    pattern = Pattern.compile(emailPattern)
    val matcher: Matcher = pattern.matcher(this.text.toString())
    return this.text.toString().isNotEmpty() && matcher.matches()
}

fun EditText.isEmpty(): Boolean {
    return this.text.toString().isEmpty()
}

fun EditText.isValid(): Boolean {
    return this.textString().isNotEmptyAndBlank()
}

fun Any.isNotEmptyAndBlank(): Boolean {
    return (this.toString().isNotEmpty() && this.toString().isNotBlank())
}


fun EditText.clear() {
    this.setText("")
}