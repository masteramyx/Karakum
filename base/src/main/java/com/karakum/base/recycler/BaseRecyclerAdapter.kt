package com.karakum.base.recycler

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 *  Base class for [RecyclerView.Adapter], holds the list of items and provides base methods for adding/removing. More importantly, here resides the [DiffUtil.Callback]
 *  which holds special implementation [BaseRecyclerDiffListener], for use with [BaseRecyclerItem].
 *
 *  This class and [BaseRecyclerAdapterImpl] are intended for use with items extending [BaseRecyclerItem], making use of properties to uniquely identify the recycler items
 */
abstract class BaseRecyclerAdapter<T : BaseRecyclerItem, VH : RecyclerView.ViewHolder>(context: Context) :
    RecyclerView.Adapter<VH>() {

    var list: MutableList<T>? = mutableListOf()
    var inflater: LayoutInflater = LayoutInflater.from(context)
    abstract var diffListener: BaseRecyclerDiffListener<T>


    fun getItem(position: Int): T? {
        return list?.get(position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun removeItem(position: Int, notify: Boolean = false): Boolean {
        // returns `true` if item was found in list, false if not
        val removed = list?.remove(list?.getOrNull(position))
        if (notify) {
            notifyItemRemoved(position)
        }
        return removed ?: false
    }

    fun clear(notify: Boolean) {
        if (list != null) {
            val count = list?.size
            list?.clear()
            if (notify) {
                notifyItemRangeRemoved(0, count ?: 0)
            }
        }
    }

    fun addItem(item: T, notify: Boolean) {
        if (list == null) {
            return
        }
        list?.add(item)
        if (notify) {
            notifyItemInserted(list?.size ?: 0)
        }
    }

    fun addItems(newItems: List<T>?) {
        val oldItems = list
        if (newItems == null || newItems.isEmpty()) {
            clear(true)
        } else if (oldItems == null || oldItems.isEmpty()) {
            list = newItems.toMutableList()
        } else {
            //there are items present in new list and old, we need to do the diff
            list = newItems.toMutableList()
            val callback = BaseRecyclerDiffUtil(oldItems, newItems)
            callback.diffListener = diffListener
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }
    }


}