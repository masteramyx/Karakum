package com.karakum.base.recycler

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup

interface BaseRecyclerItem : Parcelable {
    fun getViewType(): Int

    fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToRoot: Boolean
    ): BaseRecyclerViewHolder<*>

    fun getRecyclerId(): String
}