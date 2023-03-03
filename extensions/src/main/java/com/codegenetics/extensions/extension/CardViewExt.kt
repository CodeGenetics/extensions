package com.codegenetics.extensions.extension

import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView

import androidx.core.content.ContextCompat

/** change color of card*/
fun CardView.backgroundColor(@ColorRes colorId: Int) {
    setCardBackgroundColor(ContextCompat.getColor(context, colorId))
}