package com.dhc.library.framework


import com.dhc.library.data.IDataHelper

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 10:55
 * @description：
 */
interface ISupportApplication {


    fun getNetConfig(): IDataHelper.NetConfig?
}
