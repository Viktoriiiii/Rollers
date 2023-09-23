package ru.spb.rollers.adapters

import androidx.recyclerview.widget.DiffUtil

class CommonCallbackImpl<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val areItemsTheSameImpl: (oldItem: T, newItem: T) -> Boolean = {
            oldItem, newItem -> oldItem == newItem
    },
    private val areContentsTheSameImpl: (oldItem: T, newItem: T) -> Boolean = {
            oldItem, newItem -> oldItem == newItem
    }
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSameImpl(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSameImpl(oldItems[oldItemPosition], newItems[newItemPosition])
    }
}
