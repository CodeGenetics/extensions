package com.codegenetics.extensions.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.MainThread
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Extension function to set the size of a [BottomSheetDialog] to match the screen's width and height.
 * The width is adjusted based on the percentage provided.
 * - The background is set to transparent for a seamless look.
 */
@Deprecated("")
fun BottomSheetDialog.setSize() {
    val window = this.window
    // Set a transparent background for the dialog
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    // Set the width to match the screen width (100% by default)
    val width = this.context.getScreenWidth(100.0)
    val height = MATCH_PARENT // Set the height to match the parent
    window?.setLayout(width, height)
}

/**
 * Extension function to set the size of a [BottomSheetDialog] to match the screen's width and height.
 * The width is adjusted based on the percentage provided.
 * - The background is set to transparent for a seamless look.
 */

fun BottomSheetDialog.setSize(widthPercent: Double, heightPercent: Double) {
    val window = this.window
    // Set a transparent background for the dialog
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    // Set the width to match the screen width (100% by default)
    val w = this.context.getScreenWidth(widthPercent)
    val h = this.context.getScreenHeight(heightPercent)
    window?.setLayout(w, h)
}