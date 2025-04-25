package com.codegenetics.extensions.extension

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager

fun Window.blur() {
    this.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
}

fun Window.edgeToEdgeDisplay(){
    this.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

fun Window.restrictScreenshot(){
    this.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
}

fun Window.hideNavigationBar(){
    this.decorView.systemUiVisibility = (android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
}

/**
 * Sets the color of the status bar icons (clock, battery, etc.).
 *
 * On Android R (API 30) and above, it uses `WindowInsetsController` to control the appearance.
 * On earlier versions, it uses `systemUiVisibility`.
 *
 * @param isLight `true` to set light (dark) icons, `false` to set dark (light) icons.
 *                - If `true`, status bar icons will be dark (e.g., black). This is typically used when the status bar background is light.
 *                - If `false`, status bar icons will be light (e.g., white). This is typically used when the status bar background is dark.
 */
fun Window.setStatusBarIconsColor(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
            if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        decorView.systemUiVisibility = if (isLight)
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            0 // Default (white icons)
    }
}

/**
 * Sets the status bar icons to a dark color.
 *
 * This function modifies the system UI flags of the window to display
 * status bar icons (such as battery, network, and clock) in a dark color.
 * This is typically used when the status bar background is light,
 * to ensure the icons are visible.
 *
 * It utilizes the [setStatusBarIconsColor] function internally to achieve this.
 *
 * **Note:** This requires the window to be attached to a view hierarchy
 * and the window's decor view to be available. It also only works on API 23+ (Marshmallow).
 *
 * @see setStatusBarIconsColor
 */
fun Window.statusBarWithDarkIcons(){
    setStatusBarIconsColor(true)
}

/**
 * Sets the status bar to have light icons and text.
 *
 * This function configures the system's status bar to display light-colored icons and text,
 * making them visible against a darker background. This is typically used when the app's
 * theme uses a dark color for the status bar background.
 *
 * It internally uses the `setStatusBarIconsColor` function with the parameter `isDark` set to `false`
 * which controls the light/dark status bar icons.
 *
 * @see setStatusBarIconsColor
 * @receiver Window The window to which the status bar settings will be applied.
 */
fun Window.statusBarWithLightIcons(){
    setStatusBarIconsColor(false)
}

