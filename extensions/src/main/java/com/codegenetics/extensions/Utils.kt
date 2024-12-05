package com.codegenetics.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.collection.LruCache
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Random
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.abs

internal val colorCache = LruCache<Int, Int>(50)  // Cache size can be adjusted based on needs
internal val drawableCache = LruCache<Int, Drawable?>(50)

fun clearLruCache() {
    colorCache.evictAll()
    drawableCache.evictAll()
}

fun delay(time: Long = 500L, runnable: Runnable): Handler {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(runnable, time)
    return handler
}

fun fromHtml(html: String?): Spanned? {
    return when {
        html == null -> {
            SpannableString("")
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        }

        else -> {
            Html.fromHtml(html)
        }
    }
}

fun splitFilename(fileName: String): Pair<String, String> {
    if (fileName.lastIndexOf(".") > 0) {
        val index = fileName.lastIndexOf(".")
        return Pair(fileName.substring(0, index), fileName.substring(index, fileName.length))
    }
    return Pair(fileName, "")
}

@ChecksSdkIntAtLeast(parameter = 0)
fun isVersionLessThanEqualTo(version: Int): Boolean {
    return Build.VERSION.SDK_INT <= version
}

@ChecksSdkIntAtLeast(parameter = 0)
fun isVersionGreaterThanEqualTo(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}

fun getFormattedDate(dateVal: Long): String {
    try {
        var date = dateVal
        date *= 1000L
        return SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date(date))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return ""

}

fun getTodayDate(pattern: String = "MMMM d,yyyy"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
}

fun getCurrentTimeInMillis(): Long {
    return Calendar.getInstance().timeInMillis
}

/** Returns Number of days from Today*/
fun getDaysFromToday(date: Long): Long {
    val millionSeconds = Calendar.getInstance().timeInMillis - date
    return TimeUnit.MILLISECONDS.toDays(millionSeconds)
}

fun <T> lazyAndroid(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun debugLog(s: Any?) {
    Log.d("FRIDAY", "Hi >---> $s")
}

fun getGradientBackground(
    startColor: Int,
    centerColor: Int,
    endColor: Int,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM
): GradientDrawable {
    return GradientDrawable(
        orientation, intArrayOf(
            startColor, // start color
            centerColor, // middle color
            endColor // end color
        )
    )
}

fun getGradientBackground(
    startColor: Int,
    endColor: Int,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM
): GradientDrawable {
    return GradientDrawable(
        orientation, intArrayOf(
            startColor, // start color
            endColor // end color
        )
    )
}

fun createRoundedBorderDrawable(
    color: Int,
    strokeWidth: Int,
    cornerRadius: Float
): GradientDrawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.setStroke(strokeWidth, color)
    gradientDrawable.cornerRadius = cornerRadius
    return gradientDrawable
}

fun createRoundedSolidDrawable(color: Int, cornerRadius: Float): GradientDrawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    gradientDrawable.setColor(color)
    gradientDrawable.cornerRadius = cornerRadius
    return gradientDrawable
}

fun getStateListDrawable(
    selectedDrawable: GradientDrawable,
    unselectedDrawable: GradientDrawable
): StateListDrawable {
    val stateListDrawable = StateListDrawable()
    stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), selectedDrawable)
    stateListDrawable.addState(intArrayOf(), unselectedDrawable)
    return stateListDrawable


}


/** required in format = dd-mm-yyyy
 * @param date String*/
fun getAge(date: String): Int {
    val splitString: Array<String> = date.split("-".toRegex()).toTypedArray()
    val day = splitString[0].toInt()
    val month = splitString[1].toInt()
    val year = splitString[2].toInt()
    val cal = GregorianCalendar()
    var a: Int
    val y: Int = cal[Calendar.YEAR]
    val m: Int = cal[Calendar.MONTH]
    val d: Int = cal[Calendar.DAY_OF_MONTH]
    cal[year, month] = day
    a = y - cal[Calendar.YEAR]
    if (m < cal[Calendar.MONTH]
        || m == cal[Calendar.MONTH] && d < cal[Calendar.DAY_OF_MONTH]
    ) {
        --a
    }
    return a
}

fun getRandomString(): String {
    val r = Random()
    val i1 = r.nextInt(8000000) + 65
    val AlphaNumericString = ("0123456789" + "abcdefghijklmnopqrstuvxyz")
    val sb = java.lang.StringBuilder(i1)
    for (i in 0..20) {

        // generate a random number between
        // 0 to AlphaNumericString variable length
        val index = (AlphaNumericString.length * Math.random()).toInt()

        // add Character one by one in end of sb
        sb.append(
            AlphaNumericString[index]
        )
    }
    return "a_$sb"
}

fun getUuid(): String {
    return UUID.randomUUID().toString()
}

fun getThumbnailFromVideoURL(videoPath: String?): Bitmap? {
    try {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(
                videoPath, HashMap()
            )

            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            throw Throwable(
                "Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message
            )
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    } catch (e: Exception) {

    }
    return null
}



fun isAppVisible(): Boolean {
    return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
}

fun isAppKilled(): Boolean {
    return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)
}


fun getDateFormat(dateString: String): String? {
    val possibleFormats = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss.SSS",
        "yyyy/MM/dd HH:mm:ss",
        "EEE MMM dd HH:mm:ss z yyyy",
    )
    for (format in possibleFormats) {
        try {
            SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            return format
        } catch (e: Exception) {
            // Parsing failed for this format, try the next one
        }
    }
    // Parsing failed for all possible formats
    return null
}

/** @return Boolean time from current time*/
fun isTimeElapsed(dateString: String, timeInSeconds: Long): Boolean {
    if (dateString.isEmpty()) return false
    val dateFormat = SimpleDateFormat(getDateFormat(dateString), Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()

    val parsedTime = dateFormat.parse(dateString)

    val date = dateFormat.parse(dateString)
    return if (date != null) {
        val currentTime = Date()
        val currentUtcTime = Date(currentTime.time - TimeZone.getDefault().rawOffset)
        // Calculate the duration between the input time and the current time
        val diffInMilliSec = abs(currentUtcTime.time - (parsedTime?.time ?: Date().time))
        diffInMilliSec >= (timeInSeconds * 1000)
    } else false
}

/** @return the time difference in minutes from Current time */
fun getTimeElapsedSince(inputTime: String): Long {
    if (inputTime.isEmpty()) return 0
    return try {// Parse the input time string into a Date object
        val dateFormat = SimpleDateFormat(getDateFormat(inputTime), Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        val parsedTime = dateFormat.parse(inputTime)
        // Get the current time as a Date object in UTC time zone
        val currentTime = Date()
        val currentUtcTime = Date(currentTime.time - TimeZone.getDefault().rawOffset)
        // Calculate the duration between the input time and the current time
        val durationInMillis = abs(currentUtcTime.time - (parsedTime?.time ?: Date().time))

        // Return the elapsed time in minutes
        durationInMillis / (1000)
    } catch (e: Exception) {
        0
    }
}


/** @return the time difference in minutes from Current time
 * Required API Level 26*/
@RequiresApi(Build.VERSION_CODES.O)
fun getTimeElapsedSinceO(inputTime: String): Long {
    // Parse the input time string into a LocalDateTime object
    val formatter = DateTimeFormatter.ofPattern(getDateFormat(inputTime))
    val parsedTime = LocalDateTime.parse(inputTime, formatter)

    // Convert the input time to UTC
    val utcTime = parsedTime.atOffset(ZoneOffset.UTC)

    // Get the current time as a LocalDateTime object in UTC
    val currentUtcTime = LocalDateTime.now(ZoneOffset.UTC)

    // Calculate the duration between the input time and the current time
    val duration = Duration.between(utcTime, currentUtcTime)

    // Return the elapsed time in minutes
    return duration.toMinutes()
}

fun getTimeDifference(oldTime: String, newTime: String): Long {
    return try {
        val oldDateFormatter = SimpleDateFormat(getDateFormat(oldTime), Locale.getDefault())
        oldDateFormatter.timeZone = TimeZone.getDefault()
        val newDateFormatter = SimpleDateFormat(getDateFormat(newTime), Locale.getDefault())
        newDateFormatter.timeZone = TimeZone.getDefault()

        val oldDate = oldDateFormatter.parse(oldTime)
        val newDate = newDateFormatter.parse(newTime)
        val timeDiff = abs((newDate?.time ?: Date().time) - (oldDate?.time ?: Date().time))
        (timeDiff / 1000).toString().toLong()
    } catch (e: Exception) {
        0
    }
}

fun startTimer(
    durationSeconds: Long,
    tickInterval: Long = 1000,
    onTick: (Long) -> Unit,
    onFinish: () -> Unit
): CountDownTimer {
    return object : CountDownTimer(durationSeconds * 1000, tickInterval) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished / 1000)
        }

        override fun onFinish() {
            onFinish()
        }
    }.apply {
        start()
    }
}

fun getTimeZone(): String {
    return TimeZone.getDefault().id
}

/**
 * Attempts to execute a block of code up to a given number of retries.
 *
 * @param times The maximum number of retries.
 * @param block The block of code to execute.
 * @return The result of the block if successful, or null if all retries fail.
 */
inline fun <T> retry(times: Int, block: () -> T): T? {
    var attempt = 0
    var result: T? = null
    while (attempt < times) {
        try {
            result = block()
            break
        } catch (e: Exception) {
            attempt++
        }
    }
    return result
}
