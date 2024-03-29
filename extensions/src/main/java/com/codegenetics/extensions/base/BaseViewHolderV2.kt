package com.codegenetics.extensions.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolderV2<out T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)