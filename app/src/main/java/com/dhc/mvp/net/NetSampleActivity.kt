package com.dhc.mvp.net

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

import com.dhc.library.base.activity.BaseVmDbActivity
import com.dhc.library.base.viewmodel.parseState
import com.dhc.mvp.R
import com.dhc.mvp.databinding.ActivityNetSampleBinding
import com.dhc.mvp.state.HomeViewModel
import com.dhc.mvp.viewmodel.RequestProjectViewModel
import kotlinx.android.synthetic.main.activity_net_sample.*

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:55
 * @description：TODO 请描述该类职责
 */
class NetSampleActivity : BaseVmDbActivity<HomeViewModel, ActivityNetSampleBinding>() {


    private var isFrist: Boolean = false
    private val viewModel: RequestProjectViewModel by viewModels()


    override fun createObserver() {
        viewModel.run {
            randomGirlData.observe(this@NetSampleActivity, Observer {
                parseState(it,
                    onSuccess = { data ->
                        tv_content!!.text = getString(R.string.data_f, data!![0].toString())
                    },
                    onError = {

                    })
            })
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        bt_fragment.setOnClickListener {
            if (!isFrist) {
                loadRootFragment(R.id.fl_content, NetSampleFragment(), true, true)
                isFrist = true
            }
        }
        tv_title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        viewModel!!.getRandomGirl()//调用方法请求接口
    }

}



