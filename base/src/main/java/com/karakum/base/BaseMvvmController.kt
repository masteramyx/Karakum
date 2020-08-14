package com.karakum.base

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bluelinelabs.conductor.Controller
import com.karakum.IObservableSchedulerRx2
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base Mvvm Controller of types ViewModel and State. Handle StateChange via Rx
 */
abstract class BaseMvvmController<VM : BaseViewModel<S>, S : Mvvm.State> : Controller(),
    ViewModelStoreOwner {


    private var compositeDisposable: CompositeDisposable? = null

    open val context: Context get() = activity!!

    abstract val viewModel: VM


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

    // Container to hold the view models, android handles lifecycle stuff. As per description, if owner is destroyed
    // and recreated via configuration the new instance of owner will have same old instance of [ViewModelStore]
    // Instantiated lazily, not needed until controller is attached and viewmodel injected
    private val store by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelStore()
    }

    fun viewModelProvider(factory: ViewModelProvider.Factory = defaultFactory(this)) =
        ViewModelProvider(store, factory)


    override fun getViewModelStore(): ViewModelStore = store


    override fun onAttach(view: View) {
        super.onAttach(view)

        //set up the state subject listener
        addToDisposables(
            viewModel.stateSubject
                .distinctUntilChanged()
                .compose(scheduler.SUBSCRIBE_IO_OBSERVE_ANDROID_MAIN.schedule())
                .subscribeBy(
                    onSuccess = this@BaseMvvmController::onStateChange,
                    onError = this@BaseMvvmController::onStateError
                )
        )

    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        disposeDisposables()
    }


    // Release the View Model Store
    override fun onDestroy() {
        super.onDestroy()
        store.clear()
    }

    abstract fun onStateChange(state: Mvvm.State)

    open fun onStateError(throwable: Throwable) {
        Log.d("An Error Occurred", throwable.localizedMessage!!)
    }

    fun setViewVisibility(view: View?, show: Boolean) {
        if (view == null) {
            return
        }
        if (!show) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }


    companion object {
        private var defaultFactory: ViewModelProvider.Factory? = null

        private fun defaultFactory(controller: Controller): ViewModelProvider.Factory {
            val application = controller.activity?.application
                ?: throw IllegalStateException("Controller is not attached to the application")

            return defaultFactory
                ?: ViewModelProvider.AndroidViewModelFactory.getInstance(application).apply {
                    defaultFactory = this
                }
        }
    }
}

//Rx-Scheduler
typealias scheduler = IObservableSchedulerRx2