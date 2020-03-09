package com.zhao.networklib.errorhandler

import com.zhao.networklib.errorhandler.ExceptionHandle.Companion.handleException
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * HttpResponseFunc处理以下两类网络错误：
 * 1、http请求相关的错误，例如：404，403，socket timeout等等；
 * 2、应用数据的错误会抛RuntimeException，最后也会走到这个函数来统一处理；
 */
class HttpErrorHandler<T> : Function<Throwable, Observable<T>>{
    override fun apply(t: Throwable): Observable<T> {
        return Observable.error(handleException(t))
    }
}