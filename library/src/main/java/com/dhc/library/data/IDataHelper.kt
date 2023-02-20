package com.dhc.library.data

import com.google.gson.GsonBuilder
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * 创建者     邓浩宸
 * 创建时间   2017/3/28 13:04
 * 描述	     数据处理接口类
 */
interface IDataHelper {

    fun getClient(): OkHttpClient

    interface RequestCall {

        fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request

        fun onAfterRequest(response: Response, result: ResponseBody, chain: Interceptor.Chain): Response
    }

    interface HttpsCall {

        fun configHttps(builder: OkHttpClient.Builder)

    }

    interface GsonCall {

        fun configGson(builder: GsonBuilder)
    }

    open class NetConfig {
        var mInterceptors: Array<Interceptor>? = null
        var factories: Array<Converter.Factory>? = null
        var mCookieJar: CookieJar? = null
        var call: RequestCall? = null
        var gsonCall: GsonCall? = null
        var mHttpsCall: HttpsCall? = null
        var connectTimeoutMills: Long = 0
        var readTimeoutMills: Long = 0
        var isHasLog: Boolean = false
        var isUseRx = true
        var baseURL = ""
        var isUseMultiBaseURL = true

        /**
         * add okhttp Interceptors
         * @param configInterceptors
         * @return
         */
        fun configInterceptors(configInterceptors: Array<Interceptor>): NetConfig {
            this.mInterceptors = configInterceptors
            return this
        }

        /**
         * add okhttp Converter.Factory
         * @param factories
         * @return
         */
        fun configConverterFactory(factories: Array<Converter.Factory>): NetConfig {
            this.factories = factories
            return this
        }

        /**
         * can use multi baseurl [com.dhc.library.data.HttpHelper.createApi]
         * @param isUseMultiBaseURL
         * @return
         */
        fun configisUseMultiBaseURL(isUseMultiBaseURL: Boolean): NetConfig {
            this.isUseMultiBaseURL = isUseMultiBaseURL
            return this
        }

        /**
         * root baseurl [com.dhc.library.data.HttpHelper.createApi]
         * @param baseURL
         * @return
         */
        fun configBaseURL(baseURL: String): NetConfig {
            this.baseURL = baseURL
            return this
        }

        /**
         * config cookieManager
         * @param mCookieJar
         * @return
         */
        fun configCookieJar(mCookieJar: CookieJar): NetConfig {
            this.mCookieJar = mCookieJar
            return this
        }

        /**
         *
         * @param call
         * @return
         */
        fun configCall(call: RequestCall): NetConfig {
            this.call = call
            return this
        }
        /**
         *
         * @param gsonCall
         * @return
         */
        fun gsonCall(call: GsonCall): NetConfig {
            this.gsonCall = call
            return this
        }

        fun configConnectTimeoutMills(connectTimeoutMills: Long): NetConfig {
            this.connectTimeoutMills = connectTimeoutMills
            return this
        }

        fun configReadTimeoutMills(readTimeoutMills: Long): NetConfig {
            this.readTimeoutMills = readTimeoutMills
            return this
        }

        fun configLogEnable(isHasLog: Boolean): NetConfig {
            this.isHasLog = isHasLog
            return this
        }

        fun configIsUseRx(isUseRx: Boolean): NetConfig {
            this.isUseRx = isUseRx
            return this
        }

        fun configHttps(mHttpsCall: HttpsCall): NetConfig {
            this.mHttpsCall = mHttpsCall
            return this
        }
    }

    fun <S> getApi(serviceClass: Class<S>): S

    fun <S> createApi(serviceClass: Class<S>): S

    fun <S> getApi(serviceClass: Class<S>, client: OkHttpClient): S

    fun <S> createApi(serviceClass: Class<S>, client: OkHttpClient): S

    fun initConfig(netConfig: IDataHelper.NetConfig?)
}
