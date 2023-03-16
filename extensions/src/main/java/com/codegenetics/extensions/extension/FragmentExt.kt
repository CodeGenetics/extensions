package com.codegenetics.extensions.extension

import android.app.Activity
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.codegenetics.extensions.lib.R

fun Fragment.isAlive(callback: (Activity) -> Unit) {
    if (activity != null && isAdded && !isDetached) {
        activity?.let { it.isActivityAlive { activity -> callback(activity) } }
    }
}

fun Fragment.showProgressBar() {
    this.activity?.showProgressBar()
}

fun Fragment.hideProgressBar() {
    this.activity?.hideProgressBar()
}

/**
 * Extension method to display toast text for Fragment.
 */
fun Fragment?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) = this?.let { activity.toast(text, duration) }

/**
 * Extension method to display toast text for Fragment.
 */
fun Fragment?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) = this?.let { activity.toast(textId, duration) }

/**
 * Extension method to display notification text for Fragment.
 */
inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit) = activity?.notification(body)

/**
 * Extension method to browse url text for Fragment.
 */
fun Fragment.browse(url: String, newTask: Boolean = false) = activity?.browse(url, newTask)

/**
 * Extension method to share text for Fragment.
 */
fun Fragment.share(text: String, subject: String = "") = activity?.share(text, subject)

/**
 * Extension method to make call for Fragment.
 */
fun Fragment.makeCall(number: String) = activity?.makeCall(number)

/**
 * Extension method to send sms for Fragment.
 */
fun Fragment.sendSms(number: String, text: String = "") = activity?.sendSms(number, text)

/**
 * Extension method to rate in playstore for Fragment.
 */
fun Fragment.rate() = activity?.rate()

/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}