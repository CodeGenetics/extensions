package com.codegenetics.extensions.extension

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.codegenetics.extensions.delay
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun View.animateView(@AnimRes animationId: Int, duration: Long = 500L) {
    val animation = AnimationUtils.loadAnimation(context, animationId)
    animation.duration = duration
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit
        override fun onAnimationRepeat(animation: Animation?) = Unit
        override fun onAnimationEnd(animation: Animation?) {
        }
    })
    startAnimation(animation)
}

fun View.animateViewEndless(@AnimRes animationId: Int, duration: Long = 500L) {
    val animation = AnimationUtils.loadAnimation(context, animationId)
    animation.duration = duration
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit
        override fun onAnimationRepeat(animation: Animation?) = Unit
        override fun onAnimationEnd(animation: Animation?) {
            animateViewEndless(animationId, duration)
        }
    })
    startAnimation(animation)
}

fun View.animateFade(duration: Long = 500L) {
    val anim = ValueAnimator.ofFloat(0f, 1f)
    anim.repeatCount = ValueAnimator.INFINITE
    anim.repeatMode = ValueAnimator.RESTART
    anim.duration = duration
    anim.addUpdateListener {
        val value = it.animatedValue as Float
        this.alpha = value
        this.scaleX = value
        this.scaleY = value
    }
    anim.start()
}

fun View.animateViewEndlessSwing(
    amplitude: Float = 10f,
    frequency: Float = 5f,
    duration: Long = 2000L
) {
    val animator = ObjectAnimator.ofFloat(this, "rotation", 0f, amplitude, 0f, -amplitude, 0f)
    animator.repeatCount = ObjectAnimator.INFINITE
    animator.repeatMode = ObjectAnimator.RESTART
    animator.duration = duration
    animator.interpolator = CycleInterpolator(frequency)
    animator.start()
}

fun View.hideWithAnimation(@AnimRes animationId: Int, callback: () -> Unit) {
    try {
        val animation = AnimationUtils.loadAnimation(context, animationId)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit
            override fun onAnimationRepeat(animation: Animation?) = Unit
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.INVISIBLE
                callback()
            }
        })
        requestLayout()
        startAnimation(animation)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.backgroundColor(@ColorRes colorId: Int) {
    setBackgroundColor(ContextCompat.getColor(context, colorId))
}

/** click listener with a delay of 300 ms
 * to avoid accidental double click*/
fun View.setSmartClickListener(callback: (View) -> Unit) {
    this.setOnClickListener {
        isEnabled = false
        callback(this)
        delay(300) { isEnabled = true }
    }
}

fun View.setMargin(left: Int, right: Int, top: Int, bottom: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(left, top, right, bottom)
    this.layoutParams = param
}

fun View.setStartMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(value, 0, 0, 0)
    this.layoutParams = param
}

fun View.setEndMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(0, 0, value, 0)
    this.layoutParams = param
}

fun View.setBottomMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(0, 0, 0, value)
    this.layoutParams = param
}

fun View.setTopMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(0, value, 0, 0)
    this.layoutParams = param
}

fun View.setVerticalMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(0, value, 0, value)
    this.layoutParams = param
}

fun View.setHorizontalMargin(value: Int) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(value, 0, value, 0)
    this.layoutParams = param
}

/** change background but view should have
 * a selector background in xml
 * */
fun View.changeBackgroundColor(color: Int) {
    try {
        val drawable = this.background as GradientDrawable
        drawable.setColor(color)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.setOnclickWithInternetCheck(callback: (View) -> Unit) {
    this.setSmartClickListener {
        if (this.context.isInternetConnected())
            callback(this)
        else this.context.toast("No Internet Connection")
    }
}

fun View.rotate() {
    val animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
    animator?.duration = 3000
    animator?.repeatCount = ObjectAnimator.INFINITE
    animator?.interpolator = LinearInterpolator()

    animator?.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            // Do nothing
        }

        override fun onAnimationEnd(animation: Animator) {
            // Start next activity

        }

        override fun onAnimationCancel(animation: Animator) {
            // Do nothing
        }

        override fun onAnimationRepeat(animation: Animator) {
            // Do nothing
        }
    })

    animator?.start()

}

fun View.gotoSettingsWithSnackBar() {
    Snackbar.make(
        this,
        "Above permission(s) needed. Please allow in your application settings.",
        BaseTransientBottomBar.LENGTH_INDEFINITE
    ).setAction("Settings") {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }.show()
}

fun View.showSnackBar(msg: String) {
    Snackbar.make(this, msg, BaseTransientBottomBar.LENGTH_LONG).show()
}

fun View.showSnackBar(@StringRes msg: Int) {
    Snackbar.make(
        this, msg, BaseTransientBottomBar.LENGTH_LONG
    ).show()
}