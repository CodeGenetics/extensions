package com.codegenetics.extensions.extension

import android.os.SystemClock
import android.widget.Chronometer
import android.view.View

// Constants for time calculations
private const val MILLIS_IN_HOUR = 3600000
private const val MILLIS_IN_MINUTE = 60000
private const val MILLIS_IN_SECOND = 1000

/**
 * Makes the [Chronometer] visible and initializes it to display time starting from zero
 * in the format [hh:mm:ss].
 *
 * ### Usage Example:
 * ```kotlin
 * chronometer.init()
 * ```
 */
fun Chronometer.init() {
    this.show()
    this.base = SystemClock.elapsedRealtime()
    this.setOnChronometerTickListener { chronometer ->
        val elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
        chronometer.text = elapsedTime.toFormattedTime()
    }
}

/**
 * Initializes the [Chronometer] and starts it immediately, displaying time in the format [hh:mm:ss].
 *
 * ### Usage Example:
 * ```kotlin
 * chronometer.initAndStart()
 * ```
 */
fun Chronometer.initAndStart() {
    this.init()
    this.start()
}

/**
 * Starts the [Chronometer] from zero.
 *
 * ### Usage Example:
 * ```kotlin
 * chronometer.startWithZero()
 * ```
 */
fun Chronometer.startWithZero() {
    this.base = SystemClock.elapsedRealtime()
    this.start()
}

/**
 * Resumes the [Chronometer] from a previously stopped time.
 * @param timeWhenStopped The offset in milliseconds from which to resume counting.
 *
 * ### Usage Example:
 * ```kotlin
 * chronometer.resume(timeWhenStopped)
 * ```
 * **Note:** Ensure `timeWhenStopped` is non-negative.
 */
fun Chronometer.resume(timeWhenStopped: Long) {
    require(timeWhenStopped >= 0) { "timeWhenStopped must be non-negative" }
    this.base = SystemClock.elapsedRealtime() + timeWhenStopped
    this.start()
}

/**
 * Retrieves the elapsed time from the [Chronometer] in milliseconds.
 * @return The elapsed time since the [Chronometer] was started, or 0 if not running.
 *
 * ### Usage Example:
 * ```kotlin
 * val elapsedTime = chronometer.getTime()
 * ```
 */
fun Chronometer.getTime(): Long {
    return if (this.isRunning()) {
        SystemClock.elapsedRealtime() - this.base
    } else {
        0L
    }
}

/**
 * Checks if the [Chronometer] is currently running.
 * @return True if the [Chronometer] is running, false otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * val isRunning = chronometer.isRunning()
 * ```
 */
private fun Chronometer.isRunning(): Boolean {
    return SystemClock.elapsedRealtime() > this.base
}

/**
 * Extension function to make the [Chronometer] visible.
 *
 * ### Usage Example:
 * ```kotlin
 * chronometer.show()
 * ```
 */
fun Chronometer.show() {
    this.visibility = View.VISIBLE
}

/**
 * Extension function to format elapsed time in milliseconds to a string in the format [hh:mm:ss].
 * @return A string representation of the formatted time.
 *
 * ### Usage Example (Internal Use):
 * ```kotlin
 * val formattedTime = elapsedTime.toFormattedTime()
 * ```
 */
private fun Long.toFormattedTime(): String {
    val hours = (this / MILLIS_IN_HOUR).toInt()
    val minutes = (this % MILLIS_IN_HOUR / MILLIS_IN_MINUTE).toInt()
    val seconds = (this % MILLIS_IN_MINUTE / MILLIS_IN_SECOND).toInt()
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
