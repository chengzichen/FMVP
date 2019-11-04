package com.dhc.library.utils.rx

import com.dhc.library.data.net.NetError
import com.dhc.library.data.net.SubscriberListener
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException

import org.json.JSONException

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import retrofit2.HttpException


/**
 * 创建者     邓浩宸
 * 创建时间   2017/3/16 13:51
 * 描述	      ${请求统一处理}
 */
abstract class BaseSubscriberListener<T> : SubscriberListener<T>() {


    override fun onFail(errorMsg: NetError) {

    }


    override fun onError(e: Throwable) {
        var error: NetError? = null
        if (e != null) {
            if (e !is NetError) {
                if (e is UnknownHostException) {
                    error = NetError(e, NetError.NoConnectError)
                } else if (e is JSONException
                        || e is JsonParseException
                        || e is JsonSyntaxException) {
                    error = NetError(e, NetError.ParseError)
                } else if (e is SocketException || e is SocketTimeoutException) {
                    error = NetError(e, NetError.SocketError)
                } else if (e is HttpException) {
                    if (isCheckReLogin(e)) {//去认证
                        checkReLogin("401", "checkout")
                    }
                    error = NetError(e, NetError.NetError)
                } else {
                    error = NetError(e, NetError.OtherError)
                }
            } else {
                error = e
            }
            onFail(error)
        }
    }

    override fun checkReLogin(errorCode: String, errorMsg: String) {
        //todo
    }

    override fun isCheckReLogin(httpException: HttpException): Boolean {
        return false
    }

}
