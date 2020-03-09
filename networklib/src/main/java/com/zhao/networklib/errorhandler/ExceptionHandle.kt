package com.zhao.networklib.errorhandler

import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException

class ExceptionHandle {
    companion object {
        class ResponeThrowable(throwable: Throwable?, var code: Int) : Exception(throwable) {
            override var message: String? = null
        }

        class ServerException : RuntimeException() {
            var code = 0
            override var message: String? = null
        }

        @JvmStatic
        fun handleException(e: Throwable): ResponeThrowable {
            return when (e) {
                is HttpException -> ResponeThrowable(e, ERROR.HTTP_ERROR).apply { message = "网络错误" }
                is ServerException -> ResponeThrowable(e, e.code).apply { message = e.message }
                is JsonParseException, is JSONException, is ParseException -> ResponeThrowable(e, ERROR.PARSE_ERROR).apply { message = "解析错误" }
                is ConnectException -> ResponeThrowable(e, ERROR.NETWORD_ERROR).apply { message = "连接失败" }
                is javax.net.ssl.SSLHandshakeException -> ResponeThrowable(e, ERROR.SSL_ERROR).apply { message = "证书验证失败" }
                is ConnectTimeoutException, is java.net.SocketTimeoutException -> ResponeThrowable(e, ERROR.TIMEOUT_ERROR).apply { message = "连接超时" }
                else -> ResponeThrowable(e, ERROR.UNKNOWN).apply { message = "未知错误" }
            }
        }
    }

    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003
        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005
        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }
}