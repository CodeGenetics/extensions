package com.codegenetics.extensions.extension

import android.text.InputFilter
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Moves the cursor to the end of the text in the [EditText].
 */
fun EditText?.moveCursorToEnd() {
    this?.setSelection(this.text.length)
}

/**
 * Sets input filters to restrict invalid characters for file renaming.
 * Invalid characters include: \ / : * ? " < > |
 */
fun EditText.setFileRenameFilters() {
    val blockCharacterSet = "\\ / : * ? \" < > |"
    val etFilter = InputFilter { source, _, _, _, _, _ ->
        if (blockCharacterSet.contains(source)) "" else source
    }
    filters = arrayOf(etFilter)
}


/**
 * Retrieves the text of the [EditText] as a [String].
 */
fun EditText.textString(): String = this.text.toString()


/**
 * Sets an action listener that triggers the [callback] when the Enter key is pressed.
 */
fun EditText.setOnEnterListener(callback: () -> Unit) {
    this.setOnKeyListener { _, keyCode, keyEvent ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            callback()
            return@setOnKeyListener true
        }
        false
    }
}

/**
 * Validates if the text in the [EditText] is a valid email address.
 * @return True if valid, false otherwise.
 */
fun EditText.isEmailValid(): Boolean {
    val pattern: Pattern
    val emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
    pattern = Pattern.compile(emailPattern)
    val matcher: Matcher = pattern.matcher(this.text.toString())
    return this.text.toString().isNotEmpty() && matcher.matches()
}

/**
 * Checks if the text in the [EditText] is empty.
 * @return True if empty, false otherwise.
 */
fun EditText.isEmpty(): Boolean = this.textString().isEmpty()

/**
 * Validates if the text in the [EditText] is not empty or blank.
 * @return True if valid, false otherwise.
 */
fun EditText.isValid(): Boolean = this.textString().isNotEmptyAndBlank()

/**
 * Checks if the [String] representation of the object is not empty and not blank.
 * @return True if not empty and not blank, false otherwise.
 */
fun Any.isNotEmptyAndBlank(): Boolean {
    val str = this.toString()
    return str.isNotEmpty() && str.isNotBlank()
}

fun EditText.clear() {
    this.setText("")
}

/**
 * Extension method to set a drawable to the left of a TextView.
 */
fun EditText.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun EditText.setDrawableRight(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun EditText.setDrawableTop(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}

fun EditText.setDrawableBottom(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)
}