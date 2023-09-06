package com.mellivora.base.binding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.drakeet.multitype.ItemViewBinder

/**
 * ItemViewBinder
 */
abstract class BindingItemViewBinder<T, B: ViewBinding> : ItemViewBinder<T, RecyclerHolder>() {

    abstract fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): B

    abstract fun onBindViewHolder(binding: B, data: T, holder: RecyclerHolder)

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerHolder {
        return RecyclerHolder(onCreateBinding(inflater, parent))
    }

    override fun onBindViewHolder(holder: RecyclerHolder, item: T) {
        onBindViewHolder(holder.getBinding(), item, holder)
    }

}
