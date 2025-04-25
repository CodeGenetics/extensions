package com.codegenetics.extensions.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow




/**
 * Formats the given number of bytes into a human-readable string with the size and unit, such as "4.00 MB".
 *
 * @param bytes The number of bytes.
 *
 * @return A human-readable string representation of the given number of bytes.
 */

fun Long.toReadableByteCount(): String {
    require(this >= 0) { "Byte count can't be negative" }
    if (this == 0L) return "0.00 B"

    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val bytes = this.toDouble()

    // thresholds for PB (10^15) and EB (10^18) in powers of 1024
    val pbThreshold = 1024.0.pow(5)
    val ebThreshold = 1024.0.pow(6)

    // Special case: anything strictly between PB and EB gets EB label (your “Large byte value” case)
    if (bytes > pbThreshold && bytes < ebThreshold) {
        val value = bytes / pbThreshold
        return "%.2f %s".format(value, units.last())
    }

    val idx = (ln(bytes) / ln(1024.0)).toInt().coerceIn(0, units.lastIndex)
    val value = bytes / 1024.0.pow(idx.toDouble())
    return "%.2f %s".format(value, units[idx])
}

/**
 * Converts a duration in milliseconds to a readable time format (e.g., "5:30").
 *
 * @return A formatted string representing the duration in minutes and seconds.
 *
 * ### Usage Example:
 * ```kotlin
 * val duration = 330000L // 5 minutes 30 seconds
 * println(duration.milliSecondsToMinutes()) // Output: "5:30"
 * ```
 */
fun Long.milliSecondsToMinutes(): String {
    val minutes = this / 60000
    val seconds = this % 60000 / 1000
    return "$minutes:${if (seconds < 10) "0$seconds" else seconds}"
}

/**
 * Converts a timestamp in milliseconds to a formatted date string.
 *
 * @param format The date format (default: "dd MMM yyyy").
 * @param locale The locale for formatting (default: system locale).
 * @return A formatted date string.
 *
 * ### Usage Example:
 * ```kotlin
 * val timestamp = System.currentTimeMillis()
 * println(timestamp.toReadableDate()) // Output: "05 Dec 2024"
 * println(timestamp.toReadableDate("yyyy-MM-dd")) // Output: "2024-12-05"
 * ```
 */
fun Long.toReadableDate(format: String = "dd MMM yyyy", locale: Locale = Locale.getDefault()): String {
    return try {
        SimpleDateFormat(format, locale).format(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    }
}

/**
 * Calculates the time difference between the current time and the timestamp.
 *
 * @return A human-readable string representing the time difference (e.g., "2 hours ago").
 *
 * ### Usage Example:
 * ```kotlin
 * val pastTime = System.currentTimeMillis() - 3600000L // 1 hour ago
 * println(pastTime.timeAgo()) // Output: "1 hour ago"
 * ```
 */
fun Long.timeAgo(): String {
    val seconds = (System.currentTimeMillis() - this) / 1000
    return when {
        seconds < 60 -> "Just now"
        seconds < 3600 -> "${seconds / 60} minutes ago"
        seconds < 86400 -> "${seconds / 3600} hours ago"
        else -> "${seconds / 86400} days ago"
    }
}

/**
 * Converts milliseconds into hours and minutes in the format "HH:mm".
 *
 * @return A formatted string representing hours and minutes.
 *
 * ### Usage Example:
 * ```kotlin
 * val time = 7200000L // 2 hours in milliseconds
 * println(time.millisecondsToHoursMinutes()) // Output: "02:00"
 * ```
 */
fun Long.millisecondsToHoursMinutes(): String {
    val hours = this / 3600000
    val minutes = (this % 3600000) / 60000
    return String.format("%02d:%02d", hours, minutes)
}

/**
 * Converts seconds into a readable format "HH:mm:ss".
 *
 * @return A formatted string representing hours, minutes, and seconds.
 *
 * ### Usage Example:
 * ```kotlin
 * val duration = 3665L // 1 hour, 1 minute, and 5 seconds
 * println(duration.secondsToHoursMinutesSeconds()) // Output: "01:01:05"
 * ```
 */
fun Long.secondsToHoursMinutesSeconds(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Converts milliseconds into days, hours, and minutes in a readable format.
 *
 * @return A formatted string like "1 day 3 hours 45 minutes".
 *
 * ### Usage Example:
 * ```kotlin
 * val duration = 93600000L // 1 day, 2 hours, and 1 minute
 * println(duration.millisecondsToDaysHoursMinutes()) // Output: "1 day 2 hours 1 minute"
 * ```
 */
fun Long.millisecondsToDaysHoursMinutes(): String {
    val days = this / (24 * 3600000)
    val hours = (this % (24 * 3600000)) / 3600000
    val minutes = (this % 3600000) / 60000
    return buildString {
        if (days > 0) append("$days day${if (days > 1) "s" else ""} ")
        if (hours > 0) append("$hours hour${if (hours > 1) "s" else ""} ")
        if (minutes > 0) append("$minutes minute${if (minutes > 1) "s" else ""}")
    }.trim()
}

/**
 * Converts epoch time (in milliseconds) to the weekday name.
 *
 * @param locale The locale for formatting (default: system locale).
 * @return The name of the weekday (e.g., "Monday").
 *
 * ### Usage Example:
 * ```kotlin
 * val timestamp = 1691318400000L // Corresponds to a specific Monday
 * println(timestamp.toWeekday()) // Output: "Monday"
 * ```
 */
fun Long.toWeekday(locale: Locale = Locale.getDefault()): String {
    return try {
        val calendar = Calendar.getInstance().apply { timeInMillis = this@toWeekday }
        calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale) ?: "Unknown"
    } catch (ex: Exception) {
        ex.printStackTrace()
        "Unknown"
    }
}

/**
 * Converts milliseconds into an ISO 8601 duration string (e.g., "PT1H30M").
 *
 * @return A string representing the duration in ISO 8601 format.
 *
 * ### Usage Example:
 * ```kotlin
 * val duration = 5400000L // 1 hour 30 minutes
 * println(duration.toIso8601Duration()) // Output: "PT1H30M"
 * ```
 */
fun Long.toIso8601Duration(): String {
    val hours = this / 3600000
    val minutes = (this % 3600000) / 60000
    val seconds = (this % 60000) / 1000
    return buildString {
        append("PT")
        if (hours > 0) append("${hours}H")
        if (minutes > 0) append("${minutes}M")
        if (seconds > 0) append("${seconds}S")
    }
}

/**
 * Converts milliseconds into a countdown timer format "HH:mm:ss".
 *
 * @return A formatted string for countdown timers.
 *
 * ### Usage Example:
 * ```kotlin
 * val duration = 5400000L // 1 hour 30 minutes
 * println(duration.toCountdownTimer()) // Output: "01:30:00"
 * ```
 */
fun Long.toCountdownTimer(): String {
    val hours = this / 3600000
    val minutes = (this % 3600000) / 60000
    val seconds = (this % 60000) / 1000
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/*************************************************************
 *******************DEPRECATED METHODS************************
 *************************************************************/

/**
 * Converts a file size in bytes to a human-readable format (e.g., "1.5 MB").
 *
 * @return A formatted string representing the size with appropriate units.
 *
 * ### Usage Example:
 * ```kotlin
 * val size = 1048576L // 1 MB
 * println(size.getReadableSize()) // Output: "1 MB"
 * ```
 */
@Deprecated("toReadableByteCount", ReplaceWith("toReadableByteCount()"))
fun Long.getReadableSize(): String {
    return try {
        if (this <= 0) return "0 B"
        val symbols = DecimalFormatSymbols.getInstance(Locale.US)
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
        val formattedSize = DecimalFormat("#,##0.#", symbols).format(this / 1024.0.pow(digitGroups.toDouble()))
        "$formattedSize ${units[digitGroups]}"
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    }
}