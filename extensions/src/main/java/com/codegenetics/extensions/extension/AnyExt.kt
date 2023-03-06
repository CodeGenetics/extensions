package com.codegenetics.extensions.extension

import java.math.RoundingMode
import java.text.DecimalFormat

fun Any.twoDecimal(): String {
    val df = DecimalFormat("0.00")
    return df.format(this.toString().toDouble())
}

fun Any.roundUpToTwoDecimalsWithoutRounding(): String? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this.toString().toDouble())
}

