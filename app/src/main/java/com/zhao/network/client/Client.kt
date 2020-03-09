package com.zhao.network.client

import com.zhao.network.api.WanAndroidApiInterface
import com.zhao.networklib.WanAndroidNetworkApi

object Client {
    val wanAndroidApi by lazy {
        WanAndroidNetworkApi.getService(WanAndroidApiInterface::class.java)
    }
}