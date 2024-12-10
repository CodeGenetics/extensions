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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

/**
 * Sets the status bar color and adjusts the icon color (light/dark).
 * @param colorId Resource ID of the desired color.
 * @param iconColorType Specifies whether the status bar icons should be dark or light.
 */
fun Activity.setStatusBarColor(
    @ColorRes colorId: Int,
    iconColorType: StatusIconColorType = StatusIconColorType.Light
) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.apply {
                statusBarColor = ContextCompat.getColor(this@setStatusBarColor, colorId)
                decorView.systemUiVisibility = when (iconColorType) {
                    StatusIconColorType.Dark -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    StatusIconColorType.Light -> 0
                }
            }
        } else {
            window.statusBarColor = ContextCompat.getColor(this, colorId)
        }
    } catch (e: Exception) {
        Log.e("StatusBarColor", "Error setting status bar color", e)
    }
}

@Deprecated("Use statusBarColor instead")
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

/**
 * Navigates to the app's settings page in the system settings.
 * @param requestCode The request code for identifying the result in onActivityResult.
 */
fun Activity.navigateToSettings(requestCode: Int = 555) {
    try {
        val dialogIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivityForResult(dialogIntent, requestCode)
    } catch (e: Exception) {
        Log.e("NavigateToSettings", "Error navigating to settings", e)
    }
}

/**
 * Determines if the system theme is currently set to dark mode.
 * @return True if the system theme is dark, false otherwise.
 */
fun Activity.isSystemThemeDark(): Boolean {
    return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

/**
 * Retrieves the height of the navigation bar, if it is visible.
 * @return The height of the navigation bar in pixels, or 0 if not visible.
 */
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
        e.printStackTrace()
    }
}

fun Activity.hideProgressBar() {
    try {
        findViewById<ProgressBar>(R.id.pb)?.gone()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Activity.getScreenSize(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

/**
 * Turns on the screen if it is currently off, using a wake lock.
 */
@SuppressLint("InvalidWakeLockTag")
fun Activity.turnOnScreen() {
    try {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isInteractive) {
            pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                "MyLock"
            ).apply {
                acquire(20000)
                release()
            }
        }
    } catch (ex: Exception) {
        Log.e("TurnOnScreen", "Error turning on screen", ex)
    }
}

/**
 * Extension method to set Status Bar Color and Status Bar Icon Color Type(dark/light)
 */
enum class StatusIconColorType {
    Dark, Light
}


/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

@Deprecated("use addFragment instead",
    ReplaceWith("replaceFragment")
)
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
 * Replaces the fragment in the specified container view.
 * @param fragment The fragment to display.
 * @param frameId The ID of the container view.
 * @param addToBackStack Whether to add the transaction to the back stack.
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes frameId: Int, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        replace(frameId, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}

/**
 * Adds the fragment to the specified container view.
 * @param fragment The fragment to add.
 * @param frameId The ID of the container view.
 * @param addToBackStack Whether to add the transaction to the back stack.
 */
fun AppCompatActivity.addFragment(fragment: Fragment, @IdRes frameId: Int, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        add(frameId, fragment)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}

@Deprecated("Use addFragment instead",
    ReplaceWith("addFragment")
)
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

/**
 * Checks whether a permission rationale should be displayed.
 * @param permission The permission to check.
 * @return True if a rationale should be displayed, false otherwise.
 */
fun Activity.checkPermissionRationale(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        shouldShowRequestPermissionRationale(permission)
    } else false
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

fun Activity.isValidLifeCycle(fragmentManager: FragmentManager): Boolean =
    !this.isFinishing && fragmentManager.isDestroyed.not()

fun Activity.finishToDown() {
    this.finish()
    this.overridePendingTransition(0, R.anim.slide_out_down)
}

fun Activity.finishAffinityToDown() {
    this.finishAffinity()
    this.overridePendingTransition(0, R.anim.slide_out_down)
}


/**
 * Retrieves an intent extra of the specified type.
 * @param key The key for the intent extra.
 * @param defaultValue The default value if the extra is not found.
 * @return The value of the extra, or the default value.
 */
inline fun <reified T> Activity.getIntentExtra(key: String, defaultValue: T): T {
    return when (T::class) {
        Boolean::class -> intent?.getBooleanExtra(key, defaultValue as Boolean) as T
        Int::class -> intent?.getIntExtra(key, defaultValue as Int) as T
        String::class -> intent?.getStringExtra(key) as T ?: defaultValue
        Float::class -> intent?.getFloatExtra(key, defaultValue as Float) as T
        Long::class -> intent?.getLongExtra(key, defaultValue as Long) as T
        else -> defaultValue
    }
}
