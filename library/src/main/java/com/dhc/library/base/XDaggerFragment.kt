package com.dhc.library.base

import android.content.Context
import android.os.Bundle
import android.view.View

import com.dhc.library.di.module.FragmentModule
import com.dhc.library.framework.IBasePresenter
import com.dhc.library.framework.IBaseView
import com.dhc.library.framework.ISupportDagger
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.FragmentEvent

import javax.inject.Inject

/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 16:07
 * 描述 ： XDaggerFragment is  MVP by Dagger2
 */
abstract class XDaggerFragment<T : IBasePresenter<V>,V:IBaseView> : BaseFragment(), IBaseView,
        ISupportDagger {

    @Inject
    @JvmField
    protected  var mPresenter: T? = null
    var isShowView = false

    protected val fragmentModule: FragmentModule
        get() = FragmentModule(this)


    override fun <E> bindLifecycle(): LifecycleTransformer<E> {
        return this.bindToLifecycle()
    }

    override fun <T> bindDestroy(): LifecycleTransformer<T> {
        return this.bindUntilEvent(FragmentEvent.DESTROY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInject(savedInstanceState)
        if (mPresenter != null) {
            isShowView = true
            mPresenter!!.attachView(this as V)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun getAContext(): Context {
        return _mActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null)
            mPresenter!!.detachView()
    }

}