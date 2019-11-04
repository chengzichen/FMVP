package com.dhc.mvp.presenter

import android.graphics.Bitmap

import com.dhc.library.data.net.NetError
import com.dhc.library.utils.AppContext
import com.dhc.library.utils.sys.ScreenUtil
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.App.SampleSubscriber
import com.dhc.mvp.App.SampleSubscriberListener
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.contract.INetTestContract
import com.dhc.mvp.modle.NetTestRemoteDataService
import javax.inject.Inject
import com.dhc.library.base.XPresenter

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

/**
 * @author
 * @createDate
 * @description
 */

class NetTestPresenter @Inject
constructor(private val mNetTestRemoteDataService: NetTestRemoteDataService) : XPresenter<INetTestContract.IView>(), INetTestContract.IPresenter {


    override fun getRandomGirl() {
        mNetTestRemoteDataService.randomGirl
                .compose(v.bindLifecycle())
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