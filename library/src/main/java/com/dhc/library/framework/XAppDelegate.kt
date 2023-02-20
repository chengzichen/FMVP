package com.dhc.library.framework

import android.app.Activity
import android.app.Application
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner

import com.alibaba.android.arouter.launcher.ARouter
import com.dhc.library.data.HttpHelper
import com.dhc.library.data.IDataHelper
import com.dhc.library.data.net.manager.NetworkStateReceive
import com.dhc.library.ext.KtxAppLifeObserver
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




    fun netConfig(netConfig: IDataHelper.NetConfig?): XAppDelegate

    fun onCreate()

    fun onTerminate()

    fun onConfigurationChanged(newConfig: Configuration)

    fun onLowMemory()

    fun onTrimMemory(level: Int)


    class DefaultAppDelegate(val application: Application) : XAppDelegate {

        private lateinit var mNetworkStateReceive: NetworkStateReceive

        private var mNetConfig: IDataHelper.NetConfig? = null


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
            HttpHelper.INSTANCE.initConfig(mNetConfig)
            //
            application.registerActivityLifecycleCallbacks(SwitchBackgroundCallbacks())
            //监听网络信号
            mNetworkStateReceive = NetworkStateReceive()
            application.registerReceiver(
                mNetworkStateReceive,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            //监听是否在后台运行
            ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
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

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }


        override fun onActivityDestroyed(activity: Activity) {
            AppManager.instance.removeActivity(activity)
        }
    }
}
