package com.codegenetics.extensions.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView?.getCurrentPosition(): Int {
    return (this?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
}