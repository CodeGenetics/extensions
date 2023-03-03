package com.codegenetics.extensions.extension

import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.codegenetics.extensions.helper.ColorPick

/** @return most dominant color from the bitmap @*/
fun Bitmap.getDominantColor(): Int {
    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)

    var r = 0
    var g = 0
    var b = 0
    for (pixel in pixels) {
        r += Color.red(pixel)
        g += Color.green(pixel)
        b += Color.blue(pixel)
    }
    val size = this.width * this.height
    return Color.rgb(r / size, g / size, b / size)
}

/** @param color[ColorPick]
 * @return color in Int */
fun Bitmap.extractColor(color: ColorPick, callBack: (Int) -> Unit) {
    Palette.Builder(this).generate {
        it?.let { palette ->
            when (color) {
                ColorPick.VIBRANT -> callBack(palette.getVibrantColor(0x000000))
                ColorPick.VIBRANT_LIGHT -> callBack(palette.getLightVibrantColor(0x000000))
                ColorPick.VIBRANT_DARK -> callBack(palette.getDarkVibrantColor(0x000000))
                ColorPick.MUTED -> callBack(palette.getVibrantColor(0x000000))
                ColorPick.MUTED_LIGHT -> callBack(palette.getLightMutedColor(0x000000))
                ColorPick.MUTED_DARK -> callBack(palette.getDarkMutedColor(0x000000))
                ColorPick.DOMINANT -> callBack(palette.getDominantColor(0x000000))
            }
        }
    }
}
