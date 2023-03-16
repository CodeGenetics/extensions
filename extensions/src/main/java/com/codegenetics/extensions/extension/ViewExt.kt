package com.codegenetics.extensions.extension

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.codegenetics.extensions.delay
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}


fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.remove(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Toggle a view's visibility
 */
fun View.toggleVisibility(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    return this
}


/**
 * Toggle a view's visibility i.e visible or gone
 */
fun View.toggleRemove(): View {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
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

fun View.setStartMargin(value: Int) = setMargin(value, marginRight, marginTop, marginBottom)

fun View.setEndMargin(value: Int) = setMargin(marginLeft, value, marginTop, marginBottom)

fun View.setBottomMargin(value: Int) = setMargin(marginLeft, marginRight, marginTop, value)

fun View.setTopMargin(value: Int) = setMargin(marginLeft, marginRight, value, marginBottom)

fun View.setVerticalMargin(value: Int) = setMargin(marginLeft, marginRight, value, value)

fun View.setHorizontalMargin(value: Int) = setMargin(value, value, marginTop, marginBottom)

/**
 * Extension method to set View's left padding.
 */
fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

/**
 * Extension method to set View's right padding.
 */
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's top padding.
 */
fun View.setPaddingTop(value: Int) =
    setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

/**
 * Extension method to set View's bottom padding.
 */
fun View.setPaddingBottom(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)

/**
 * Extension method to set View's start padding.
 */
fun View.setPaddingStart(value: Int) =
    setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

/**
 * Extension method to set View's end padding.
 */
fun View.setPaddingEnd(value: Int) =
    setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's horizontal padding.
 */
fun View.setPaddingHorizontal(value: Int) =
    setPaddingRelative(value, paddingTop, value, paddingBottom)

/**
 * Extension method to set View's vertical padding.
 */
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)

/**
 * Extension method to set View's height.
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

/**
 * Extension method to set View's width.
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * Extension method to resize View with height & width.
 */
fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}


/**
 * Extension method to update padding of view.
 *
 */
fun View.updatePadding(
    paddingStart: Int = getPaddingStart(),
    paddingTop: Int = getPaddingTop(),
    paddingEnd: Int = getPaddingEnd(),
    paddingBottom: Int = getPaddingBottom()
) {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
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

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackBar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun View.showSnackBar(msg: String) {
    Snackbar.make(this, msg, BaseTransientBottomBar.LENGTH_LONG).show()
}

fun View.showSnackBar(@StringRes msg: Int) {
    Snackbar.make(
        this, msg, BaseTransientBottomBar.LENGTH_LONG
    ).show()
}

/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 * @author Antonio Leiva
 * @see <a href="https://antonioleiva.com/kotlin-ongloballayoutlistener/>Kotlin recipes: OnGlobalLayoutListener</a>
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * Extension method to get ClickableSpan.
 * e.g.
 * val loginLink = getClickableSpan(context.getColorCompat(R.color.colorAccent), { })
 */
fun View.doOnLayout(onLayout: (View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            if (onLayout(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}

/**
 * Extension method to simplify view binding.
 */
fun <T : ViewDataBinding> View.bind() = DataBindingUtil.bind<T>(this) as T

/**
 * Set an onclick listener
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

/**
 * Extension method to set OnClickListener on a view.
 */
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }

/**
 * Extension method to get a view as bitmap.
 */
fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}

/**
 * Show a snackbar with [message]
 */
fun View.snack(message: String, length: Int = BaseTransientBottomBar.LENGTH_LONG) =
    snack(message, length) {}

/**
 * Show a snackbar with [messageRes]
 */
fun View.snack(@StringRes messageRes: Int, length: Int = BaseTransientBottomBar.LENGTH_LONG) =
    snack(messageRes, length) {}

/**
 * Show a snackbar with [message], execute [f] and show it
 */
inline fun View.snack(
    message: String,
    @Snackbar.Duration length: Int = BaseTransientBottomBar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

/**
 * Show a snackbar with [messageRes], execute [f] and show it
 */
inline fun View.snack(
    @StringRes messageRes: Int,
    @Snackbar.Duration length: Int = BaseTransientBottomBar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, messageRes, length)
    snack.f()
    snack.show()
}

/**
 * Find a parent of type [parentType], assuming it exists
 */
tailrec fun <T : View> View.findParent(parentType: Class<T>): T {
    return if (parent.javaClass == parentType) parent as T else (parent as View).findParent(
        parentType
    )
}

/**
 * Like findViewById but with type interference, assume the view exists
 */
inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById<T>(id)

/**
 *  Like findViewById but with type interference, or null if not found
 */
inline fun <reified T : View> View.findOptional(@IdRes id: Int): T? = findViewById(id) as? T

/**
 * Extension method to inflate layout for ViewGroup.
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

/**
 * Extension method to get views by tag for ViewGroup.
 */
fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}

/**
 * Extension method to remove views by tag ViewGroup.
 */
fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0..childCount - 1) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}



