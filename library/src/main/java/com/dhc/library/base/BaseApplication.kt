package com.dhc.library.base


import android.app.Application
import android.content.res.Configuration

import com.dhc.library.data.IDataHelper
import com.dhc.library.di.component.AppComponent
import com.dhc.library.di.component.DaggerAppComponent
import com.dhc.library.framework.ISupportApplication
import com.dhc.library.framework.XAppDelegate

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 12:00
 * @description： BaseApplication
 */
open class BaseApplication : Application(), ISupportApplication {
     var  xAppDelegate: XAppDelegate? = null


    override fun onCreate() {
        super.onCreate()
        xAppDelegate = XAppDelegate.DefaultAppDelegate(this).netConfig(getNetConfig())
        (xAppDelegate as XAppDelegate.DefaultAppDelegate).onCreate()
    }


    override fun getAppComponent(): AppComponent {
        return xAppDelegate!!.getAppComponent()
    }

    override fun getAppComponentBuilder(): DaggerAppComponent.Builder {
        return xAppDelegate!!.getAppComponentBuilder()
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


}
