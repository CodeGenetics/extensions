package com.codegenetics.extensions.extension

import java.text.DecimalFormat

fun Any.twoDecimal(): String {
    val df = DecimalFormat("0.00")
    return df.format(this.toString().toDouble())
}