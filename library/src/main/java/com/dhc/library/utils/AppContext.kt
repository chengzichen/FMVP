package com.dhc.library.utils

import android.content.Context


/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 13:52
 * @description: save AppContext
 */
object AppContext {
    @JvmStatic
    var mAppContext: Context? = null

    @JvmStatic
    fun init(context: Context) {
        if (mAppContext == null) {
            mAppContext = context.applicationContext
        } else {
            throw IllegalStateException("set context duplicate")
        }
    }
    @JvmStatic
    fun get(): Context {
        return if (mAppContext == null) {
            throw IllegalStateException("forget init?")
        } else {
            mAppContext!!
        }
    }
}
