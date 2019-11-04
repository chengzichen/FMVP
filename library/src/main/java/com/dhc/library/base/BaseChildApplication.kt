package com.dhc.library.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log

import com.dhc.library.utils.ApplicationLike


/**
 * @creator: denghc(desoce)
 * @updateTime: 2018/7/30 12:01
 * @description: This Application can only be used in the child moudle for moudle isolation
 */
class BaseChildApplication : BaseApplication(), ApplicationLike {

    override fun onCreate() {
        super.onCreate()
        onCreateAsLibrary(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onLowMemoryAsLibrary(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        onTrimMemoryAsLibrary(this, level)
    }

    override fun onTerminate() {
        super.onTerminate()
        onTerminate(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onConfigurationChanged(this, newConfig)
    }

    override fun onTerminate(application: Application) {
        Log.i(TAG, this.javaClass.name + "onTerminate")
    }

    override fun onCreateAsLibrary(application: Application) {
        Log.i(TAG, this.javaClass.name + "onCreateAsLibrary")
    }

    override fun onLowMemoryAsLibrary(application: Application) {
        Log.i(TAG, this.javaClass.name + "onLowMemoryAsLibrary")
    }

    override fun onTrimMemoryAsLibrary(application: Application, level: Int) {
        Log.i(TAG, this.javaClass.name + "onTrimMemoryAsLibrary")
    }

    override fun onConfigurationChanged(application: Application, configuration: Configuration) {
        Log.i(TAG, this.javaClass.name + "onConfigurationChanged")
    }


    override fun init(context: Context) {

    }

    companion object {

        private val TAG = BaseChildApplication::class.java.simpleName
    }


}
