package com.codegenetics.extensions.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

fun Long.getReadableSize(): String {
    try {
        val symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.US)

        if (this <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat(
            "#,##0.#", symbols
        ).format(this / 1024.0.pow(digitGroups.toDouble())).toString() + " " + units[digitGroups]

    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return ""

}