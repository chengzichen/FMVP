package com.dhc.library.framework

import android.view.View


/**
 * 创建者：邓浩宸
 * 时间 ：2017/3/28 16:44
 * 描述 ：View的代理
 */
interface VDelegate {

    fun resume()

    fun pause()

    fun destory()

    fun visible(flag: Boolean, view: View)

    fun gone(flag: Boolean, view: View)

    fun inVisible(view: View)

    fun toastShort(msg: String)

    fun toastLong(msg: String)

}
