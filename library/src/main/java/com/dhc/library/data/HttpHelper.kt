package com.dhc.library.data

import android.content.Context
import android.text.TextUtils

import com.dhc.library.data.cache.ICache
import com.dhc.library.data.net.CacheInterceptor
import com.dhc.library.data.net.CallInterceptor
import com.dhc.library.data.net.StringConverterFactory
import com.dhc.library.utils.AppUtil
import com.dhc.library.utils.file.FileUtil
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.File
import java.util.concurrent.TimeUnit


import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 15:59
 * 描述 ：网络请求的辅助类
 */
class HttpHelper(var context: Context, private val iCache: ICache)//Map used to store RetrofitService
    : IDataHelper {

    private  var netConfig = IDataHelper.NetConfig()

    private  var okHttpClient: OkHttpClient? = null

    private  var retrofit: Retrofit ? = null

    private var gson: Gson? = null

    override fun initConfig(netConfig: IDataHelper.NetConfig) {
        this.netConfig = netConfig
    }


    override fun <S> getApi(serviceClass: Class<S>): S {
        if (iCache.contains(serviceClass.name)) {
            return iCache[serviceClass.name] as S
        } else {
            val obj = createApi(serviceClass)
            iCache.put(serviceClass.name, obj!!)
            return obj
        }
    }

    override fun <S> getApi(serviceClass: Class<S>, client: OkHttpClient): S {
        if (iCache.contains(serviceClass.name)) {
            return iCache[serviceClass.name] as S
        } else {
            val obj = createApi(serviceClass, client)
            iCache.put(serviceClass.name, obj!!)
            return obj
        }
    }

    override fun <S> createApi(serviceClass: Class<S>): S {
        return createApi(serviceClass, getClient())
    }


    override fun <S> createApi(serviceClass: Class<S>, client: OkHttpClient): S {
        var baseURL = netConfig.baseURL
        if (netConfig.isUseMultiBaseURL) {
            try {
                val field1 = serviceClass.getField("baseURL")
                baseURL = field1.get(serviceClass) as String
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                baseURL = netConfig.baseURL
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                baseURL = netConfig.baseURL
            }

            if (TextUtils.isEmpty(baseURL))
                throw RuntimeException("baseUrl is null .please init by NetModule or apiService field BaseUrl")
        }
        return if (retrofit != null && retrofit!!.baseUrl().host() == baseURL) {
            retrofit!!.create(serviceClass)
        } else {
            getRetrofit(baseURL).create(serviceClass)
        }
    }

    override fun getClient(): OkHttpClient {
        return if (okHttpClient != null) {
            okHttpClient!!
        } else {
            getOkHttpClient()
        }
    }

    fun getGson(): Gson {
        if (gson == null)
            gson = GsonBuilder().create()//Gson解析
        return this.gson!!
    }

    /**
     * 如果是需要返回String的数据是使用
     *
     * @return Retrofit
     */
    fun getRetrofit(host: String): Retrofit {
        if (gson == null)
            gson = getGson()//Gson解析
        if (okHttpClient == null)
            okHttpClient = getOkHttpClient()

        val builder = Retrofit.Builder()
        builder.baseUrl(host)//baseurl路径
        builder.client(okHttpClient)//添加客户端
        builder.addConverterFactory(StringConverterFactory())//添加Gson格式化工厂
                .addConverterFactory(GsonConverterFactory.create(gson!!))//添加Gson格式化工厂
        if (netConfig.factories != null) {
            for (i in netConfig.factories!!.indices) {
                builder.addConverterFactory(netConfig.factories!![i])
            }
        }
        if (netConfig.isUseRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//call 适配器
        }
        retrofit = builder.build()
        return retrofit as Retrofit
    }


    fun getOkHttpClient(): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))//对cooke自动管理管理
        var cacheFile = File(FileUtil.getCacheDirectory(context), "Cache")//缓存路径
        cacheFile = FileUtil.makeDirs(cacheFile)
        val cache = Cache(cacheFile, (1024 * 1024 * 40).toLong())//设置缓存大小为40M
        //缓存
        val cacheInterceptor = CacheInterceptor(context)

        val builder = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                //                        .retryOnConnectionFailure(true)
                .connectTimeout(if (netConfig.connectTimeoutMills != 0L) netConfig.connectTimeoutMills else 15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)//写超时超时
                .readTimeout(if (netConfig.readTimeoutMills != 0L) netConfig.readTimeoutMills else 15, TimeUnit.SECONDS)//读超时
                .cookieJar(if (netConfig.mCookieJar != null) netConfig.mCookieJar else cookieJar)

        //设置https相关
        if (netConfig.mHttpsCall != null) {
            netConfig.mHttpsCall!!.configHttps(builder)
        }
        if (netConfig.call != null) {
            builder.addInterceptor(CallInterceptor(netConfig.call))
        }
        if (netConfig.mInterceptors != null) {
            for (i in netConfig.mInterceptors!!.indices) {
                builder.addInterceptor(netConfig.mInterceptors!![i])
            }
        }
        if (AppUtil.isDebug()) {//如果当前是debug模式就开启日志过滤器
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        //当前okHttpClient
        okHttpClient = builder.build()

        return okHttpClient as OkHttpClient
    }


}
