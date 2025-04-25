package com.codegenetics.extensions.extension

import com.codegenetics.extensions.utils.Resource
import com.codegenetics.extensions.utils.Resource.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Emits values from the source [Flow] only when they are different from the previously emitted value.
 * It uses the identity of the elements for comparison.
 * This function is a shortcut for `ifChanged { it }` and is equivalent to `distinctUntilChanged()`.
 *
 * @param T The type of elements in the [Flow].
 * @return A [Flow] that emits only the changed values.
 *
 * @see distinctUntilChanged
 *
 * Example:
 * ```kotlin
 * val flow = flowOf(1, 1, 2, 2, 3, 1, 1)
 * flow.ifChanged().collect { println(it) } // Output: 1, 2, 3, 1
 * ```
 */
fun <T> Flow<T>.ifChanged() = ifChanged { it }

fun <T, R> Flow<T>.ifChanged(transform: (T) -> R): Flow<T> {
    var observedValueOnce = false
    var lastMappedValue: R? = null

    return filter { value ->
        val mapped = transform(value)
        if (!observedValueOnce || mapped != lastMappedValue) {
            lastMappedValue = mapped
            observedValueOnce = true
            true
        } else {
            false
        }
    }
}

fun <T> Flow<T>.asResult(): Flow<Resource<T>> {
    return map<T, Resource<T>>(::Success).catch {
        emit(Resource.Error(message = it.message ?: "Something went wrong"))
    }
}