package com.dhc.library.di.module


import com.dhc.library.base.BaseApplication
import com.dhc.library.data.DBHelper
import com.dhc.library.data.HttpHelper
import com.dhc.library.data.IDataHelper
import com.dhc.library.data.cache.ICache
import com.dhc.library.data.cache.MemoryCache
import com.dhc.library.utils.file.FileUtil

import java.io.File

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker

/**
 * 创建者     邓浩宸
 * 创建时间   2018/3/29 12:51
 * 描述	     数据工具提供者
 */
@Module
class DataModule(internal var netConfig: IDataHelper.NetConfig?) {

    @Provides
    @Singleton
    internal fun provideHttpHelper(application: BaseApplication, iCache: ICache): HttpHelper {
        val httpHelper = HttpHelper(application, iCache)
        if (netConfig == null)
            netConfig = IDataHelper.NetConfig()
        httpHelper.initConfig(netConfig!!)
        return httpHelper
    }

    @Provides
    @Singleton
    internal fun provideDatabaseHelper(application: BaseApplication, iCache: ICache): DBHelper {
        return DBHelper(application, iCache)
    }


    @Singleton
    @Provides
    internal fun provideLruCache(): ICache {
        return MemoryCache.instance
    }


    /**
     * 提供 [RxCache]
     *
     * @param cacheDirectory RxCache缓存路径
     * @return
     */
    @Singleton
    @Provides
    internal fun provideRxCache(@Named("RxCacheDirectory") cacheDirectory: File): RxCache {
        val builder = RxCache.Builder()
        return builder
                .persistence(cacheDirectory, GsonSpeaker())
    }

    /**
     * 需要单独给 [RxCache] 提供缓存路径
     *
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    internal fun provideRxCacheDirectory(application: BaseApplication): File {
        val cacheDirectory = File(FileUtil.getCacheDirectory(application), "RxCache")
        return FileUtil.makeDirs(cacheDirectory)
    }

}
