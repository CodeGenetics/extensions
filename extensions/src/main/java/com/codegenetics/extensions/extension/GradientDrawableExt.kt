package com.codegenetics.extensions.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable

fun GradientDrawable.toBitmap(): Bitmap {
    val width = 100
    val height = 100
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, width, height)
    this.draw(canvas)
    return bitmap
}