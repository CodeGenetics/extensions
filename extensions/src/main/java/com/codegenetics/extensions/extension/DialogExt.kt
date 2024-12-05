package com.codegenetics.extensions.extension

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

/**
 * Sets the size of the [Dialog] as a percentage of the screen's width and height.
 *
 * @param context The context used to calculate screen dimensions.
 * @param widthPercent The desired width of the dialog as a percentage of the screen width (0.0 to 100.0).
 * @param heightPercent The desired height of the dialog as a percentage of the screen height (0.0 to 100.0).
 *
 * ### Usage Example:
 * ```kotlin
 * dialog.setSize(context, widthPercent = 80.0, heightPercent = 50.0)
 * ```
 */
fun Dialog.setSize(
    context: Context,
    widthPercent: Double = 100.0,
    heightPercent: Double = 0.0
) {
    require(widthPercent in 0.0..100.0) { "Width percent must be between 0.0 and 100.0" }
    require(heightPercent in 0.0..100.0) { "Height percent must be between 0.0 and 100.0" }

    val width = context.getScreenWidth(widthPercent)
    val height = if (heightPercent > 0.0) {
        context.getScreenHeight(heightPercent)
    } else {
        WRAP_CONTENT
    }

    this.window?.setLayout(width, height)
}