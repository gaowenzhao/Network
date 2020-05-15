package com.zhao.networklib.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T> : Observer<T>{
    override fun onComplete() {

    }

    override fun onNext(t: T) {
        onSuccess(t)
    }
    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }
    abstract fun onSuccess(t: T)
    open fun onFailure(e: Throwable){}
}