package com.dhc.mvp.net

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.dhc.library.base.activity.BaseVmDbActivity
import com.dhc.library.base.fragment.BaseVmDbFragment
import com.dhc.library.base.viewmodel.BaseViewModel
import com.dhc.library.base.viewmodel.parseState
import com.dhc.mvp.R
import com.dhc.mvp.databinding.FragmentNetSampleBinding
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.NetTestPresenter
import com.dhc.mvp.viewmodel.RequestProjectViewModel
import kotlinx.android.synthetic.main.activity_net_sample.*

import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:56
 * @description：TODO 请描述该类职责
 */
class NetSampleFragment : BaseVmDbFragment<BaseViewModel, FragmentNetSampleBinding>() {


    private val viewModel: RequestProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultVerticalAnimator()
    }

    override fun createObserver() {
        viewModel.run {
            randomGirlData.observe(viewLifecycleOwner, Observer {
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
        tv_title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        viewModel!!.getRandomGirl()
    }
}