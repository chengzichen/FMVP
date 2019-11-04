package com.dhc.library.framework

import android.content.Context

import com.trello.rxlifecycle3.LifecycleTransformer


/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 16:07
 * 描述 ：View的基类
 */
interface IBaseView {

    /**
     * get  View context
     * @return
     */
    fun getAContext(): Context

    /**
     * Retrofit bind View Lifecycle
     * @param <T>   Response data
     * @return  Response data
    </T> */
    fun <T> bindLifecycle(): LifecycleTransformer<T>
    /**
     * Retrofit bind View Destroy
     * @param <T>   Response data
     * @return  Response data
    </T> */
    fun <T> bindDestroy(): LifecycleTransformer<T>

}
