package com.codegenetics.extensions.extension

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot

fun ImageView.colorFilter(@ColorRes colorId: Int) {
    setColorFilter(ContextCompat.getColor(context, colorId))
}

@Deprecated("use load Image Instead",
    ReplaceWith("loadImage(img)", "com.codegenetics.extensions.extension")
)
fun ImageView.loadImageWithGlide(url: Any?) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadImageWithBlurEffect(image: Any) {
    Glide.with(this)
        .load(image)
        .apply(RequestOptions().override(10).centerCrop())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.rotate() {
    val animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
    animator.duration = 1000
    animator.repeatCount = ObjectAnimator.INFINITE
    animator.interpolator = LinearInterpolator()
    animator.start()
}


/**
 * Extension function to load an image into an ImageView using Glide.
 *
 * This function simplifies image loading for an ImageView by handling optional placeholders,
 * error images, and circular cropping. It ensures that the operation is lifecycle-safe by
 * validating the context before proceeding.
 *
 * @param url The URL or resource ID of the image to load. Accepts `String` for URLs or `Int` for resource IDs.
 *            If the URL is `null`, the function will do nothing.
 * @param placeholder The resource ID of the placeholder image to display while the image loads (optional).
 * @param errorPlaceholder The resource ID of the error image to display if the image load fails (optional).
 *
 * Usage Example:
 * ```
 * imageView.loadImage(
 *     url = "https://example.com/image.png",
 *     placeholder = R.drawable.placeholder,
 *     errorPlaceholder = R.drawable.error_placeholder,
 *     isCircular = true
 * )
 * ```
 */
fun ImageView.loadImage(
    url: Any?,
    placeholder: Int? = null,
    errorPlaceholder: Int? = null,
) {
    when (url) {
        is Int -> if (url <= 0) return
        is String -> if (url.toString().isEmpty()) return
    }
    val context = this.context
    if (context.isValidContext()) {
        url?.let {
            val request = Glide.with(this).load(it)
            placeholder?.let { placeholderRes -> request.placeholder(placeholderRes) }
            errorPlaceholder?.let { errorRes -> request.error(errorRes) }
            request.into(this)
        }
    }
}

/**
 * Extension function to load an image into an ImageView using Glide.
 *
 * @param url The URL or resource ID of the image to load. Accepts `String` or `Int`.
 * @param placeholder The resource ID of the placeholder image to display while the image loads (optional).
 * @param errorPlaceholder The resource ID of the error image to display if the image load fails (optional).
 * @param isCircular Set to `true` if the image should be displayed with a circular crop (default: `false`).
 * @param onSuccess Callback invoked when the image is successfully loaded (optional).
 * @param onFailure Callback invoked when the image load fails. Provides the exception (`GlideException?`) for debugging (optional).
 *
 * Usage Example:
 * ```
 * imageView.loadImage(
 *     url = "https://example.com/image.png",
 *     placeholder = R.drawable.placeholder,
 *     errorPlaceholder = R.drawable.error_placeholder,
 *     isCircular = true,
 *     onSuccess = {
 *         Log.d("ImageViewExtension", "Image loaded successfully!")
 *     },
 *     onFailure = { exception ->
 *         Log.e("ImageViewExtension", "Image load failed", exception)
 *     }
 * )
 * ```
 */
fun ImageView.loadImageWithCallback(
    url: Any?,
    placeholder: Int? = null,
    errorPlaceholder: Int? = null,
    isCircular: Boolean = false,
    onSuccess: (() -> Unit)? = null,
    onFailure: ((Exception?) -> Unit)? = null
) {
    when (url) {
        is Int -> if (url <= 0) return
        is String -> if (url.toString().isEmpty()) return
    }
    val context = this.context
    if (context.isValidContext()) {
        url?.let {
            val request = Glide.with(this)
                .load(it)
                .apply {
                    placeholder?.let { placeholderRes -> placeholder(placeholderRes) }
                    errorPlaceholder?.let { errorRes -> error(errorRes) }
                    if (isCircular) circleCrop()
                }
                .listener(object : com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable> {
                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable>,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        onSuccess?.invoke()
                        return false // Allow Glide to handle setting the image on the ImageView
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        onFailure?.invoke(e)
                        return false // Allow Glide to handle the error placeholder
                    }
                })

            request.into(this)
        }
    }
}


