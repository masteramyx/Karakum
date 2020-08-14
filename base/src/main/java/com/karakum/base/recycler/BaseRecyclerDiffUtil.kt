package com.karakum.base.recycler

import androidx.recyclerview.widget.DiffUtil

/**
 * https://blog.mindorks.com/the-powerful-tool-diff-util-in-recyclerview-android-tutorial
 */
class BaseRecyclerDiffUtil<T : BaseRecyclerItem>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    var diffListener: DiffUtil.ItemCallback<T>? = null

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (diffListener == null) {
            oldList[oldItemPosition] == newList[newItemPosition]
        } else {
            diffListener!!.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (diffListener == null) {
            false
        } else {
            diffListener!!.areContentsTheSame(
                oldList[oldItemPosition],
                newList[newItemPosition]
            )
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Pair(oldList[oldItemPosition], newList[newItemPosition])
    }
}