package com.codegenetics.extensions.extension

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.text.Editable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

fun Any.twoDecimal(): String {
    val df = DecimalFormat("0.00")
    return df.format(this.toString().toDouble())
}

fun Any.roundUpToTwoDecimalsWithoutRounding(): String? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this.toString().toDouble())
}

/**
 * Extension method to int time to 2 digit String
 */
fun Int.twoDigitTime() = if (this < 10) "0" + toString() else toString()

/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue].
 */
fun Editable.replaceAll(newValue: String) {
    replace(0, length, newValue)
}

/**
 * Extension method to replace all text inside an [Editable] with the specified [newValue] while
 * ignoring any [android.text.InputFilter] set on the [Editable].
 */
fun Editable.replaceAllIgnoreFilters(newValue: String) {
    val currentFilters = filters
    filters = emptyArray()
    replaceAll(newValue)
    filters = currentFilters
}

/**
 * Extension method to get ClickableSpan.
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
 * Extension method to check is aboveApi.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Extension method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}

/**
 * Extension method check if is Main Thread.
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName

/**
 * Convert a given date to milliseconds
 */
fun Date.toMillis() : Long {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.timeInMillis
}