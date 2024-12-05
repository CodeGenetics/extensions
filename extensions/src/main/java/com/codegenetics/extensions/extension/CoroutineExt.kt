package com.codegenetics.extensions.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.launchInBackground(
    backgroundBlock: suspend () -> Unit,
    onCompletion: (() -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null
) {
    this.launch {
        try {
            withContext(Dispatchers.IO) {
                backgroundBlock()
            }
            onCompletion?.invoke()
        } catch (e: Throwable) {
            withContext(Dispatchers.Main) {
                onError?.invoke(e)
            }
        }
    }
}

/**
 * Launches a background coroutine on [Dispatchers.IO], handling completion and errors gracefully.
 *
 * @param backgroundBlock The suspendable block of code to execute in the background.
 * @param onCompletion Optional lambda invoked on the [Dispatchers.Main] once the background work completes.
 * @param onError Optional lambda invoked on the [Dispatchers.Main] if an exception occurs during execution.
 *
 * ### Usage Example:
 * ```kotlin
 * scope.launchSafelyInBackground(
 *     backgroundBlock = { performLongRunningTask() },
 *     onCompletion = { updateUI() },
 *     onError = { handleError(it) }
 * )
 * ```
 */
fun CoroutineScope.launchSafelyInBackground(
    backgroundBlock: suspend () -> Unit,
    onCompletion: (() -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = null
) {
    this.launch {
        try {
            withContext(Dispatchers.IO) {
                backgroundBlock()
            }
            withContext(Dispatchers.Main) {
                onCompletion?.invoke()
            }
        } catch (e: Throwable) {
            withContext(Dispatchers.Main) {
                onError?.invoke(e)
            }
        }
    }
}