package com.codegenetics.extensions.extension

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import kotlin.math.hypot

/**
 * Starts a pulse flash animation on the ImageView.
 *
 * This function creates a combination of scaling and alpha animations to produce a "pulse" effect on the ImageView.
 * The animation repeats indefinitely until the specified `totalDuration` has elapsed, after which it is stopped.
 *
 * @param totalDuration The total duration of the animation in milliseconds. Defaults to 3000L (3 seconds).
 * @param pulseScale The maximum scale factor to apply during the pulse. A value of 1.2f means the view will grow to 120% of its original size. Defaults to 1.2f.
 * @param pulseDuration The duration of a single pulse (scale up and down) in milliseconds. Defaults to 500L (0.5 seconds).
 * @param alphaMin The minimum alpha value to reach during the flash. A value of 0.3f means the view will fade to 30% opacity. Defaults to 0.3f.
 * @param removeAfter Whether to remove the ImageView from its parent after the animation completes. Defaults to false.
 * @param onFinish An optional callback function to be invoked when the animation is finished. Defaults to null.
 *
 * Example Usage:
 * ```kotlin
 * imageView.startPulseFlash(totalDuration = 5000L, pulseScale = 1.3f, alphaMin = 0.5f, removeAfter = true) {
 *     println("Pulse flash animation finished!")
 * }
 * ```
 */
fun View.startPulseFlash(
    totalDuration: Long = 3_000L,
    pulseScale: Float = 1.2f,
    pulseDuration: Long = 500L,
    alphaMin: Float = .3f,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    // scale X/Y animators
    val scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, pulseScale).apply {
        duration = pulseDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1f, pulseScale).apply {
        duration = pulseDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    // alpha animator
    val flash = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, alphaMin).apply {
        duration = pulseDuration / 2
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    // play together with a smooth interpolator
    val set = AnimatorSet().apply {
        playTogether(scaleX, scaleY, flash)
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }

    // stop it & optionally remove the view after totalDuration
    postDelayed({
        set.cancel()
        clearAnimation()
        if (removeAfter) {
            (parent as? ViewGroup)?.removeView(this)
        }
        onFinish?.invoke()
    }, totalDuration)
}


/**
 * Starts a shaking animation on the View.
 *
 * This function creates a shaking effect by repeatedly moving the View left and right
 * along the X-axis. The animation continues for the specified [totalDuration] and can
 * optionally remove the view from its parent after the animation is complete.
 *
 * @param totalDuration The total duration of the shaking animation in milliseconds. Defaults to 2000ms.
 * @param shakeDistance The maximum distance in pixels the view will move to the left or right. Defaults to 25f.
 * @param shakeDuration The duration of each individual shake (left or right movement) in milliseconds. Defaults to 100ms.
 * @param removeAfter If true, the view will be removed from its parent after the animation completes. Defaults to false.
 * @param onFinish An optional callback that will be invoked when the animation finishes. Defaults to null.
 */
fun View.startShake(
    totalDuration: Long = 2000L,
    shakeDistance: Float = 25f,
    shakeDuration: Long = 100L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    // Move left/right
    val left = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, -shakeDistance).apply {
        duration = shakeDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val right = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, shakeDistance).apply {
        duration = shakeDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    val set = AnimatorSet().apply {
        playSequentially(left, right)
        start()
    }

    postDelayed({
        set.cancel()
        translationX = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a bouncing animation on the View.
 *
 * This function animates the view vertically, creating a bouncing effect. The view moves up and down repeatedly
 * within the specified [bounceHeight] until the [totalDuration] has elapsed. After the [totalDuration], the animation is stopped,
 * the view's `translationY` is reset to 0, and optionally, the view is removed from its parent.
 *
 * @param totalDuration The total duration of the bouncing animation in milliseconds. Default is 2000ms.
 * @param bounceHeight The maximum height the view will bounce up in pixels. Default is 50 pixels.
 * @param bounceDuration The duration of a single up or down bounce in milliseconds. Default is 300ms.
 * @param removeAfter If true, the view will be removed from its parent after the animation is finished. Default is false.
 * @param onFinish An optional lambda function that will be called after the animation is finished and the view is potentially removed.
 *
 * Example Usage:
 * ```
 *  myView.startBounce(totalDuration = 3000L, bounceHeight = 100f, removeAfter = true) {
 *      println("Bounce animation finished and view removed.")
 *  }
 * ```
 */
fun View.startBounce(
    totalDuration: Long = 2000L,
    bounceHeight: Float = 50f,
    bounceDuration: Long = 300L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val up = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0f, -bounceHeight).apply {
        duration = bounceDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    val set = AnimatorSet().apply {
        play(up)
        start()
    }

    postDelayed({
        set.cancel()
        translationY = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a spinning animation on the View.
 *
 * This function applies a continuous rotation animation to the target View. The animation can be
 * customized with several parameters to control its behavior, duration, and appearance.
 *
 * @param totalDuration The total duration of the spinning animation in milliseconds.
 *                      After this time, the animation will stop, the view's rotation will be reset to 0,
 *                      and the `onFinish` callback (if provided) will be invoked. Defaults to 3000L (3 seconds).
 * @param rotationsPerCycle The number of full rotations the View should complete within each cycle.
 *                          A value of 1f means one full rotation (360 degrees).
 *                          A value of 0.5f means a half rotation (180 degrees).
 *                          Defaults to 1f.
 * @param cycleDuration The duration of each rotation cycle in milliseconds.
 *                      This determines how fast each full or partial rotation will occur.
 *                      Defaults to 500L (0.5 seconds).
 * @param removeAfter If true, the View will be removed from its parent after the animation finishes.
 *                    Defaults to false.
 * @param onFinish An optional callback function that will be invoked after the animation finishes.
 *                 Defaults to null.
 */
fun View.startSpin(
    totalDuration: Long = 3000L,
    rotationsPerCycle: Float = 1f,
    cycleDuration: Long = 500L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val spin = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f * rotationsPerCycle).apply {
        duration = cycleDuration
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
    }
    spin.start()

    postDelayed({
        spin.cancel()
        rotation = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a fade-blink animation on the View.
 *
 * This function animates the View's alpha value, creating a blinking effect. The animation consists
 * of repeatedly fading the View from full opacity (1.0) to a minimum alpha value and back.
 * The animation stops after the specified total duration, and optionally removes the view from its parent.
 *
 * @param totalDuration The total duration of the blink animation in milliseconds. Default is 2000L (2 seconds).
 * @param minAlpha The minimum alpha value to which the View will fade. Should be between 0.0 and 1.0. Default is 0.2f.
 * @param blinkDuration The duration of a single fade-in/fade-out cycle in milliseconds. Default is 400L.
 * @param removeAfter If true, the View will be removed from its parent ViewGroup after the animation finishes. Default is false.
 * @param onFinish An optional lambda to be invoked after the animation finishes. Default is null.
 */
fun View.startFadeBlink(
    totalDuration: Long = 2000L,
    minAlpha: Float = 0.2f,
    blinkDuration: Long = 400L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val blink = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, minAlpha).apply {
        duration = blinkDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    blink.start()

    postDelayed({
        blink.cancel()
        alpha = 1f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "wobble" animation on the View.
 *
 * This function creates a back-and-forth rotation animation, simulating a wobble effect.
 * The animation consists of alternating rotations to the left and right, around the View's center.
 *
 * @param totalDuration The total duration of the entire wobble animation in milliseconds. After this time the animation will stop. Defaults to 2500ms.
 * @param wobbleAngle The maximum angle (in degrees) the View will rotate to either side during the wobble. Defaults to 15 degrees.
 * @param wobbleDuration The duration of each individual rotation (to the left or right) in milliseconds. Defaults to 150ms.
 * @param removeAfter If true, the View will be removed from its parent after the animation finishes. Defaults to false.
 * @param onFinish An optional lambda function to be executed when the animation is finished. Defaults to null.
 *
 * Example usage:
 * ```kotlin
 * myView.startWobble(
 *     totalDuration = 3000L,
 *     wobbleAngle = 10f,
 *     wobbleDuration = 200L,
 *     removeAfter = true,
 *     onFinish = {
 *         println("Wobble animation finished!")
 *     }
 * )
 * ```
 */
fun View.startWobble(
    totalDuration: Long = 2500L,
    wobbleAngle: Float = 15f,
    wobbleDuration: Long = 150L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val left = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, -wobbleAngle).apply {
        duration = wobbleDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val right = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, wobbleAngle).apply {
        duration = wobbleDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    val set = AnimatorSet().apply {
        playSequentially(left, right)
        start()
    }

    postDelayed({
        set.cancel()
        rotation = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a fade-in-out animation on a View.
 *
 * This function animates the alpha (transparency) of a View, making it repeatedly
 * fade out to a specified minimum alpha and then fade back in to full opacity.
 * After a specified total duration, the animation is stopped, the alpha is reset to 1f,
 * and optionally the view is removed from its parent.
 *
 * @param totalDuration The total duration of the fade-in-out effect in milliseconds.
 *                      The animation will stop after this duration. Defaults to 2000L (2 seconds).
 * @param minAlpha The minimum alpha value the view will fade out to. Should be between 0f (fully transparent) and 1f (fully opaque). Defaults to 0f.
 * @param fadeDuration The duration of each fade-in or fade-out animation in milliseconds. Defaults to 500L (0.5 seconds).
 * @param removeAfter If true, the view will be removed from its parent after the total duration. Defaults to false.
 * @param onFinish An optional callback function that will be invoked when the animation is finished. Defaults to null.
 */
fun View.startFadeInOut(
    totalDuration: Long = 2000L,
    minAlpha: Float = 0f,
    fadeDuration: Long = 500L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val blink = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, minAlpha).apply {
        duration = fadeDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }.also { it.start() }

    postDelayed({
        blink.cancel()
        alpha = 1f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a horizontal sliding animation on the view.
 *
 * This function creates a looping animation that slides the view horizontally, first to the left and then to the right,
 * repeatedly until the total duration is reached. After the total duration, the animation stops,
 * the view's horizontal translation is reset to 0, and optionally, the view is removed from its parent.
 *
 * @param totalDuration The total duration of the animation in milliseconds. Defaults to 2000L (2 seconds).
 * @param distance The distance the view will slide horizontally in pixels. Defaults to 300f.
 * @param slideDuration The duration of each individual slide (left or right) in milliseconds. Defaults to 400L.
 * @param removeAfter Whether to remove the view from its parent after the animation finishes. Defaults to false.
 * @param onFinish An optional callback function to be executed after the animation finishes. Defaults to null.
 */
fun View.startSlideHoriz(
    totalDuration: Long = 2000L,
    distance: Float = 300f,
    slideDuration: Long = 400L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val left = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, -distance).apply {
        duration = slideDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val right = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, distance).apply {
        duration = slideDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    AnimatorSet().apply {
        playSequentially(left, right)
        start()
    }

    postDelayed({
        translationX = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a flip animation on the View.
 *
 * This function applies a Y-axis rotation animation to the view, simulating a flipping effect.
 * The animation will repeat indefinitely (reversing direction each time) for a set duration,
 * after which it will be stopped, the view's rotation will be reset, and optionally the view
 * will be removed from its parent.
 *
 * @param totalDuration The total duration (in milliseconds) for which the flip animation should run.
 *                      After this time, the animation is canceled. Defaults to 2000L (2 seconds).
 * @param flipDuration The duration (in milliseconds) of a single flip (from 0 to 180 degrees).
 *                     Defaults to 600L (0.6 seconds).
 * @param removeAfter If true, the view will be removed from its parent after the total duration has elapsed.
 *                    Defaults to false.
 * @param onFinish An optional callback function that will be invoked after the total duration has elapsed
 *                 and the animation is canceled. Defaults to null.
 */
fun View.startFlip(
    totalDuration: Long = 2000L,
    flipDuration: Long = 600L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val flip = ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0f, 180f).apply {
        duration = flipDuration
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
    }.also { it.start() }

    postDelayed({
        flip.cancel()
        rotationY = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "heartbeat" animation on the View.
 *
 * This function creates a scaling animation that makes the view pulse like a heartbeat.
 * It scales the view up and down repeatedly for a specified duration.
 *
 * @param totalDuration The total duration of the heartbeat animation in milliseconds. Defaults to 2000ms.
 * @param scaleUp The scale factor to which the view will scale up during the beat. Defaults to 1.2f.
 * @param beatDuration The duration of each individual beat (scale up and down) in milliseconds. Defaults to 300ms.
 * @param removeAfter If true, the view will be removed from its parent after the animation finishes. Defaults to false.
 * @param onFinish A callback function that will be invoked when the animation finishes. Defaults to null.
 *
 * Example Usage:
 * ```
 * myView.startHeartbeat(totalDuration = 3000L, scaleUp = 1.3f, removeAfter = true) {
 *     println("Heartbeat animation finished!")
 * }
 * ```
 */
fun View.startHeartbeat(
    totalDuration: Long = 2000L,
    scaleUp: Float = 1.2f,
    beatDuration: Long = 300L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, scaleUp).apply {
        duration = beatDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1f, scaleUp).apply {
        duration = beatDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    AnimatorSet().apply {
        playTogether(scaleX, scaleY)
        start()
    }

    postDelayed({
        scaleX.cancel(); scaleY.cancel()
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "rubber band" animation on the View.
 *
 * This function animates the view's scaleX and scaleY properties to create a rubber band effect.
 * It scales the view up and down repeatedly for a specified duration, then optionally removes
 * the view from its parent and invokes a finish callback.
 *
 * @param totalDuration The total duration of the animation, including the repeated rubber effect and any delay before stopping. Defaults to 2000 milliseconds.
 * @param scaleXUp The maximum scale factor for the X-axis during the animation. Defaults to 1.25.
 * @param scaleYUp The minimum scale factor for the Y-axis during the animation. Defaults to 0.75.
 * @param rubberDuration The duration of each individual scaling animation (up and down). Defaults to 400 milliseconds.
 * @param removeAfter Whether to remove the view from its parent after the animation completes. Defaults to false.
 * @param onFinish An optional callback to be invoked after the animation completes and the view is reset. Defaults to null.
 *
 * Usage example:
 * ```
 * myView.startRubberBand(
 *     totalDuration = 3000L,
 *     scaleXUp = 1.5f,
 *     scaleYUp = 0.5f,
 *     rubberDuration = 500L,
 *     removeAfter = true,
 *     onFinish = {
 *         println("Rubber band animation finished!")
 *     }
 * )
 * ```
 */
fun View.startRubberBand(
    totalDuration: Long = 2000L,
    scaleXUp: Float = 1.25f,
    scaleYUp: Float = 0.75f,
    rubberDuration: Long = 400L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val sx = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, scaleXUp).apply {
        duration = rubberDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val sy = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1f, scaleYUp).apply {
        duration = rubberDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    AnimatorSet().apply {
        playTogether(sx, sy)
        start()
    }

    postDelayed({
        sx.cancel(); sy.cancel()
        scaleX = 1f; scaleY = 1f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "wiggle" animation on the View.
 *
 * This function applies a back-and-forth rotation animation to the View, creating a "wiggle" effect.
 * The animation consists of two parts: rotating to the left by the specified [angle], and then rotating to the right by the same [angle].
 * These two rotations are repeated sequentially until the [totalDuration] is reached.
 *
 * @param totalDuration The total duration of the wiggle animation in milliseconds. Defaults to 2000L (2 seconds).
 * @param angle The maximum angle of rotation (in degrees) for the wiggle. A positive value rotates clockwise, and a negative value rotates counterclockwise. Defaults to 5f.
 * @param wiggleDuration The duration of each individual wiggle (left or right rotation) in milliseconds. Defaults to 100L (100 milliseconds).
 * @param removeAfter If true, the View will be removed from its parent ViewGroup after the animation finishes. Defaults to false.
 * @param onFinish An optional callback function that will be invoked when the animation finishes. Defaults to null.
 */
fun View.startWiggle(
    totalDuration: Long = 2000L,
    angle: Float = 5f,
    wiggleDuration: Long = 100L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val left = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, -angle).apply {
        duration = wiggleDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val right = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, angle).apply {
        duration = wiggleDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    AnimatorSet().apply {
        playSequentially(left, right)
        start()
    }
    postDelayed({
        rotation = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "pop" animation on the View.
 *
 * This function creates an animation that scales the View up and then back down to its original size,
 * creating a "pop" effect.
 *
 * @param popScale The maximum scale the View will reach during the animation. Defaults to 1.3f.
 *                 A value greater than 1.0f will make the view larger than its original size,
 *                 and a value between 0.0f and 1.0f will make it smaller.
 * @param popDuration The duration of the animation in milliseconds. Defaults to 300L.
 * @param onFinish An optional callback that will be invoked when the animation finishes. Defaults to null.
 */
fun View.startPop(
    popScale: Float = 1.3f,
    popDuration: Long = 300L,
    onFinish: (() -> Unit)? = null
) {
    AnimatorSet().apply {
        playTogether(
            ObjectAnimator.ofFloat(this@startPop, View.SCALE_X, 1f, popScale, 1f),
            ObjectAnimator.ofFloat(this@startPop, View.SCALE_Y, 1f, popScale, 1f)
        )
        duration = popDuration
        interpolator = OvershootInterpolator()
        doOnEnd { onFinish?.invoke() }
        start()
    }
}

/**
 * Starts a highlight animation on the View, smoothly transitioning the background color.
 *
 * This function animates the background color of the View from [fromColor] to [toColor] and back,
 * creating a pulsing highlight effect. The animation repeats indefinitely until [totalDuration]
 * milliseconds have passed, after which it stops. Optionally, the view can be removed from its
 * parent after the animation finishes.
 *
 * @param totalDuration The total duration of the highlight animation in milliseconds. The animation
 *                      will pulse from `fromColor` to `toColor` and back, repeating until this
 *                      duration has elapsed. Defaults to 1500 milliseconds.
 * @param fromColor The starting color for the highlight animation (e.g., the view's current background color).
 * @param toColor The color to which the background will transition during the highlight.
 * @param removeAfter If true, the view will be removed from its parent ViewGroup after the
 *                    animation completes. Defaults to false.
 * @param onFinish An optional callback that will be invoked after the animation completes and
 *                 the view is potentially removed. Defaults to null.
 *
 * Example Usage:
 * ```kotlin
 * myView.startHighlight(
 *     totalDuration = 2000L,
 *     fromColor = Color.WHITE,
 *     toColor = Color.YELLOW,
 *     removeAfter = true,
 *     onFinish = {
 *         println("Highlight animation finished and view removed.")
 *     }
 * )
 * ```
 *
 * Note: If `removeAfter` is true, ensure that the view has a ViewGroup as its parent.
 *       Otherwise, an exception may be thrown.
 */
fun View.startHighlight(
    totalDuration: Long = 1500L,
    fromColor: Int,
    toColor: Int,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val animator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
        duration = totalDuration / 2
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { setBackgroundColor(it.animatedValue as Int) }
        start()
    }
    postDelayed({
        animator.cancel()
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a vertical sliding animation on the view.
 *
 * This function animates the view's vertical position (TRANSLATION_Y) by moving it up and down
 * repeatedly. After a specified total duration, the animation stops, the view's position is reset,
 * and optionally, the view can be removed from its parent.
 *
 * @param totalDuration The total duration of the animation in milliseconds. After this time, the
 *                      animation will stop and the view will be reset to its original position.
 *                      Defaults to 2500 milliseconds (2.5 seconds).
 * @param distance The distance in pixels the view should move vertically during each slide. A
 *                 positive value will move the view downwards, and negative upwards.
 *                 Defaults to 200 pixels upwards.
 * @param slideDuration The duration of a single up/down slide in milliseconds. Defaults to 500
 *                      milliseconds (0.5 seconds).
 * @param removeAfter If true, the view will be removed from its parent after the total duration
 *                    has elapsed. Defaults to false.
 * @param onFinish An optional lambda function to be executed after the total duration has elapsed
 *                 and the view has been reset. Defaults to null.
 *
 * Example Usage:
 * ```
 * // Slide a view up and down for 3 seconds, moving 150 pixels each time, with each slide taking 700ms.
 * // After 3 seconds remove the view and print a log
 * myView.startSlideVert(
 *     totalDuration = 3000L,
 *     distance = 150f,
 *     slideDuration = 700L,
 *     removeAfter = true,
 *     onFinish = { Log.d("SlideAnimation", "Animation finished and view removed!") }
 * )
 *
 * // Slide view up and down for default time with default distance.
 * myView.startSlideVert()
 * ```
 */
fun View.startSlideVert(
    totalDuration: Long = 2500L,
    distance: Float = 200f,
    slideDuration: Long = 500L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val up = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0f, -distance).apply {
        duration = slideDuration; repeatMode = ValueAnimator.REVERSE; repeatCount = ValueAnimator.INFINITE
    }
    AnimatorSet().apply { play(up); start() }
    postDelayed({
        translationY = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "glow" effect on the View by animating its elevation.
 *
 * This function creates a repeating animation that increases and decreases the
 * View's elevation, creating a pulsing or "glow" effect.  After a specified
 * total duration, the animation is stopped, the elevation is reset to zero,
 * and the View can optionally be removed from its parent.
 *
 * @param totalDuration The total duration of the entire glow effect, in milliseconds.
 *                      After this duration, the animation will stop. Defaults to 2000ms (2 seconds).
 * @param minElevation The minimum elevation of the View during the animation. Defaults to 0f.
 * @param maxElevation The maximum elevation of the View during the animation. This controls
 *                     the intensity of the glow. Defaults to 20f.
 * @param glowDuration The duration of one full cycle of the glow (from min to max and back
 *                     to min), in milliseconds. Defaults to 600ms.
 * @param removeAfter If true, the View will be removed from its parent after the total
 *                    duration has elapsed. Defaults to false.
 * @param onFinish An optional callback function that will be invoked when the glow effect
 *                 has finished (after the total duration). Defaults to null.
 *
 * Example usage:
 * ```
 * myView.startGlow(totalDuration = 3000L, maxElevation = 30f, glowDuration = 800L, removeAfter = true) {
 *     println("Glow finished and view removed!")
 * }
 * ```
 */
fun View.startGlow(
    totalDuration: Long = 2000L,
    minElevation: Float = 0f,
    maxElevation: Float = 20f,
    glowDuration: Long = 600L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val animator = ValueAnimator.ofFloat(minElevation, maxElevation).apply {
        duration = glowDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { elevation = it.animatedValue as Float }
        start()
    }

    postDelayed({
        animator.cancel()
        elevation = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}


/**
 * Starts a circular reveal animation on a View.
 *
 * This function creates and starts a circular reveal animation on the target [View].
 * The animation expands from a circular point specified by [centerX] and [centerY]
 * to a circle with [endRadius].
 *
 * @param centerX The x-coordinate of the center of the revealing circle, relative to the View.
 *                Defaults to the horizontal center of the View.
 * @param centerY The y-coordinate of the center of the revealing circle, relative to the View.
 *                Defaults to the vertical center of the View.
 * @param startRadius The starting radius of the revealing circle. Defaults to 0.
 * @param endRadius The ending radius of the revealing circle.
 *                  Defaults to the distance from the center to the furthest corner of the View,
 *                  ensuring the entire View is revealed.
 * @param revealDuration The duration of the reveal animation in milliseconds. Defaults to 500ms.
 * @param removeAfter If true, the View will be removed from its parent after the animation finishes.
 *                    Defaults to false.
 * @param onFinish A callback function that will be invoked when the animation finishes.
 *                 Defaults to null.
 *
 * @throws IllegalStateException if the view's parent is not a ViewGroup and removeAfter is true.
 *
 * @receiver The [View] on which to perform the circular reveal animation.
 *
 * @since API 21
 */
fun View.startCircularReveal(
    centerX: Int = width / 2,
    centerY: Int = height / 2,
    startRadius: Float = 0f,
    endRadius: Float = hypot(width.toFloat(), height.toFloat()),
    revealDuration: Long = 500L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    // only works on API21+
    val anim = ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, endRadius)
        .setDuration(revealDuration)
    this.visibility = View.VISIBLE
    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator) {
            if (removeAfter) (parent as? ViewGroup)?.removeView(this@startCircularReveal)
            onFinish?.invoke()
        }
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    anim.start()
}

/**
 * Starts a tilt sway animation on the View.
 *
 * This function creates an animation that simulates a swaying or tilting effect by rotating the view
 * around its X and Y axes. The animation is achieved using `ObjectAnimator` to manipulate the
 * `ROTATION_X` and `ROTATION_Y` properties of the View.
 *
 * @param totalDuration The total duration of the animation in milliseconds. After this duration,
 *                      the animation will be stopped, the view's rotation reset and removed from
 *                      parent if [removeAfter] is true. Default is 2000L (2 seconds).
 * @param tiltAngle     The maximum angle (in degrees) to tilt the view. A positive value for X rotation
 *                      tilts it backward and a negative value for Y rotation til its left.
 *                      Default is 10f (10 degrees).
 * @param tiltDuration  The duration of one cycle of the tilt (i.e., from 0 to tiltAngle and back to 0).
 *                      Default is 300L (300 milliseconds).
 * @param removeAfter   If true, the view will be removed from its parent after the animation finishes.
 *                      Default is false.
 * @param onFinish      An optional callback function that will be invoked when the animation finishes
 *                      (after the totalDuration).
 */
fun View.startTiltSway(
    totalDuration: Long = 2000L,
    tiltAngle: Float = 10f,
    tiltDuration: Long = 300L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val tiltX = ObjectAnimator.ofFloat(this, View.ROTATION_X, 0f, tiltAngle, 0f).apply {
        duration = tiltDuration
        repeatCount = ValueAnimator.INFINITE
    }
    val tiltY = ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0f, -tiltAngle, 0f).apply {
        duration = tiltDuration
        repeatCount = ValueAnimator.INFINITE
    }

    AnimatorSet().apply {
        playTogether(tiltX, tiltY)
        start()
    }

    postDelayed({
        tiltX.cancel(); tiltY.cancel()
        rotationX = 0f; rotationY = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a swinging animation on the View.
 *
 * This function creates a back-and-forth swinging motion around the top center edge of the view.
 *
 * @param totalDuration The total duration of the swing animation in milliseconds. After this duration,
 *                      the animation will stop and the view will return to its original state.
 *                      Defaults to 2500L (2.5 seconds).
 * @param angle The maximum angle (in degrees) that the view will swing to either side of its
 *              original position. A positive value represents a clockwise swing from 0, while a negative value
 *              represents a counter-clockwise swing from 0. Defaults to 30f (30 degrees).
 * @param swingDuration The duration of a single swing (from the center to one extreme and back) in
 *                      milliseconds. This determines how fast the view swings. Defaults to 400L (0.4 seconds).
 * @param removeAfter If true, the view will be removed from its parent ViewGroup after the animation
 *                    completes. Defaults to false.
 * @param onFinish An optional callback function that will be invoked after the animation finishes
 *                 (after the `totalDuration` has elapsed). Defaults to null.
 *
 * Example Usage:
 * ```
 * myView.startSwing(totalDuration = 5000L, angle = 45f, swingDuration = 500L, removeAfter = true) {
 *     println("Swing animation finished!")
 * }
 * ```
 */
fun View.startSwing(
    totalDuration: Long = 2500L,
    angle: Float = 30f,
    swingDuration: Long = 400L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    pivotX = width / 2f
    pivotY = 0f // swing around top edge

    val left = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, -angle).apply {
        duration = swingDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }
    val right = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, angle).apply {
        duration = swingDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
    }

    AnimatorSet().apply {
        playSequentially(left, right)
        start()
    }

    postDelayed({
        left.cancel(); right.cancel()
        rotation = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a marquee-like animation on a View, making it scroll horizontally back and forth.
 *
 * This function animates the `TRANSLATION_X` property of the given View, creating a horizontal
 * scrolling effect. The animation loops indefinitely until the specified `totalDuration` has
 * elapsed, after which it stops and resets the View's position. Optionally, it can remove
 * the view from its parent after the animation finishes.
 *
 * @param totalDuration The total duration of the marquee animation in milliseconds.
 *                      Defaults to 5000L (5 seconds).
 * @param distance The distance in pixels that the View should scroll horizontally in one direction.
 *                 A positive value moves the View from left to right.
 *                 Defaults to 500f (500 pixels). You can set this to the width of the View or any desired value.
 * @param scrollDuration The duration of a single scroll cycle (one direction) in milliseconds.
 *                       Defaults to 2000L (2 seconds).
 * @param removeAfter If true, the View will be removed from its parent ViewGroup after the animation finishes.
 *                    Defaults to false.
 * @param onFinish An optional callback function that will be invoked after the animation finishes and
 *                 the View's position has been reset. Defaults to null.
 *
 * Example usage:
 * ```
 *   myTextView.startMarquee(
 *       totalDuration = 10000L,
 *       distance = myTextView.width.toFloat(), // marquee distance equals to the textview width
 *       scrollDuration = 3000L,
 *       removeAfter = false,
 *       onFinish = {
 *           Log.d("Marquee", "Animation finished!")
 *       }
 *   )
 * ```
 */
fun View.startMarquee(
    totalDuration: Long = 5000L,
    distance: Float = 500f,  // you can set this to width or any px
    scrollDuration: Long = 2000L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, -distance, distance).apply {
        duration = scrollDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        start()
    }

    postDelayed({
        animator.cancel()
        translationX = 0f
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Animates a view with a "drop and bounce" effect.
 *
 * This function creates an animation sequence that first drops the view from above
 * and then bounces it slightly upwards before settling back to its original position.
 *
 * @param dropDistance The distance (in pixels) that the view should drop from above.
 *                     A positive value moves the view downwards and negative value move it upwards.
 *                     Defaults to 1000f.
 * @param dropDuration The duration (in milliseconds) of the drop animation. Defaults to 500L.
 * @param bounceDuration The duration (in milliseconds) of the bounce animation. Defaults to 300L.
 * @param removeAfter If true, the view will be removed from its parent after the animation completes.
 *                    Defaults to false.
 * @param onFinish A lambda function to be executed when the animation finishes.
 *                 Defaults to null.
 */
fun View.startDropBounce(
    dropDistance: Float = 1000f,
    dropDuration: Long = 500L,
    bounceDuration: Long = 300L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    // drop down
    val drop = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, -dropDistance, 0f).apply {
        duration = dropDuration
        interpolator = AccelerateInterpolator()
    }
    // bounce up slightly
    val bounce = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0f, -50f, 0f).apply {
        duration = bounceDuration
        interpolator = OvershootInterpolator()
    }

    AnimatorSet().apply {
        playSequentially(drop, bounce)
        start()
        // this is the KTX helper:
        doOnEnd {
            if (removeAfter) (parent as? ViewGroup)?.removeView(this@startDropBounce)
            onFinish?.invoke()
        }
    }
}

/**
 * Starts a color pulse animation on the View.
 *
 * This function animates the background color of the View, transitioning from [fromColor] to
 * [toColor] and back repeatedly, creating a "pulse" effect. The animation runs for a specified
 * total duration, with each individual pulse taking a shorter duration.
 *
 * @param fromColor The initial color of the pulse.
 * @param toColor The target color of the pulse.
 * @param totalDuration The total duration of the animation in milliseconds. Defaults to 2000ms.
 * @param pulseDuration The duration of each individual color transition (from [fromColor] to
 * [toColor] or back) in milliseconds. Defaults to 500ms.
 * @param removeAfter Whether to remove the View from its parent after the animation finishes.
 * Defaults to false.
 * @param onFinish An optional callback function that will be executed after the animation
 * finishes. Defaults to null.
 *
 * Example Usage:
 * ```
 * // Animate a button's background from red to blue for 3 seconds, with each pulse taking 1 second.
 * // The button will not be removed after the animation finishes.
 * button.startColorPulse(Color.RED, Color.BLUE, 3000L, 1000L)
 *
 * // Animate a textview's background from green to yellow for 5 seconds, with each pulse taking 500ms.
 * // The textview will be removed after the animation finishes and a message will be logged.
 * textView.startColorPulse(Color.GREEN, Color.YELLOW, 5000L, 500L, true) {
 *     Log.d("ColorPulse", "Animation finished and view removed.")
 * }
 * ```
 */
fun View.startColorPulse(
    fromColor: Int,
    toColor: Int,
    totalDuration: Long = 2000L,
    pulseDuration: Long = 500L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    val animator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
        duration = pulseDuration
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { setBackgroundColor(it.animatedValue as Int) }
        start()
    }

    postDelayed({
        animator.cancel()
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}

/**
 * Starts a "hinge" animation on the View, simulating a falling motion.
 *
 * This function animates the view's rotation around its top-left corner,
 * creating a visual effect similar to a hinge being opened and then the view falling.
 *
 * @param totalDuration The total duration of the animation, including the hinge and the holding time (in milliseconds).
 * @param fallAngle The angle (in degrees) to which the view will rotate during the hinge motion. Defaults to 80f.
 * @param hingeDuration The duration of the hinge/falling motion itself (in milliseconds). Defaults to 600L.
 * @param removeAfter If true, the view will be removed from its parent ViewGroup after the animation finishes. Defaults to false.
 * @param onFinish An optional callback function to be executed after the animation finishes.
 */
fun View.startHinge(
    totalDuration: Long = 2000L,
    fallAngle: Float = 80f,
    hingeDuration: Long = 600L,
    removeAfter: Boolean = false,
    onFinish: (() -> Unit)? = null
) {
    pivotX = 0f  // hinge at top-left
    pivotY = 0f

    val hinge = ObjectAnimator.ofFloat(this, View.ROTATION, 0f, fallAngle, fallAngle).apply {
        duration = hingeDuration
        interpolator = AccelerateInterpolator()
    }

    hinge.start()
    postDelayed({
        hinge.cancel()
        if (removeAfter) (parent as? ViewGroup)?.removeView(this)
        onFinish?.invoke()
    }, totalDuration)
}
