package com.codegenetics.extensions.extension

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.isAlive(callback: (Activity) -> Unit) {
    if (activity != null && isAdded && !isDetached) {
        activity?.let { it.isActivityAlive { activity -> callback(activity) } }
    }
}