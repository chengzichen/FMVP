package com.dhc.library.base


import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

import com.dhc.library.data.IDataHelper
import com.dhc.library.framework.ISupportApplication
import com.dhc.library.framework.XAppDelegate

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 12:00
 * @description： BaseApplication
 */
open class BaseApplication : Application(), ISupportApplication, ViewModelStoreOwner {
     var  xAppDelegate: XAppDelegate? = null


    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        xAppDelegate = XAppDelegate.DefaultAppDelegate(this).netConfig(getNetConfig())
        (xAppDelegate as XAppDelegate.DefaultAppDelegate).onCreate()
    }



    override fun getNetConfig(): IDataHelper.NetConfig? {
        return null
    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        xAppDelegate!!.onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        xAppDelegate!!.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        xAppDelegate!!.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        xAppDelegate!!.onConfigurationChanged(newConfig)
    }
    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }


    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

}
