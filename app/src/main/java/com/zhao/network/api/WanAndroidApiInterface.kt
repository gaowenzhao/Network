package com.zhao.network.api

import com.zhao.network.bean.ArticleBean
import com.zhao.network.bean.ArticleHistory
import io.reactivex.Observable
import retrofit2.http.GET

interface WanAndroidApiInterface {

    /**
     * 首页轮播图
     */
    @GET("/wxarticle/chapters/json")
    fun getWxArticle(): Observable<ArticleBean>

    @GET("/wxarticle/list/408/1/json")
    fun getArticleHistory(): Observable<ArticleHistory>

}