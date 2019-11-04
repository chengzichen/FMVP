package com.dhc.library.base

import com.dhc.library.framework.IBasePresenter
import com.dhc.library.framework.IBaseView

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 13:31
 * @description： Used to attachView and detachView
 */
open class XPresenter<V : IBaseView> : IBasePresenter<V> {

    private var mView: V? = null

    /**
     * getViewModel ViewModel  is Activity or Fragment
     *
     * @return  IBaseView
     */
    protected val v: V
        get() {
            checkNotNull(mView) { "view can not be null" }
            return mView as V
        }

    override fun attachView(view: V) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
    }
}