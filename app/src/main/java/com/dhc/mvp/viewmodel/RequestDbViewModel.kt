package com.dhc.mvp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dhc.library.base.viewmodel.BaseViewModel
import com.dhc.library.base.viewmodel.ResultState
import com.dhc.library.base.viewmodel.request
import com.dhc.library.base.viewmodel.requestNoCheck
import com.dhc.library.data.DBHelper
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.dao.AppDatabase
import com.dhc.mvp.modle.apiService
import com.dhc.mvp.modle.bean.GankItemBean
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class RequestDbViewModel : BaseViewModel() {

    var cacheApi = DBHelper.INSTANCE.getApi(AppDatabase::class.java, "gankitem")
    var randomGirlData: MutableLiveData<ResultState<List<GankItemBean>>> = MutableLiveData()
    var dbRandomGirlData: MutableLiveData<ResultState<List<GankItemBean>>> = MutableLiveData()


    fun getRandomGirl() {
        request({ randomInDB() }, randomGirlData)
    }

    suspend fun randomInDB() = runBlocking {
        val asyncData = async {
            apiService.getRandomGirl(1)
        }
        val data: SampleApiResponse<List<GankItemBean>> = asyncData.await()
        cacheApi.gankDao().insertAll(data.data[0])
        data
    }


    fun loadAllByIds(ids: Array<String>) {
        requestNoCheck({ cacheApi.gankDao().loadAllByIds(ids) }, dbRandomGirlData)
    }

}