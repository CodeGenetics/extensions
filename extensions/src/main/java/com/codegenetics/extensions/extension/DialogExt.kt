package com.codegenetics.extensions.extension

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

fun Dialog.setSize(context: Context, widthPercent: Double?, heightPercent: Double? = 0.0) {
    if (widthPercent != null && heightPercent != null) {
        val width = context.getScreenWidth(widthPercent)
        val height = context.getScreenHeight(heightPercent)
        if (heightPercent > 0) {
            this.window?.setLayout(width, height)
        } else {
            this.window?.setLayout(width, WRAP_CONTENT)
        }
    }

}