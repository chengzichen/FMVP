package com.dhc.mvp.net

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

import com.dhc.library.base.XDaggerActivity
import com.dhc.library.framework.IBasePresenter
import com.dhc.library.framework.IBaseView
import com.dhc.mvp.R
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.RxCacheTestPresenter
import com.dhc.mvp.presenter.contract.INetTestContract
import java.util.concurrent.TimeUnit

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subscribers.ResourceSubscriber

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午2:55
 * @description：使用 RXCACHE示例
 */
class RxJavaSampleActivity : XDaggerActivity<IBasePresenter<IBaseView>, IBaseView>() {

    private var title: TextView? = null
    private var content: TextView? = null
    internal var mDisposable = CompositeDisposable()
    override val layoutId: Int
        get() = R.layout.activity_rxcache_sample

    override fun initEventAndData(savedInstanceState: Bundle?) {
        title = `$`(R.id.tv_title)
        content = `$`(R.id.tv_content)
        title!!.text = "点我"
        title!!.setOnClickListener { mDisposable.clear() }
        content!!.setOnClickListener {
            val resourceSubscriber = object : ResourceSubscriber<Long>() {
                override fun onNext(aLong: Long?) {
                    Log.d("11111", aLong.toString())
                    content!!.text = aLong.toString()
                }

                override fun onError(t: Throwable) {

                }

                override fun onComplete() {

                }
            }

            Flowable.interval(2000, 2000, TimeUnit.MILLISECONDS)
                    .compose<Long>(this@RxJavaSampleActivity
                            .bindLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resourceSubscriber)

            mDisposable.add(resourceSubscriber)
        }
    }


    override fun initInject(savedInstanceState: Bundle?) {

    }


}
