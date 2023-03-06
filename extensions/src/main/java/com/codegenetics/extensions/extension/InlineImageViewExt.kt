package com.codegenetics.extensions.extension

import androidx.core.graphics.drawable.toBitmap
import com.codegenetics.extensions.extension.customviews.InlineImageTextView

fun InlineImageTextView.setImage(imageUrl: String) {

    try {
        val width = this.textSize.toInt()
        val height = this.textSize.toInt()
        this.context.getDrawable(imageUrl, width, height) {
            this.setInlineImage(
                bitmap = it.toBitmap(), atIndex = text.length - 1, text = "$text"
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun InlineImageTextView.setImage(
    imageUrl: String, width: Int = resources.getDimensionPixelOffset(com.intuit.sdp.R.dimen._20sdp),
    height: Int = resources.getDimensionPixelOffset(com.intuit.sdp.R.dimen._20sdp),
) {

    try {
        this.context.getDrawable(imageUrl, width, height) {
            this.setInlineImage(
                bitmap = it.toBitmap(), atIndex = text.length - 1, text = "$text"
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
