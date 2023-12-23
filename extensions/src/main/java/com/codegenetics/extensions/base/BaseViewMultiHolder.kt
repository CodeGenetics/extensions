package com.codegenetics.extensions.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewMultiHolder<T>(binding: Any) : RecyclerView.ViewHolder(binding as View) {
        abstract fun bind(data: T)
    }