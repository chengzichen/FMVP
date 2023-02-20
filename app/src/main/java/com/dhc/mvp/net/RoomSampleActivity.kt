package com.dhc.mvp.net

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

import com.dhc.library.base.activity.BaseVmDbActivity
import com.dhc.library.base.viewmodel.parseState
import com.dhc.mvp.R
import com.dhc.mvp.databinding.ActivityRxcacheSampleBinding
import com.dhc.mvp.state.HomeViewModel
import com.dhc.mvp.viewmodel.RequestDbViewModel
import kotlinx.android.synthetic.main.activity_rxcache_sample.*
import kotlinx.android.synthetic.main.activity_rxcache_sample.tv_content
import kotlinx.android.synthetic.main.activity_rxcache_sample.tv_title

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午2:55
 * @description：使用 Room示例
 */
class RoomSampleActivity : BaseVmDbActivity<HomeViewModel, ActivityRxcacheSampleBinding>() {

    private val viewModel: RequestDbViewModel by viewModels()


    override fun createObserver() {
        viewModel.run {
            randomGirlData.observe(this@RoomSampleActivity, Observer {
                parseState(it, onSuccess = { data ->
                    tv_content!!.text = getString(R.string.data_f, data!![0].toString())
                    viewModel.loadAllByIds(arrayOf(data[0]._id))
                }, onError = { })
            })
            dbRandomGirlData.observe(this@RoomSampleActivity, Observer {
                parseState(it, { data -> tv_database!!.text = "数据库数据: " + data[0].toString() })
            })
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        tv_title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        viewModel!!.getRandomGirl()//调用方法请求接口
    }
}
