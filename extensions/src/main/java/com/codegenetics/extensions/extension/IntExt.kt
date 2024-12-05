package com.codegenetics.extensions.extension

/**
 * Ensures a value is within a given range.
 *
 * @param min The minimum value.
 * @param max The maximum value.
 * @return The coerced value within the range.
 */
fun Int.coerceBetween(min: Int, max: Int): Int {
    return coerceAtLeast(min).coerceAtMost(max)
}