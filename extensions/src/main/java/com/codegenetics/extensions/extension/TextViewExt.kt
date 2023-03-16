package com.codegenetics.extensions.extension

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun TextView.tintViewDrawable(@ColorRes colorId: Int) {
    val drawables = compoundDrawables
    for (drawable in drawables) {
        drawable?.setColorFilter(
            ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP
        )
    }
}

fun TextView.textColor(@ColorRes colorId: Int) {
    setTextColor(ContextCompat.getColor(context, colorId))
}

/**
 * Extension method underLine for TextView.
 */
fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * Extension method deleteLine for TextView.
 */
fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

/**
 * Extension method bold for TextView.
 */
fun TextView.bold() {
    paint.isFakeBoldText = true
    paint.isAntiAlias = true
}

/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    } catch (e: Exception) {
        Log.d("ViewExtensions",  "exception in setColorOfSubstring, text=$text, substring=$substring", e)
    }
}

/**
 * Extension method to set font for TextView.
 */
fun TextView.font(font: String) {
    typeface = Typeface.createFromAsset(context.assets, "fonts/$font.ttf")
}

/**
 * Extension method to set a drawable to the left of a TextView.
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * Extension method to get md5 string.
 */
fun String.md5() = encrypt(this, "MD5")

/**
 * Extension method to get sha1 string.
 */
fun String.sha1() = encrypt(this, "SHA-1")

/**
 * Extension method to check if String is Phone Number.
 */
fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Email.
 */
fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * Extension method to check if String is Number.
 */
fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

/**
 * Extension method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.lowercase().contentEquals(other.lowercase())

/**
 * Extension method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

/**
 * Extension method to convert byteArray to String.
 */
private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}