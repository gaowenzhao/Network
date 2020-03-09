package com.zhao.networklib.base

import com.zhao.networklib.commoninterceptor.CommonRequestInterceptor
import com.zhao.networklib.commoninterceptor.CommonResponseInterceptor
import com.zhao.networklib.environment.EnvironmentActivity
import com.zhao.networklib.environment.IEnvironment
import com.zhao.networklib.errorhandler.HttpErrorHandler
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class NetworkApi : IEnvironment {
    private var okHttpClient: OkHttpClient? = null
    private val retrofitHashMap = HashMap<String, Retrofit>()
    private val baseUrl: String by lazy {
        if (isFormal) getFormal() else getTest()
    }

    companion object {
        private var iNetworkRequiredInfo: INetworkRequiredInfo? = null
        private var isFormal: Boolean = false

        @JvmStatic
        fun init(networkRequiredInfo: INetworkRequiredInfo) {
            iNetworkRequiredInfo = networkRequiredInfo
            isFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.applicationContext)
        }
    }

    protected fun <T> getRetrofit(service: Class<T>): Retrofit {
        var retrofit: Retrofit? = retrofitHashMap[baseUrl + service.name]
        retrofit?.let { return it }
        retrofit = Retrofit.Builder().baseUrl(baseUrl).client(getOkHttp()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        retrofitHashMap[baseUrl + service.name] = retrofit
        return retrofit
    }

    private fun getOkHttp(): OkHttpClient? {
        okHttpClient?.let {
            return it
        } ?: kotlin.run {
            val okHttpClientBuilder = OkHttpClient.Builder()
            getInterceptor()?.run { okHttpClientBuilder.addInterceptor(this) }
            okHttpClientBuilder.addInterceptor(CommonRequestInterceptor(iNetworkRequiredInfo))
            okHttpClientBuilder.addInterceptor(CommonResponseInterceptor())
            if (iNetworkRequiredInfo != null && iNetworkRequiredInfo!!.isDebug) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
            okHttpClient = okHttpClientBuilder.build()
            return okHttpClient
        }
    }

    open fun <T> applySchedulers(observer: Observer<T>): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream ->
            val observable = upstream.map(getAppErrorHandler()).onErrorResumeNext(HttpErrorHandler()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            observable.subscribe(observer)
            observable
        }
    }

    protected abstract fun getInterceptor(): Interceptor?

    protected abstract fun <T> getAppErrorHandler(): Function<T, T>?
}