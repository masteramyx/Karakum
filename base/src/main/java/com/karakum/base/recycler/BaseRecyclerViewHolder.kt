package com.karakum.base.recycler

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.extensions.LayoutContainer

abstract class BaseRecyclerViewHolder<T : BaseRecyclerItem>(view: View) :
    RecyclerView.ViewHolder(view),
    LayoutContainer {

    override val containerView: View?
        get() = itemView


    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val context = itemView.context

    val resources = itemView.resources

    /**
     * Set after the adapter calls [BaseRecyclerAdapter.bindViewHolder], item is set and then it's
     * properties set to the view via [bindItem]
     */
    protected lateinit var item: T
        private set


    /**
     * Sets the item on the view
     */
    abstract fun bindItem(item: T)

    @CallSuper
    @Suppress("UNCHECKED_CAST")
    fun bindView(item: BaseRecyclerItem) {
        disposeDisposables()
        this.item = item as T
        bindItem(item)
    }

    @CallSuper
    open fun unbindView() {
        disposeDisposables()
    }

    protected fun addToDisposables(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


    protected fun disposeDisposables() {
        compositeDisposable.clear()
    }

}