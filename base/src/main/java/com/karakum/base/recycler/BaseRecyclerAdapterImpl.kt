package com.karakum.base.recycler

import android.content.Context
import android.view.ViewGroup

open class BaseRecyclerAdapterImpl<T : BaseRecyclerItem>(context: Context) :
    BaseRecyclerAdapter<T, BaseRecyclerViewHolder<T>>(context) {

    override var diffListener: BaseRecyclerDiffListener<T> = BaseRecyclerDiffListener()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<T> {
        return list?.first { item ->
            item.getViewType() == viewType
        }?.createViewHolder(inflater, parent, false) as BaseRecyclerViewHolder<T>
    }


    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        val item = list!![position]
        onBindViewHolderListeners(item, holder)
        holder.bindView(item)
    }

    /**
     * Add additional listeners to Viewholders, called before [BaseRecyclerViewHolder.bindView]
     */
    open fun onBindViewHolderListeners(item: T, holder: BaseRecyclerViewHolder<*>) {
        //
    }

    override fun onViewDetachedFromWindow(holder: BaseRecyclerViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.unbindView()
    }

    override fun getItemViewType(position: Int): Int {
        return list!![position].getViewType()
    }
}