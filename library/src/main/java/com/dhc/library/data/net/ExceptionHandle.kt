package com.dhc.library.data.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 根据异常返回相关的错误信息工具类
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): NetError {
        val ex: NetError
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = NetError(Error.NETWORK_ERROR,e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = NetError(Error.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = NetError(Error.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = NetError(Error.SSL_ERROR,e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = NetError(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = NetError(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = NetError(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is NetError -> return it

                else -> {
                    ex = NetError(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = NetError(Error.UNKNOWN,e)
        return ex
    }
}