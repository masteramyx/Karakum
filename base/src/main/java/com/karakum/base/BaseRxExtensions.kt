package com.karakum.base

import io.reactivex.Observable
import io.reactivex.Single

//Stole this Extension Funcs

fun <T> Observable<T>.subscribeBy(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
) = this.subscribe(onSuccess, onError)

fun <T> Single<T>.subscribeBy(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
) = this.subscribe(onSuccess, onError)