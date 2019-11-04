package com.dhc.library.framework

import com.dhc.library.base.XDaggerActivity

/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 16:07
 * 描述 ：Presenter基类
 */
interface IBasePresenter<V : IBaseView> {

    /**
     * lifecycle attachView
     * @param view
     */
    fun attachView(view: V)

    /**
     * lifecycle detachView[XDaggerActivity.onDestroy]
     */
    fun detachView()

}
