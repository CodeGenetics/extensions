package com.codegenetics.extensions.extension

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/** return activity instance in callback
 * check if it is not finishing and destroyed*/
fun Activity.isActivityAlive(callback: (Activity) -> Unit) {
    try {
        if (isFinishing.not() && isDestroyed.not()) {
            callback(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.setStatusBarColor(@ColorRes colorId: Int) {
    try {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
                .and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, colorId)
        window.navigationBarColor = ContextCompat.getColor(this, colorId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
/** navigate you to the system setting of that app.
 * default @param[requestCode]=555
 * provide @param[requestCode] to get result in
 * onActivityResult*/
fun Activity.navigateToSettings(requestCode: Int = 555) {
    try {
        val dialogIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        dialogIntent.data = uri
        startActivityForResult(dialogIntent, requestCode)
    } catch (e: WindowManager.BadTokenException) {
        e.printStackTrace()
    }
}

fun Activity.isSystemThemeDark(): Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> false
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
}

/** return Height of tha system Bottom Navigation */
fun Activity.getNavHeight(): Int {
    val decorView = this.window.decorView
    var h = 0

    val isNavigationBarShowing = decorView.height != this.windowManager.defaultDisplay.height

    if (!isNavigationBarShowing) {
        return 0
    }

    val resourceId = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        h = this.resources.getDimensionPixelSize(resourceId)
    } else {
        decorView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val height = decorView.height
                val displayMetrics = DisplayMetrics()
                this@getNavHeight.windowManager.defaultDisplay.getMetrics(displayMetrics)
                val screenHeight = displayMetrics.heightPixels
                h = screenHeight - height
            }
        })
    }
    return h
}
