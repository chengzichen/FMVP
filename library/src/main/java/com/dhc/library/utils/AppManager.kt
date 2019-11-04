package com.dhc.library.utils

import android.app.Activity
import com.dhc.library.utils.AppManager.Companion.activityStack

import java.util.Stack

/**
 * 创建者：denghaochen
 * 时间 ：2016-09-07 下午 3:43
 * 描述 ：Activity进栈出栈统一管理
 */
class AppManager private constructor() {


    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }


    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }


    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
        }
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
        }
    }

    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    fun finishALLExcludeCurrentActivity(cls: Class<*>) {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                if (activityStack!![i].javaClass != cls) {
                    activityStack!![i].finish()
                }
            }
            i++
        }
    }

    fun finishALLExcludeCurrentActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                if (activityStack!![i] != currentActivity()) {
                    activityStack!![i].finish()
                }
            }
            i++
        }
    }


    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 退出应用
     */
    fun AppExit() {
        try {
            finishAllActivity()
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
        }

    }
    companion object {
        private var activityStack: Stack<Activity>? = null
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }
}
