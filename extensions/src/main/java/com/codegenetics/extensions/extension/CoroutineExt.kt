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