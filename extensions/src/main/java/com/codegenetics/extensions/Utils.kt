package com.codegenetics.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
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