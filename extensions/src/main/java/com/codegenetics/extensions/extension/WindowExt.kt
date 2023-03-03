package com.codegenetics.extensions.extension

import android.app.Activity
import android.util.DisplayMetrics
import android.view.ViewTreeObserver
import android.view.Window
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

