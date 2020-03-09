package com.zhao.network

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhao.network.api.WanAndroidApiInterface
import com.zhao.network.bean.ArticleBean
import com.zhao.network.bean.ArticleHistory
import com.zhao.network.bean.ArticleZipBean
import com.zhao.network.model.MainModel
import com.zhao.networklib.WanAndroidNetworkApi
import com.zhao.networklib.observer.BaseObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mainModel = MainModel()
    companion object {
        const val tag = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        get_news_channels.setOnClickListener {
//            mainModel.getWxArticle(object : BaseObserver<ArticleBean>(){
//                override fun onSuccess(t: ArticleBean) {
//                    Log.i("MainModel", "onSuccess=${t.data.toString()}")
//                }
//
//                override fun onFailure(e: Throwable) {
//                    Log.i("MainModel", "onFailure=${e.message}")
//                }
//
//            })
          /* mainModel.mergeArticleRequest(object : BaseObserver<Any>() {
                override fun onSuccess(t: Any) {
                    when (t) {
                        is ArticleBean -> {
                            Log.i("MainModel", "onSuccess=ArticleBean")
                        }
                        is ArticleHistory -> {
                            Log.i("MainModel", "onSuccess=ArticleHistory")
                        }
                    }
                }
                override fun onFailure(e: Throwable) {
                    Log.i("MainModel","error=${e.message}")
                }
            })*/
    /*        mainModel.zipArticleRequest(object : BaseObserver<ArticleZipBean>(){
                override fun onSuccess(t: ArticleZipBean) {
                    Log.i("MainModel","onSuccess=${t.articleBeans}")
                }

                override fun onFailure(e: Throwable) {
                    Log.i("MainModel","error=${e.message}")
                }
            })*/
            mainModel.flatMapArticleRequest(object : BaseObserver<ArticleHistory>(){
                override fun onSuccess(t: ArticleHistory) {
                    Log.i("MainModel","onSuccess=${t.data.datas[0].author}")
                }

                override fun onFailure(e: Throwable) {
                    Log.i("MainModel","error=${e.message}")
                }
            })
        }
    }

}
