package com.dhc.library.utils

import android.app.Application
import android.content.res.Configuration

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Created by 邓浩宸 on 17/2/27.
 * 作为接口，方便主工程调度子模块的声明周期
 */

interface ApplicationLike : IProvider {

    fun onTerminate(application: Application)

    fun onCreateAsLibrary(application: Application)

    fun onLowMemoryAsLibrary(application: Application)

    fun onTrimMemoryAsLibrary(application: Application, level: Int)

    fun onConfigurationChanged(application: Application, configuration: Configuration)

}
