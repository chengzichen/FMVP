package com.dhc.library.framework


import com.dhc.library.data.IDataHelper
import com.dhc.library.di.component.AppComponent
import com.dhc.library.di.component.DaggerAppComponent

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 10:55
 * @description：
 */
interface ISupportApplication {


    fun getNetConfig(): IDataHelper.NetConfig?
    fun getAppComponentBuilder(): DaggerAppComponent.Builder?
    fun getAppComponent(): AppComponent?
}
