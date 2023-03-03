package com.codegenetics.extensions.extension

import android.graphics.PorterDuff
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.tintViewDrawable(@ColorRes colorId: Int) {
    val drawables = compoundDrawables
    for (drawable in drawables) {
        drawable?.setColorFilter(
            ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP
        )
    }
}

fun TextView.textColor(@ColorRes colorId: Int) {
    setTextColor(ContextCompat.getColor(context, colorId))
}