package com.dhc.library.base

import android.content.Context
import android.os.Bundle

import com.dhc.library.di.module.ActivityModule
import com.dhc.library.framework.IBasePresenter
import com.dhc.library.framework.IBaseView
import com.dhc.library.framework.ISupportDagger
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.ActivityEvent

import javax.inject.Inject


/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 13:20
 * @description： XDaggerActivity is  MVP by Dagger2
 */
abstract class XDaggerActivity<P : IBasePresenter<V>, V : IBaseView> : BaseActivity(), IBaseView,
        ISupportDagger {

    @Inject
    @JvmField
    protected var mPresenter: P? = null


    protected val activityModule: ActivityModule
        get() = ActivityModule(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject(savedInstanceState)
        if (mPresenter != null)
            mPresenter!!.attachView(this as V)
        super.onCreate(savedInstanceState)

    }


    override fun <T> bindDestroy(): LifecycleTransformer<T> {
        return this.bindUntilEvent<T>(ActivityEvent.DESTROY)
    }

    override fun <E> bindLifecycle(): LifecycleTransformer<E> {
        return this.bindToLifecycle()
    }


    override fun getAContext(): Context {
        return this
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null)
            mPresenter!!.detachView()
    }

}