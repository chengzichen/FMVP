package com.dhc.library.data.net

import retrofit2.HttpException

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 13:50
 * @description:
 */
abstract class SubscriberListener<D> {

    /**
     * 是否显示dialog
     * @return
     */
    val isShowLoading: Boolean
        get() = true

    /**
     * 成功
     * @param data
     */
    abstract fun onSuccess(data: D?)

    /**
     * 最终错误处理
     * @param error
     */
    abstract fun onFail(error: NetError)

    /**
     * 抛出异常,还是在onFail中处理
     * @param e
     */
    abstract fun onError(e: Throwable)

    /**
     * end
     */
    fun onCompleted() {}

    /**
     * begin
     */
    fun onBegin() {}

    /**
     * 是否需要重新登录
     * @return
     * @param httpException
     */
    open fun isCheckReLogin(httpException: HttpException): Boolean {
        return false
    }

    /**
     * 重新登录处理
     * @param errorCode
     * @param errorMsg
     */
    abstract fun checkReLogin(errorCode: String, errorMsg: String)
}
