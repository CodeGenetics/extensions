package com.codegenetics.extensions.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Retrieves the position of the first completely visible item in a [RecyclerView].
 *
 * @return The position of the first completely visible item, or -1 if no item is completely visible or the layout manager is not a [LinearLayoutManager].
 *
 * ### Usage Example:
 * ```kotlin
 * val recyclerView: RecyclerView = ...
 * val currentPosition = recyclerView.getCurrentPosition()
 * println("Current Position: $currentPosition") // Output: 0, 1, or -1 if invalid
 * ```
 */
fun RecyclerView?.getCurrentPosition(): Int {
    val layoutManager = this?.layoutManager
    return if (layoutManager is LinearLayoutManager) {
        layoutManager.findFirstCompletelyVisibleItemPosition()
    } else {
        -1 // Return -1 if not a LinearLayoutManager or RecyclerView is null
    }
}

/**
 * Retrieves the position of the last completely visible item in a [RecyclerView].
 *
 * @return The position of the last completely visible item, or -1 if the layout manager is not a [LinearLayoutManager].
 *
 * ### Usage Example:
 * ```kotlin
 * val recyclerView: RecyclerView = ...
 * val lastVisiblePosition = recyclerView.getLastVisiblePosition()
 * println("Last Visible Position: $lastVisiblePosition")
 * ```
 */
fun RecyclerView?.getLastVisiblePosition(): Int {
    val layoutManager = this?.layoutManager
    return if (layoutManager is LinearLayoutManager) {
        layoutManager.findLastCompletelyVisibleItemPosition()
    } else {
        -1
    }
}

/**
 * Retrieves the total number of items in the [RecyclerView].
 *
 * @return The total item count, or 0 if the adapter is null.
 *
 * ### Usage Example:
 * ```kotlin
 * val recyclerView: RecyclerView = ...
 * val itemCount = recyclerView.getTotalItemCount()
 * println("Total Item Count: $itemCount")
 * ```
 */
fun RecyclerView?.getTotalItemCount(): Int {
    return this?.adapter?.itemCount ?: 0
}

/**
 * Smoothly scrolls to the specified position in the [RecyclerView].
 *
 * @param position The target position to scroll to.
 *
 * ### Usage Example:
 * ```kotlin
 * val recyclerView: RecyclerView = ...
 * recyclerView.smoothScrollToPosition(5)
 * ```
 */
fun RecyclerView?.smoothScrollToPosition(position: Int) {
    this?.layoutManager?.let {
        if (it is LinearLayoutManager) {
            it.smoothScrollToPosition(this, RecyclerView.State(), position)
        }
    }
}

/**
 * Checks if the [RecyclerView] is scrolled to the bottom.
 *
 * @return `true` if scrolled to the bottom, `false` otherwise.
 *
 * ### Usage Example:
 * ```kotlin
 * val recyclerView: RecyclerView = ...
 * val isAtBottom = recyclerView.isScrolledToBottom()
 * println("Is RecyclerView at bottom: $isAtBottom")
 * ```
 */
fun RecyclerView?.isScrolledToBottom(): Boolean {
    val layoutManager = this?.layoutManager
    return if (layoutManager is LinearLayoutManager) {
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        lastVisibleItemPosition == totalItemCount - 1
    } else {
        false
    }
}