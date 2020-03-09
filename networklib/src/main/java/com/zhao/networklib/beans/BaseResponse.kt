package com.zhao.networklib.beans


open class BaseResponse{
    var errorCode = 0
    var errorMsg: String? = null
    fun isOk(): Boolean {
        return errorCode == 0
    }
}