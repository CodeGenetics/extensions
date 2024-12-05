package com.codegenetics.extensions.extension

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Looper
import android.text.Editable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date

fun Any.twoDecimal(): String {
    val df = DecimalFormat("0.00")
    return df.format(this.toString().toDouble())
}

/**
 * Parses the object to a Double.
 * @return Double value of the object, or 0.0 if parsing fails.
 */
fun Any.parseDouble(): Double {
    return try {
        if (this.toString().isNotBlank()) {
            this.toString().toDouble()
        } else {
            0.0
        }
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        0.0
    }
}

/**
 * Parses the object to an Int.
 * @return Int value of the object, or 0 if parsing fails.
 */
fun Any.parseInt(): Int {
    return try {
        if (this.toString().isNotBlank()) {
            this.toString().toInt()
        } else {
            0
        }
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        0
    }
}



/**
 * Formats the number to two decimal places without rounding up.
 * @return String representation of the number formatted to two decimal places without rounding.
 */
fun Any.roundUpToTwoDecimalsWithoutRounding(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this.toString().toDouble())
}


/**
 * Extension method to int time to 2 digit String
 */
/**
 * Formats the integer as a two-digit time string (e.g., "01" for 1).
 * @return String representation of the integer with leading zero if necessary.
 */
fun Int.twoDigitTime() = if (this < 10) "0$this" else this.toString()

/**
 * Replaces all text in an [Editable] with the specified [newValue].
 * @param newValue The new text to replace the existing text.
 */
fun Editable.replaceAll(newValue: String) {
    replace(0, length, newValue)
}


/**
 * Replaces all text in an [Editable] with the specified [newValue], ignoring any filters.
 * @param newValue The new text to replace the existing text.
 */
fun Editable.replaceAllIgnoreFilters(newValue: String) {
    val currentFilters = filters
    filters = emptyArray()
    replaceAll(newValue)
    filters = currentFilters
}

/**
 * Creates a clickable span with the specified color and action.
 * @param color The text color for the clickable span.
 * @param action The action to perform when the span is clicked.
 * @return A [ClickableSpan] instance.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */
fun getClickableSpan(color: Int, action: (view: View) -> Unit): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(view: View) {
            action(view)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = color
        }
    }
}

/**
 * Extension method to load icon from url.
 */
fun MenuItem.loadIconFromUrl(context: Context, imageUrl: String) {
    Glide.with(context).asBitmap()
        .load(imageUrl)
        .into(object : SimpleTarget<Bitmap>(100, 100) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val circularIcon = RoundedBitmapDrawableFactory.create(context.resources, resource)
                circularIcon.isCircular = true
                icon = circularIcon
            }
        })
}

/**
 * Extension method to write preferences.
 */
inline fun SharedPreferences.edit(preferApply: Boolean = false, f: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.f()
    if (preferApply) editor.apply() else editor.commit()
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
 inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}


/**
 * Executes a block if the device's API level is above the specified version.
 * @param api The API level to compare against.
 * @param included Whether to include the specified API level in the condition. Defaults to false.
 * @param block The block to execute if the condition is met.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Executes a block if the device's API level is below the specified version.
 * @param api The API level to compare against.
 * @param included Whether to include the specified API level in the condition. Defaults to false.
 * @param block The block to execute if the condition is met.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}


/**
 * Checks if the current thread is the main (UI) thread.
 * @return True if the current thread is the main thread, false otherwise.
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/**
 * Retrieves the TAG name of the class for logging purposes.
 * @return The simple name of the class.
 */
fun <T : Any> T.TAG(): String = this::class.simpleName ?: "Unknown"

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis() : Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}