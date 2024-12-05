package com.codegenetics.extensions.extension

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

fun <T> List<T>.toArraylist() = this as ArrayList

/**
 * Maps elements and filters out null or empty results in one operation.
 *
 * @param transform The transformation function.
 * @return A filtered and mapped list.
 */
inline fun <T, R> Iterable<T>.mapNotNullOrEmpty(transform: (T) -> R?): List<R> {
    return this.mapNotNull(transform).filter { it.toString().isNotEmpty() }
}

/**
 * Safely retrieves an element by index.
 *
 * @param index The index of the element to retrieve.
 * @return The element at the given index or null if the index is out of bounds.
 */
fun <T> List<T>.getOrNull(index: Int): T? {
    return if (index in indices) this[index] else null
}

