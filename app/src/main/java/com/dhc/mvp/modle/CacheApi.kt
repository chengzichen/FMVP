package com.dhc.mvp.modle

import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.modle.bean.GankItemBean
import java.util.concurrent.TimeUnit

import io.reactivex.Flowable
import io.rx_cache2.LifeCache
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @creator：denghc(desoce)
 * @updateTime：rxcache 缓存
 */
interface CacheApi {

    /**
     * 随机妹纸图
     */
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)//两分钟失效
    fun getRandomGirl(sampleApiResponseFlowable: Flowable<SampleApiResponse<List<GankItemBean>>>): Flowable<SampleApiResponse<List<GankItemBean>>>
}
