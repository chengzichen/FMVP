package com.dhc.mvp.modle

import com.dhc.library.data.HttpHelper
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.modle.bean.GankItemBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/22 下午2:22
 * @description：TODO 请描述该类职责
 */

val apiService: Api by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpHelper.INSTANCE.getApi(Api::class.java)
}

interface Api {

     val baseURL: String
         get() = "http://gank.io/api/"

    /**
     * 随机妹纸图
     */
    @GET("random/data/福利/{num}")
    suspend fun getRandomGirl(@Path("num") num: Int): SampleApiResponse<List<GankItemBean>>

}