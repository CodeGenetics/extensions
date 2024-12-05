package com.codegenetics.extensions.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

/**
 * Converts a [GradientDrawable] to a [Bitmap].
 *
 * @param width The desired width of the bitmap. Default is 100 pixels.
 * @param height The desired height of the bitmap. Default is 100 pixels.
 * @return A [Bitmap] representation of the [GradientDrawable].
 *
 * ### Usage Example:
 * ```kotlin
 * val gradientDrawable = GradientDrawable(
 *     GradientDrawable.Orientation.LEFT_RIGHT,
 *     intArrayOf(Color.RED, Color.BLUE)
 * )
 * val bitmap = gradientDrawable.toBitmap(200, 200)
 * imageView.setImageBitmap(bitmap)
 * ```
 */
fun GradientDrawable.toBitmap(width: Int = 100, height: Int = 100): Bitmap {
    require(width > 0 && height > 0) { "Width and height must be greater than 0" }

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, width, height)
    this.draw(canvas)
    return bitmap
}