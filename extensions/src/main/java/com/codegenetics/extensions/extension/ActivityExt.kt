package com.codegenetics.extensions.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.codegenetics.extensions.lib.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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

fun Activity.showProgressBar() {
    try {
        findViewById<ProgressBar>(R.id.pb)?.show()
    } catch (e: Exception) {
    }
}

fun Activity.hideProgressBar() {
    try {
        findViewById<ProgressBar>(R.id.pb)?.gone()
    } catch (e: Exception) {
    }
}


fun Activity.getScreenSize(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun Activity.turnOnScreen() {
    try {
        val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isScreenOn
        if (!isScreenOn) {
            @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                "MyLock"
            )
            wl.acquire(20000)
            @SuppressLint("InvalidWakeLockTag") val wl_cpu =
                pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock")
            wl_cpu.acquire(20000)
        }
    } catch (ex: Exception) {
        Log.e("TAG", "turnOnScreen: ", ex)
    }
}

/**
 * Extension method to set Status Bar Color and Status Bar Icon Color Type(dark/light)
 */
enum class StatusIconColorType {
    Dark, Light
}

fun Activity.setStatusBarColor(
    color: Int,
    iconColorType: StatusIconColorType = StatusIconColorType.Light
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            statusBarColor = color
            decorView.systemUiVisibility = when (iconColorType) {
                StatusIconColorType.Dark -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                StatusIconColorType.Light -> 0
            }
        }
    } else
        this.window.statusBarColor = color
}

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

/**
 * Setup actionbar
 */
fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

/**
 * Extension method to get ContentView for ViewGroup.
 */
fun Activity.getContentView(): ViewGroup {
    return this.findViewById(android.R.id.content) as ViewGroup
}

fun Activity.checkPermissionRationale(
    permission: String
): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale(permission)
        Log.d("PermissionLogs", "checkPermissionRationale: $shouldShowRequestPermissionRationale")
        shouldShowRequestPermissionRationale
    } else {
        Log.d("PermissionLogs", "checkPermissionRationaleElse: false")
        false
    }
}

/** Directly call launch from activity for Coroutine Default
 * attached with lifecycle*/
fun AppCompatActivity.launchDefault(
    context: CoroutineContext = Dispatchers.Default,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.launch(context = context, block = block)
}

/** Directly call launch from activity for Coroutine Main
 * attached with lifecycle*/
fun AppCompatActivity.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launchDefault(context = Dispatchers.Main, block = block)
}

/** Directly call launch from activity for Coroutine IO
 * attached with lifecycle*/
fun AppCompatActivity.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return launchDefault(Dispatchers.IO, block = block)
}
