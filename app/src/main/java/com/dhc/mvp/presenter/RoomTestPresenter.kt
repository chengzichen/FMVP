package com.dhc.mvp.presenter

import com.dhc.library.base.XPresenter
import com.dhc.library.data.DBHelper
import com.dhc.library.data.net.NetError
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.App.SampleSubscriber
import com.dhc.mvp.App.SampleSubscriberListener
import com.dhc.mvp.dao.AppDatabase
import com.dhc.mvp.modle.NetTestRemoteDataService
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.contract.INetTestContract

import javax.inject.Inject

import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * @author
 * @createDate
 * @description
 */

class RoomTestPresenter @Inject
constructor(private val mNetTestRemoteDataService: NetTestRemoteDataService, dbHelper: DBHelper) : XPresenter<INetTestContract.IView>(), INetTestContract.IPresenter {
    private val cacheApi: AppDatabase

    init {
        cacheApi = dbHelper.getApi(AppDatabase::class.java, "gankitem")
    }


    override fun getRandomGirl() {
        mNetTestRemoteDataService.randomGirl
                .compose(v.bindLifecycle())
                .observeOn(Schedulers.io())
                .doOnNext { listSampleApiResponse -> cacheApi.gankDao().insertAll()
                    (listSampleApiResponse.data.get(0)) }
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


    fun loadAllByIds(ids: Array<String>): Flowable<List<GankItemBean>> {
        return cacheApi.gankDao().loadAllByIds(ids).subscribeOn(Schedulers.io())
    }
}