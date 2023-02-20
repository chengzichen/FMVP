package com.dhc.mvp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dhc.library.base.viewmodel.BaseViewModel
import com.dhc.library.base.viewmodel.ResultState
import com.dhc.library.base.viewmodel.request
import com.dhc.mvp.modle.apiService
import com.dhc.mvp.modle.bean.GankItemBean

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class RequestProjectViewModel : BaseViewModel() {


    var randomGirlData: MutableLiveData<ResultState<List<GankItemBean>>> = MutableLiveData()


    fun getRandomGirl() {
        request({ apiService.getRandomGirl(1) }, randomGirlData)
    }

}