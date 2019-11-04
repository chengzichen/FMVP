package com.dhc.library.framework

import android.os.Bundle
import android.view.View

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30$ 12:59$
 * @description：  expand Activity&Fragment
 */
interface ISupportBaseView {

    /**
     * get LayoutResID
     *
     * @return layoutResId
     */
    val layoutId: Int

    /**
     * initView &  initData
     *
     * @param savedInstanceState the data most recently supplied in .
     */
    fun initEventAndData(savedInstanceState: Bundle?)

    /**
     * replace  findViewById
     *
     * @param resId   layout resId
     * @param <T>   View
     * @return    View
    </T> */
    fun <T : View> `$`(resId: Int): T

}
