package com.codegenetics.extensions.extension

import android.app.Activity
import android.widget.ProgressBar
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
