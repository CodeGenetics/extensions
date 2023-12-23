package com.codegenetics.extensions.base

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtils<T: Any>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false

}