package com.codegenetics.extensions.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<Model : Any, Binding : ViewBinding>(
    private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> Binding,
    callback: DiffUtil.ItemCallback<Model> = BaseDiffUtils(),
) : ListAdapter<Model, BaseViewHolderV2<Binding>>(callback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2<Binding> {
        return BaseViewHolderV2(
            bindingFactory.invoke(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolderV2<Binding>, position: Int) {
        with(holder) {
            binding.root.tag = position
            binding.bindViews(getItem(absoluteAdapterPosition))
            binding.bindListeners(absoluteAdapterPosition)
        }
    }

    protected abstract fun Binding.bindViews(model: Model)
    open fun Binding.bindListeners(position: Int) {}

}

/**
 * @sample
 * class MoodsAdapter  : BaseAdapter<DiaryMoodsModel, ItemMoodBinding>(ItemMoodBinding::inflate) {
 *
 *      [//typealias of Callback (Int)->Unit]
 *     private var intCallback: IntCallback? = null
 *
 *     fun onItemClickListener(intCallback: IntCallback) { this.intCallback = intCallback }
 *      [to set TextViews etc]
 *     override fun ItemMoodBinding.bindViews(model: DiaryMoodsModel) {
 *         ivMood.setImageDrawable(root.context.getDrawableImage(model.mood))
 *         root.setItemForeground(model.isSelected)
 *     }
 *
 *     [ To add All ClickListener]
 *     override fun ItemMoodBinding.bindListeners(position: Int) {
 *         root.setSmartClickListener {
 *             getItem(position).apply {
 *                 isSelected = true
 *                 notifyItemChanged(position)
 *             }
 *             intCallback?.invoke(position)
 *         }
 *     }
 *
 * }
 *
 * */

