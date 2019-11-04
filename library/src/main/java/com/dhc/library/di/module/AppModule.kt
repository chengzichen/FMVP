package com.dhc.library.di.module


import com.dhc.library.base.BaseApplication

import java.util.Random

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * 创建者：邓浩宸
 * 时间 ：2017/3/21 10:52
 * 描述 ：提供一些框架必须的实例的 [Module]
 */
@Module
class AppModule(private val application: BaseApplication) {

    @Provides
    @Singleton
    internal fun provideApplicationContext():
    //    @ContextLife("Application")
            BaseApplication {
        return application
    }


    @Provides
    @Singleton
    fun random(): Random {
        return Random()
    }
}
