package com.zhao.network.bean

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.operators.observable.ObservableZip
import io.reactivex.plugins.RxJavaPlugins

class test {
    fun <T, R> zipArray(
        zipper: Function<in Array<Any?>?, out R>?, delayError: Boolean, bufferSize: Int, vararg sources: ObservableSource<out T>?
    ): Observable<R>? {
        if (sources.size == 0) {
            return Observable.empty()
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null")
        ObjectHelper.verifyPositive(bufferSize, "bufferSize")
        return RxJavaPlugins.onAssembly(ObservableZip(sources, null, zipper, bufferSize, delayError))
    }
    fun <T1, T2, R> zip(
        source1: ObservableSource<out T1?>?, source2: ObservableSource<out T2?>?, zipper: BiFunction<in T1?, in T2?, out R>?, delayError: Boolean
    ): Observable<R>? {
        ObjectHelper.requireNonNull(source1, "source1 is null")
        ObjectHelper.requireNonNull(source2, "source2 is null")
        return Observable.zipArray(
            Functions.toFunction(zipper), delayError, Observable.bufferSize(), source1, source2
        )
    }
}