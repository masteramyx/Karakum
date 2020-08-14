package com.karakum.base.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * [BaseRecyclerDiffUtil.diffListener] override default diffs with one for [BaseRecyclerItem] which uses
 * [BaseRecyclerItem.getViewType] and [BaseRecyclerItem.getRecyclerId] to make diffing easier
 */
class BaseRecyclerDiffListener<T : BaseRecyclerItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getViewType() == newItem.getViewType() && oldItem.getRecyclerId() == newItem.getRecyclerId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}