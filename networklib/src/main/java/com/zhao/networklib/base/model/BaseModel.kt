package com.zhao.networklib.base.model

import com.zhao.networklib.beans.BaseResponse
import com.zhao.networklib.errorhandler.ExceptionHandle
import com.zhao.networklib.errorhandler.HttpErrorHandler
import com.zhao.networklib.observer.BaseObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

open class BaseModel {
    fun mergeRequest(observer: BaseObserver<Any>,vararg args: Observable<*>) {
        subscribe(Observable.mergeArray(*args), observer)
    }
    fun <T1, T2, R> zipRequest(observer: BaseObserver<R>, source1: Observable<T1>,
                               source2: Observable<T2>, zipper: BiFunction<T1,T2,R>){
        handleThread(Observable.zip(handleError(source1),handleError(source2),zipper,false)).subscribe(observer)
    }

    fun <T> subscribe(observable: Observable<T>, observer: BaseObserver<T>) {
        handleErrorAndThread(observable).subscribe(observer)
    }
    private fun <T> getAppErrorHandler(): Function<T, T> {
        return Function { response ->
            when (response) {
                is BaseResponse -> {
                    if(!response.isOk()){
                        val exception = ExceptionHandle.Companion.ServerException()
                        exception.code =  response.errorCode
                        exception.message = response.errorMsg
                        throw exception
                    }
                }
            }
            response
        }
    }
    open fun <T> handleError(observable: Observable<T>): Observable<T>{
        return observable.map(getAppErrorHandler()).onErrorResumeNext(HttpErrorHandler())
    }
    private fun <T> handleThread(observable: Observable<T>): Observable<T>{
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    private fun <T> handleErrorAndThread(observable: Observable<T>): Observable<T> {
        return handleThread(handleError(observable))
    }
}