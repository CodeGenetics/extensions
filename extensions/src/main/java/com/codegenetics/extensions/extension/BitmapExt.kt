package com.codegenetics.extensions.extension

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.util.Base64
import androidx.palette.graphics.Palette
import com.codegenetics.extensions.helper.ColorPick
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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

/**
 * Extension method to get base64 string for Bitmap.
 */
fun Bitmap.toBase64(): String {
    var result = ""
    val baos = ByteArrayOutputStream()
    try {
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        baos.flush()
        baos.close()
        val bitmapBytes = baos.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            baos.flush()
            baos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result
}

/**
 * Extension method to resize Bitmap to specified height and width.
 */
fun Bitmap.resize(w: Number, h: Number): Bitmap {
    val width = width
    val height = height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    return this
}

/**
 * Extension method to save Bitmap to specified file path.
 */
fun Bitmap.saveFile(path: String) {
    val f = File(path)
    if (!f.exists()) {
        f.createNewFile()
    }
    val stream = FileOutputStream(f)
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    stream.flush()
    stream.close()
}
