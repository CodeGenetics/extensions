package com.codegenetics.extensions.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

/**
 * Transforms the emitted values of a [LiveData].
 *
 * @param transform A lambda to transform the emitted value.
 * @return A new [LiveData] instance with transformed values.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData: LiveData<Int> = MutableLiveData(5)
 * val squaredLiveData = liveData.map { it * it }
 * squaredLiveData.observe(viewLifecycleOwner) { value ->
 *     println("Transformed value: $value") // Output: 25
 * }
 * ```
 */
fun <X, Y> LiveData<X>.map(transform: (X) -> Y): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(this) { value ->
        result.value = transform(value)
    }
    return result
}

/**
 * Switches the source [LiveData] based on the latest emitted value.
 *
 * @param transform A lambda to create a new [LiveData] source based on the emitted value.
 * @return A new [LiveData] instance that switches sources dynamically.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData: LiveData<Int> = MutableLiveData(5)
 * val switchedLiveData = liveData.switchMap { MutableLiveData(it * 2) }
 * switchedLiveData.observe(viewLifecycleOwner) { value ->
 *     println("Switched value: $value") // Output: 10
 * }
 * ```
 */
fun <X, Y> LiveData<X>.switchMap(transform: (X) -> LiveData<Y>): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(this) { value ->
        result.addSource(transform(value)) { transformedValue ->
            result.value = transformedValue
        }
    }
    return result
}

/**
 * A wrapper for data exposed via a [LiveData] that represents an event.
 * This prevents the data from being consumed multiple times.
 *
 * @param T The type of the content.
 */
class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    /**
     * Returns the content if it hasn't been handled, null otherwise.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it has already been handled.
     */
    fun peekContent(): T = content
}

/**
 * Wraps a value in an [Event].
 *
 * @return The [Event] wrapping the value.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData: MutableLiveData<Event<String>> = MutableLiveData()
 * liveData.value = "Hello, World!".toEvent()
 *
 * liveData.observe(viewLifecycleOwner) { event ->
 *     event.getContentIfNotHandled()?.let { value ->
 *         println("Event value: $value") // Output: "Hello, World!"
 *     }
 * }
 * ```
 */
fun <T> T.toEvent(): Event<T> {
    return Event(this)
}

/**
 * Combines two [LiveData] sources and emits a [Pair] of their values.
 *
 * @param source1 The first [LiveData] source.
 * @param source2 The second [LiveData] source.
 * @return A [LiveData] emitting [Pair] of values from the two sources.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData1: LiveData<Int> = MutableLiveData(3)
 * val liveData2: LiveData<String> = MutableLiveData("Hello")
 *
 * val combinedLiveData = combineLatest(liveData1, liveData2)
 * combinedLiveData.observe(viewLifecycleOwner) { (intValue, stringValue) ->
 *     println("$intValue, $stringValue") // Output: "3, Hello"
 * }
 * ```
 */
fun <A, B> combineLatest(source1: LiveData<A>, source2: LiveData<B>): LiveData<Pair<A, B>> {
    val result = MediatorLiveData<Pair<A, B>>()
    result.addSource(source1) { value1 ->
        val value2 = source2.value
        if (value2 != null) {
            result.value = Pair(value1, value2)
        }
    }
    result.addSource(source2) { value2 ->
        val value1 = source1.value
        if (value1 != null) {
            result.value = Pair(value1, value2)
        }
    }
    return result
}

/**
 * Filters the emitted values of a [LiveData] based on a predicate.
 *
 * @param predicate A lambda that returns true if the value should be emitted.
 * @return A [LiveData] emitting only values that satisfy the predicate.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData: LiveData<Int> = MutableLiveData(10)
 * val filteredLiveData = liveData.filter { it > 5 }
 * filteredLiveData.observe(viewLifecycleOwner) { value ->
 *     println("Filtered value: $value") // Output: 10
 * }
 * ```
 */
fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value)) {
            result.value = value
        }
    }
    return result
}

/**
 * Forces a [LiveData] to re-emit its current value.
 *
 * ### Usage Example:
 * ```kotlin
 * val liveData: MutableLiveData<Int> = MutableLiveData(1)
 * liveData.observe(viewLifecycleOwner) { value ->
 *     println("Observed value: $value")
 * }
 * liveData.triggerUpdate() // Forces re-emission of the current value
 * ```
 */
fun <T> MutableLiveData<T>.triggerUpdate() {
    this.value = this.value
}