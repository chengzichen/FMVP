package com.dhc.library.utils.sys

import android.content.Context
import android.content.res.Resources

import java.lang.reflect.Field

/**
 * 分辨率转换和屏幕参数工具类
 */
object DensityUtils {


    val imageMaxEdge: Int
        get() = (165.0 / 320.0 * ScreenUtil.screenWidth).toInt()

    val imageMinEdge: Int
        get() = (76.0 / 320.0 * ScreenUtil.screenWidth).toInt()


    /**
     * dp to px, return by int
     *
     * @param context Context
     * @param dpValue value in dp
     * @return value in px, by int
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return (dp2pxF(context, dpValue) + 0.5f).toInt()
    }


    /**
     * dp to px, return by float
     *
     * @param context Context
     * @param dpValue value in dp
     * @return value in px, by float
     */
    fun dp2pxF(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale
    }

    /**
     * px to dp, return by int
     *
     * @param context Context
     * @param pxValue value in px
     * @return value in dp, by int
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        return (px2dpF(context, pxValue) + 0.5f).toInt()
    }

    /**
     * px to dp, return by float
     *
     * @param context Context
     * @param pxValue value in px
     * @return value in dp
     */
    fun px2dpF(context: Context, pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale
    }

    /**
     * px to sp, return by int
     *
     * @param context Context
     * @param pxValue value in px
     * @return value in sp
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        return (px2spF(context, pxValue) + 0.5f).toInt()
    }

    /**
     * px to sp, return by float
     *
     * @param context Context
     * @param pxValue value in px
     * @return value in sp
     */
    fun px2spF(context: Context, pxValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return pxValue / fontScale
    }

    /**
     * sp to px, return by int
     *
     * @param context Context
     * @param spValue value in sp
     * @return value in px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        return (sp2pxF(context, spValue) + 0.5f).toInt()
    }

    /**
     * sp to px, return by float
     *
     * @param context Context
     * @param spValue value in sp
     * @return value in px
     */
    fun sp2pxF(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Context
     * @return 屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param context Context
     * @return 屏幕高度
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取状态栏高度
     *
     * @param context Context
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>
        val obj: Any
        val field: Field
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            val x = Integer.parseInt(field.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }

    /**
     * 获取导航栏高度
     *
     * @param context Context
     * @return 导航栏高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelOffset(identifier)
    }


}
