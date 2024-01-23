package com.codegenetics.extensions.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/** Directly call launch from viewModel for Coroutine Default
 * attached with lifecycle*/
fun ViewModel.launchDefault(
    context: CoroutineContext = Dispatchers.Default,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return viewModelScope.launch(context = context, block = block)
}

/** Directly call launch from viewModel for Coroutine Main
 * attached with lifecycle*/
fun ViewModel.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launchDefault(context = Dispatchers.Main, block = block)
}

/** Directly call launch from viewModel for Coroutine IO
 * attached with lifecycle*/
fun ViewModel.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return launchDefault(context = Dispatchers.IO, block = block)
}
