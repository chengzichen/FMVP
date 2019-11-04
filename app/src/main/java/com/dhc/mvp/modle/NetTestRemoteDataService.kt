package com.dhc.mvp.modle

import javax.inject.Inject
import com.dhc.library.data.HttpHelper
import com.dhc.library.utils.rx.RxUtil
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.contract.INetTestContract

import io.reactivex.Flowable

/**
 * @author
 * @createDate
 * @description
 */

class NetTestRemoteDataService @Inject
constructor(private val mHttpHelper: HttpHelper) : INetTestContract.IModel {


    override val randomGirl: Flowable<SampleApiResponse<List<GankItemBean>>>
        get() = mHttpHelper.createApi(Api::class.java).getRandomGirl(1).compose(RxUtil.rxSchedulerHelper())

}