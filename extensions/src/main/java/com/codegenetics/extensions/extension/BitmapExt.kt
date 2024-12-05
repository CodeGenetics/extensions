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

/**
 * Calculates and returns the most dominant color in the [Bitmap].
 * @return The most dominant color as an integer.
 */
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

/**
 * Extracts a specific color type from the [Bitmap] using the [Palette] library.
 * @param color The type of color to extract (e.g., VIBRANT, MUTED, etc.).
 * @param callBack A callback function that receives the extracted color as an integer.
 */
fun Bitmap.extractColor(color: ColorPick, callBack: (Int) -> Unit) {
    Palette.Builder(this).generate { palette ->
        palette?.let {
            when (color) {
                ColorPick.VIBRANT -> callBack(it.getVibrantColor(0x000000))
                ColorPick.VIBRANT_LIGHT -> callBack(it.getLightVibrantColor(0x000000))
                ColorPick.VIBRANT_DARK -> callBack(it.getDarkVibrantColor(0x000000))
                ColorPick.MUTED -> callBack(it.getMutedColor(0x000000))
                ColorPick.MUTED_LIGHT -> callBack(it.getLightMutedColor(0x000000))
                ColorPick.MUTED_DARK -> callBack(it.getDarkMutedColor(0x000000))
                ColorPick.DOMINANT -> callBack(it.getDominantColor(0x000000))
            }
        }
    }
}

/**
 * Converts the [Bitmap] to a Base64-encoded string.
 * @return The Base64-encoded string representation of the [Bitmap].
 */
fun Bitmap.toBase64(): String {
    var result = ""
    val baos = ByteArrayOutputStream()
    try {
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        baos.flush()
        val bitmapBytes = baos.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            baos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result
}


/**
 * Resizes the [Bitmap] to the specified width and height.
 * @param w The target width.
 * @param h The target height.
 * @return A new [Bitmap] resized to the specified dimensions, or the original [Bitmap] if width or height is zero.
 */
fun Bitmap.resize(w: Number, h: Number): Bitmap {
    val width = this.width
    val height = this.height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return if (width > 0 && height > 0) {
        Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    } else {
        this
    }
}

/**
 * Saves the [Bitmap] to a file at the specified path.
 * @param path The file path where the [Bitmap] should be saved.
 * @throws IOException If an error occurs while saving the file.
 */
fun Bitmap.saveFile(path: String) {
    val file = File(path)
    if (!file.exists()) {
        file.createNewFile()
    }
    FileOutputStream(file).use { stream ->
        compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
    }
}
