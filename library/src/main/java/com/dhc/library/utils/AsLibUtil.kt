package com.dhc.library.utils

import android.app.Application
import android.content.res.Configuration

import java.util.ArrayList

/**
 * 创建者     邓浩宸
 * 创建时间   2017/3/29 11:55
 * 描述	      ${TODO}
 */
object AsLibUtil {


    private val mChildApplicationList = ArrayList<ApplicationLike>()

    fun addAsLIbChild(applicationAsLibrary: ApplicationLike?) {
        if (applicationAsLibrary != null)
            mChildApplicationList.add(applicationAsLibrary)
    }

    fun doCreateAsLibrary(application: Application) {
        for (app in mChildApplicationList) {
            app?.onCreateAsLibrary(application)
        }
    }

    fun onLowMemoryAsLibrary(application: Application) {
        for (app in mChildApplicationList) {
            app?.onLowMemoryAsLibrary(application)
        }
    }

    fun onTrimMemoryAsLibrary(application: Application, level: Int) {
        for (app in mChildApplicationList) {
            app?.onTrimMemoryAsLibrary(application, level)
        }
    }

    fun onTerminate(application: Application) {
        for (app in mChildApplicationList) {
            app?.onTerminate(application)
        }
    }

    fun onConfigurationChanged(application: Application, newConfig: Configuration) {
        for (app in mChildApplicationList) {
            app?.onConfigurationChanged(application, newConfig)
        }
    }
}
