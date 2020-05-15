package com.zhao.networklib

import com.zhao.networklib.base.NetworkApi
import com.zhao.networklib.beans.BaseResponse
import com.zhao.networklib.errorhandler.ExceptionHandle
import io.reactivex.functions.Function
import okhttp3.Interceptor
import okhttp3.Request

class WanAndroidNetworkApi : NetworkApi() {
    companion object {
        val INSTANCE: WanAndroidNetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WanAndroidNetworkApi()
        }

        fun <T> getService(service: Class<T>): T = INSTANCE.getRetrofit(service).create(service)
    }

    override fun getInterceptor(): Interceptor? {
        return Interceptor { chain ->
            val builder: Request.Builder = chain.request().newBuilder()
            builder.header("Content-Type", "application/json; charset=UTF-8")
            builder.header("client", "2")
            chain.proceed(builder.build())
        }
    }

    override fun <T> getAppErrorHandler(): Function<T, T>? {
        return Function{ response ->
            if (response is BaseResponse) {
                if(response.errorCode != 0){
                    val exception = ExceptionHandle.Companion.ServerException()
                    exception.code =  response.errorCode
                    exception.message = response.errorMsg
                    throw exception
                }
            }
            response
        }
    }

    override fun getFormal(): String {
        return "https://www.wanandroid.com"
    }

    override fun getTest(): String {
        return "https://www.wanandroid.com"
    }
}