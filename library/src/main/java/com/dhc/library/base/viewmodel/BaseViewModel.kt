package com.dhc.library.base.viewmodel

import androidx.lifecycle.ViewModel
import com.dhc.library.base.livedata.event.EventLiveData

/**
 * @author： denghc(desoce)
 * @updateTime： 2023/2/17 08:58
 * @description：主要用于获取数据,ViewModel的基类 使用ViewModel类，放弃AndroidViewModel，原因：用处不大
 * 完全有其他方式获取Application上下文
 */
open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套才加的，不然我加他个鸡儿加
     */
    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { EventLiveData<String>() }
        //隐藏
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

}