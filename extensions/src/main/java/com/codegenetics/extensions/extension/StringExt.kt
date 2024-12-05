package com.codegenetics.extensions.extension

import android.graphics.Color
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Checks if the string is a valid hexadecimal color code.
 *
 * @return `true` if the string matches a valid color format (#RRGGBB, #RGB, or #AARRGGBB).
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FF5733".isValidColor()) // Output: true
 * println("123456".isValidColor())  // Output: false
 * ```
 */
fun String.isValidColor(): Boolean {
    val regex = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|[0-9a-fA-F]{8})$".toRegex()
    return this.matches(regex)
}

/**
 * Validates the color string and adjusts its format if necessary.
 * Adds missing characters (e.g., "00" for alpha) if needed.
 *
 * @return The validated and adjusted color string, or an empty string if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FFF".validateColor())   // Output: "#FFF"
 * println("#F00A".validateColor())  // Output: "#F00A00"
 * ```
 */
fun String.validateColor(): String {
    return when {
        this.matches("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|[0-9a-fA-F]{8})$".toRegex()) -> this
        this.matches("^#([0-9a-fA-F]{4})$".toRegex()) -> "${this}00"
        this.matches("^#([0-9a-fA-F]{5})$".toRegex()) -> "${this}00"
        else -> ""
    }
}

/**
 * Parses a validated color string into an integer value.
 *
 * @return The integer value of the color, or 0 if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * println("#FF5733".toColor())  // Output: -435143
 * ```
 */
fun String.toColor(): Int {
    return try {
        Color.parseColor(this.validateColor())
    } catch (e: IllegalArgumentException) {
        0 // Default color
    }
}

/**
 * Converts a numeric string (representing seconds) into HH:mm:ss format.
 *
 * @return The formatted time string.
 *
 * ### Usage Example:
 * ```kotlin
 * println("3661".toTimeString())  // Output: "01:01:01"
 * ```
 */
fun String.toTimeString(): String {
    val seconds = this.toIntOrNull() ?: return "00:00:00"
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}

/**
 * Formats a JSON string with specified indentation.
 *
 * @param space The number of spaces to use for indentation (default: 2).
 * @return The formatted JSON string, or an empty string if invalid.
 *
 * ### Usage Example:
 * ```kotlin
 * val json = """{"name":"John","age":30}"""
 * println(json.beautifyJson()) // Output: Pretty-printed JSON
 * ```
 */
fun String.beautifyJson(space: Int = 2): String {
    return try {
        if (!this.isValidJson()) return this
        JSONObject(this).toString(space)
    } catch (e: JSONException) {
        try {
            JSONArray(this).toString(space)
        } catch (e: JSONException) {
            this
        }
    }
}

fun String.toEmoji(): String {
    if (this.isNotEmpty()) {
        return String(Character.toChars(this.toInt(16)))
    }
    return ""
}

fun String.toFormulatedTime(): String {
    val totalSecs = this.toInt()
    val hours: Int
    val minutes: Int
    val seconds: Int
    when {
        totalSecs in 1..59 -> {
            return "$totalSecs Sec"
        }

        totalSecs in 60..3599 -> {
            minutes = totalSecs % 3600 / 60
            seconds = totalSecs % 60
            return "$minutes Min $seconds sec"
        }

        totalSecs >= 3600 -> {
            hours = totalSecs / 3600
            minutes = totalSecs % 3600 / 60
            seconds = totalSecs % 60
            return "$hours hr $minutes Min $seconds sec"
        }

        else -> return ""
    }
}

fun String.toEpoch(): Long {
    var str = this
    str = str.trim { it <= ' ' }
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = df.parse(str)
    val epoch = date?.time!!
    println(epoch) // 1055545912454
    return epoch
}

fun String.toDefaultTimeZone(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = formatter.parse(this)
        val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault()) //this format changeable
        dateFormatter.timeZone = TimeZone.getDefault()
        dateFormatter.format(value)
    } catch (e: java.lang.Exception) {
        "00-00-0000 00:00"
    }
}

fun String.unescapeJavaString(): String {
    val sb = StringBuilder(this.length)
    var i = 0
    while (i < this.length) {
        var ch = this[i]
        if (ch == '\\') {
            val nextChar = if (i == this.length - 1) '\\' else this[i + 1]
            // Octal escape?
            if (nextChar in '0'..'7') {
                var code = "" + nextChar
                i++
                if (i < this.length - 1 && this[i + 1] >= '0' && this[i + 1] <= '7') {
                    code += this[i + 1]
                    i++
                    if (i < this.length - 1 && this[i + 1] >= '0' && this[i + 1] <= '7') {
                        code += this[i + 1]
                        i++
                    }
                }
                sb.append(code.toInt(8).toChar())
                i++
                continue
            }
            when (nextChar) {
                '\\' -> ch = '\\'
                'b' -> ch = '\b'
                'n' -> ch = '\n'
                'r' -> ch = '\r'
                't' -> ch = '\t'
                '\"' -> ch = '\"'
                '\'' -> ch = '\''
                'u' -> {
                    if (i >= this.length - 5) {
                        ch = 'u'
                        break
                    }
                    val code =
                        ("" + this[i + 2] + this[i + 3] + this[i + 4] + this[i + 5]).toInt(16)
                    sb.append(Character.toChars(code))
                    i += 5
                    i++
                    continue
                }
            }
            i++
        }
        sb.append(ch)
        i++
    }
    return sb.toString()
}

fun String.parseHtmlString(): String {
    return HtmlCompat.fromHtml(
        this.unescapeJavaString(), HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
}

fun String.parseDouble(): Double {
    try {
        return if (this.isNotEmptyAndBlank()) {
            this.replace(",", ".").toDouble()
        } else {
            0.0
        }
    } catch (e: NumberFormatException) {
        val EASTERN_ARABIC_NUMBERS_LOCALE: Locale =
            Locale.Builder().setLanguage("ar").setExtension('u', "nu-arab").build()
        return NumberFormat.getInstance(EASTERN_ARABIC_NUMBERS_LOCALE).parse(this)?.toDouble()!!
    } catch (e: Exception) {
        return 0.0
    }
}

/**
 * Extension method to get Date for String with specified format.
 */
fun String.dateInFormat(format: String): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: Date? = null
    try {
        parsedDate = dateFormat.parse(this)
    } catch (ignored: ParseException) {
        ignored.printStackTrace()
    }
    return parsedDate
}

fun String.toSnakeCase(): String {
    // Replace all non-alphanumeric characters with underscores
    val regex = Regex("[^a-zA-Z0-9]")
    val result = regex.replace(this, "_")
    // Convert to lowercase and separate words with underscores
    return result.lowercase()
}

fun String.toCamelCaseWithSpaces(): String {
    // Replace all non-alphabetic characters with spaces
    val regex = Regex("[^a-zA-Z]")
    val result = regex.replace(this, " ")
    // Capitalize the first character of each word
    return result.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}

fun String?.isNotNullEmptyBlank(): Boolean {
    return this != null && this.isNotEmptyAndBlank()
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return ""
    return this[0].uppercaseChar() + this.substring(1)
}

fun String.capitalizeFirstLetterAndAfterSpace(): String {
    var result = ""
    var capitalizeNext = true  // Start with true to capitalize the first letter

    for (char in this) {
        result += if (capitalizeNext && char.isLetter()) {
            capitalizeNext = false
            char.uppercaseChar()
        } else {
            if (char.isWhitespace()) capitalizeNext = true
            char
        }
    }

    return result
}

fun String.toCapitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}

fun String.toColorByRGB(str: String): Int {
    val s = str.uppercase()
    return try {
        Color.parseColor(s)
    } catch (e: IllegalArgumentException) {
        // Check if the string is a valid 6-digit hex color code
        if (s.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
            Color.parseColor("#FF$s")
        } else {
            // Check if the string is a valid 3-digit hex color code
            if (s.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
                val r = s[1].toString() + s[1].toString()
                val g = s[2].toString() + s[2].toString()
                val b = s[3].toString() + s[3].toString()
                Color.parseColor("#FF$r$g$b")
            } else {
                // Return a default color if the string is not a valid color code
                return "${s}0".toColor()
            }
        }
    } catch (e: Exception) {
        // Return a default color if an exception is thrown
        0
    }
}

fun String.isValidJson(): Boolean {
    return try {
        JSONObject(this)
        true
    } catch (ex: Exception) {
        try {
            JSONArray(this)
            true
        } catch (ex: Exception) {
            false
        }
    }
}

inline fun <reified T> String.parseJson(): T? {
    return try {
        if (isValidJson()) {
            Gson().fromJson(this, T::class.java)
        } else {
            null
        }

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String?.toSafeInt(defaultValue: Int = 0): Int {
    return this?.toIntOrNull() ?: defaultValue
}

fun String?.toSafeFloat(defaultValue: Float = 0f): Float {
    return this?.toFloatOrNull() ?: defaultValue
}

fun String?.toSafeDouble(defaultValue: Double = 0.0): Double {
    return this?.toDoubleOrNull() ?: defaultValue
}

fun String?.toSafeLong(defaultValue: Long = 0L): Long {
    return this?.toLongOrNull() ?: defaultValue
}

fun String?.toSafeBoolean(defaultValue: Boolean = false): Boolean {
    return when (this?.lowercase()) {
        "true" -> true
        "false" -> false
        else -> defaultValue
    }
}

fun String?.toSafeString(defaultValue: String = ""): String {
    return this ?: defaultValue
}

/**
 * Masks a part of the string with a given character.
 *
 * @param start The starting index of the mask.
 * @param end The ending index of the mask (exclusive).
 * @param maskChar The character used for masking.
 * @return A masked string.
 */
fun String.mask(start: Int = 2, end: Int = this.length - 2, maskChar: Char = '*'): String {
    if (start >= end) return this
    val maskPart = this.substring(start, end).map { maskChar }.joinToString("")
    return this.replaceRange(start, end, maskPart)
}

