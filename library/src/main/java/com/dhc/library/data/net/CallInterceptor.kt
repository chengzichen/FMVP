package com.dhc.library.data.net

import com.dhc.library.data.IDataHelper

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response


/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 13:49
 * @description： Do before and after requests
 */
class CallInterceptor(internal var call: IDataHelper.RequestCall?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (call != null) {
            if (call!!.onBeforeRequest(request, chain) != null)
                request = call!!.onBeforeRequest(request, chain)
        }
        var response = chain.proceed(request)
        if (call != null) {
            //response.body()不能多次使用string方法
            if (call!!.onAfterRequest(response, response.body()!!, chain) != null)
                response = call!!.onAfterRequest(response, response.body()!!, chain)
        }
        return response
    }
}
