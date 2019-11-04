package com.dhc.library.framework

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Bundle

import com.alibaba.android.arouter.launcher.ARouter
import com.dhc.library.base.BaseApplication
import com.dhc.library.data.IDataHelper
import com.dhc.library.di.component.AppComponent
import com.dhc.library.di.component.DaggerAppComponent
import com.dhc.library.di.component.DaggerAppComponent.*
import com.dhc.library.di.module.AppModule
import com.dhc.library.di.module.DataModule
import com.dhc.library.utils.AppContext
import com.dhc.library.utils.AppManager
import com.dhc.library.utils.AppUtil
import com.dhc.library.utils.AsLibUtil
import com.dhc.library.utils.sys.ScreenUtil

/**
 * @creator：denghc(desoce)
 * @updateTime： 2018/6/20 10:43
 * @description： Application delegate
 */
interface XAppDelegate {

    fun getAppComponent(): AppComponent

    fun getAppComponentBuilder(): DaggerAppComponent.Builder

    fun netConfig(netConfig: IDataHelper.NetConfig?): XAppDelegate

    fun onCreate()

    fun onTerminate()

    fun onConfigurationChanged(newConfig: Configuration)

    fun onLowMemory()

    fun onTrimMemory(level: Int)


    class DefaultAppDelegate( val application: Application) : XAppDelegate {

        private var builder: DaggerAppComponent.Builder? = null

        private var mNetConfig: IDataHelper.NetConfig? = null


        override fun getAppComponentBuilder(): DaggerAppComponent.Builder {
             builder = DaggerAppComponent.builder()
                    .dataModule(DataModule(mNetConfig))
                    .appModule(AppModule(this.application as BaseApplication))
            return builder!!
        }

        override fun getAppComponent(): AppComponent {
            if (builder == null)
                builder = getAppComponentBuilder()
            return builder!!.build()
        }

        override fun netConfig(netConfig: IDataHelper.NetConfig?): DefaultAppDelegate {
            mNetConfig = netConfig
            return this
        }

        override fun onCreate() {
            AppContext.init(application)       //保存appcotext的实例
            AppUtil.syncIsDebug(application.applicationContext)//判断是否是debug模式
            ScreenUtil.init(application)    // init tools
            if (AppUtil.isDebug()) {
                ARouter.openLog()     // 打印日志
                ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(application) // 尽可能早，推荐在Application中初始化
            application.registerActivityLifecycleCallbacks(SwitchBackgroundCallbacks())
        }

        override fun onTerminate() {
            AsLibUtil.onTerminate(application)
        }

        override fun onConfigurationChanged(newConfig: Configuration) {
            AsLibUtil.onConfigurationChanged(application, newConfig)
        }

        override fun onLowMemory() {
            AsLibUtil.onLowMemoryAsLibrary(application)
        }

        override fun onTrimMemory(level: Int) {
            AsLibUtil.onTrimMemoryAsLibrary(application, level)
        }

    }


    class SwitchBackgroundCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            AppManager.instance.addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            AppManager.instance.removeActivity(activity)
        }
    }
}
