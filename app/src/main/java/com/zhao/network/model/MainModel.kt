package com.zhao.network.model

import com.zhao.network.bean.ArticleBean
import com.zhao.network.bean.ArticleHistory
import com.zhao.network.bean.ArticleZipBean
import com.zhao.network.client.Client.wanAndroidApi
import com.zhao.networklib.base.model.BaseModel
import com.zhao.networklib.observer.BaseObserver
import io.reactivex.functions.BiFunction

class MainModel : BaseModel() {
    fun getWxArticle(observer: BaseObserver<ArticleBean>) {
        //        wanAndroidApi.getWxArticle().compose(WanAndroidNetworkApi.INSTANCE.applySchedulers(observer))
        subscribe(wanAndroidApi.getWxArticle(), observer)
    }

    fun mergeArticleRequest(observer: BaseObserver<Any>) {
        mergeRequest(observer, wanAndroidApi.getWxArticle(), wanAndroidApi.getArticleHistory())
    }

    fun zipArticleRequest(observer: BaseObserver<ArticleZipBean>) {
        zipRequest(observer,
            wanAndroidApi.getWxArticle(),
            wanAndroidApi.getArticleHistory(),
            BiFunction<ArticleBean, ArticleHistory, ArticleZipBean> { t1, t2 ->
                ArticleZipBean().apply {
                    articleBeans = t1.data
                    articleHistory = t2.data
                }
            })
    }

    fun flatMapArticleRequest(observer: BaseObserver<ArticleHistory>) {
        subscribe(handleError(wanAndroidApi.getWxArticle()).flatMap { response->
            wanAndroidApi.getArticleHistory()
        },observer)
    }
}