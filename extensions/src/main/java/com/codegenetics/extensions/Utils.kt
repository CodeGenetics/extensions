package com.codegenetics.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

fun getTimeDifference(oldTime: String, newTime: String): Int {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)
        val oldDateString = formatter.format(Date(oldTime.toLong() * 1000L))
        val newDateString = formatter.format(Date(newTime.toLong()))
        val oldDate = formatter.parse(oldDateString)
        val newDate = formatter.parse(newDateString)
        val timeDiff = newDate.time - oldDate.time
        (timeDiff / 1000).toString().toInt()
    } catch (e: Exception) {
      0
    }
}

fun isAppVisible(): Boolean {
    return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
}

fun isAppKilled(): Boolean {
    return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)
}