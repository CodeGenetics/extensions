package com.codegenetics.extensions.extension

import android.graphics.Color
import androidx.core.text.HtmlCompat
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/** @return bool by matching regex*/
fun String.isValidColor(): Boolean {
    if (this.length < 3) return false
    if (this.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
        return true
    } else if (this.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
        return true

    } else if (this.matches("^#([0-9a-fA-F]{8})$".toRegex())) {
        return true
    }
    return false
}

/** if color string is have invalid character count
 * if color length is 4 or 5
 * @return valid color by adding Zero*/
fun String.validateColor(): String {
    if (!this.startsWith("#")) return ""
    if (this.length < 4 || this.length > 9) return ""

    if (this.matches("^#([0-9a-fA-F]{6})$".toRegex())) {
        return this
    } else if (this.matches("^#([0-9a-fA-F]{3})$".toRegex())) {
        return this
    } else if (this.matches("^#([0-9a-fA-F]{8})$".toRegex())) {
        return this
    } else if (this.matches("^#([0-9a-fA-F]{4})$".toRegex())) {
        return "${this}00"
    } else if (this.matches("^#([0-9a-fA-F]{5})$".toRegex())) {
        return "${this}00"
    } else {
        return ""
    }
}
/** return color in Integer*/
fun String.toColor(): Int {
    return try {
        Color.parseColor(this.validateColor())
    } catch (e: Exception) {
        0
    }
}

/**
 * @param seconds
 * @return [hh:mm:ss] format*/
fun String.toTimeString(): String {
    val input = this.toInt()
    val hours: Int = input / 3600
    val minutes: Int = input % 3600 / 60
    val seconds: Int = input % 3600 % 60

    val h = if (hours < 10) "0$hours" else "$hours"
    val m = if (minutes < 10) "0$minutes" else "$minutes"
    val s = if (seconds < 10) "0$seconds" else "$seconds"


    return "$h:$m:$s"
}

fun String.beautifyJson(space: Int = 2): String {
    return try {
        JSONObject(this).toString(space)
    } catch (e: Exception) {
        ""
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