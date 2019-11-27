package com.karakum.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject


/**
 * BaseView model which takes a interface of `State`. This allows each screen's view model to have
 * certain states that only apply to that screen
 **/

//todo- Figure out a loadingSubject and an alertSubject, for now there are no error msgs nor loading animations
abstract class BaseViewModel<S : Mvvm.State> : ViewModel() {

    val stateSubject: PublishSubject<S> = PublishSubject.create()

    val loadingSubject: PublishSubject<S> = PublishSubject.create()

    private var compositeDisposable: CompositeDisposable? = null

    protected fun addToDisposables(disposable: Disposable) {
        (compositeDisposable ?: CompositeDisposable()).apply {
            compositeDisposable = this
            add(disposable)
        }
    }

    protected fun disposeDisposables() {
        compositeDisposable?.let {
            it.dispose()
            //Dispose of current disposables and set the Container to NULL
            compositeDisposable = null
        }
    }

    //DISPOSE OF DISPOSABLES
    override fun onCleared() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}