package com.dhc.mvp.presenter

import com.dhc.library.base.XPresenter
import com.dhc.library.data.net.NetError
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.App.SampleSubscriber
import com.dhc.mvp.App.SampleSubscriberListener
import com.dhc.mvp.modle.CacheApi
import com.dhc.mvp.modle.NetTestRemoteDataService
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.contract.INetTestContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import org.reactivestreams.Publisher

import java.lang.reflect.Type

import javax.inject.Inject

import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.rx_cache2.internal.RxCache

/**
 * @author
 * @createDate
 * @description
 */

class RxCacheTestPresenter @Inject
constructor(private val mNetTestRemoteDataService: NetTestRemoteDataService, rxCache: RxCache) : XPresenter<INetTestContract.IView>(), INetTestContract.IPresenter {
    private val cacheApi: CacheApi

    init {
        cacheApi = rxCache.using(CacheApi::class.java)
    }


    override fun getRandomGirl() {
        //解决BUGhttps://github.com/VictorAlbertos/RxCache/issues/73
        cacheApi.getRandomGirl(mNetTestRemoteDataService.randomGirl)
                .compose(v.bindLifecycle())
                .flatMap {
                    listSampleApiResponse: SampleApiResponse<List<GankItemBean>>->
                    val data = listSampleApiResponse.data
                    if (data != null) {
                        val gson = GsonBuilder().create()
                        val s = gson.toJson(data)
                        val type = object : TypeToken<List<GankItemBean>>() {

                        }.type
                        val dataCopy = gson.fromJson<List<GankItemBean>>(s, type)

                        listSampleApiResponse.setData(dataCopy)
                    }
                    Flowable.just<SampleApiResponse<List<GankItemBean>>>(listSampleApiResponse)
                }
                .subscribe(SampleSubscriber(object : SampleSubscriberListener<List<GankItemBean>>() {
                    override fun onSuccess(response: List<GankItemBean>?) {
                        v.success(response)
                    }

                    override fun onFail(errorMsg: NetError) {
                        super.onFail(errorMsg)
                        v.failure("-1", errorMsg.message)
                    }
                }))
    }
}