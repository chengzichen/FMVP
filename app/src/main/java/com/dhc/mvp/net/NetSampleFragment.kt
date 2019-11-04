package com.dhc.mvp.net

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.dhc.library.base.XDaggerFragment
import com.dhc.mvp.R
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.NetTestPresenter
import com.dhc.mvp.presenter.contract.INetTestContract

import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:56
 * @description：TODO 请描述该类职责
 */
class NetSampleFragment : XDaggerFragment<NetTestPresenter, INetTestContract.IView>(), INetTestContract.IView {


    private var title: TextView? = null
    private var content: TextView? = null


    override val layoutId: Int
        get() = R.layout.fragment_net_sample

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initEventAndData(savedInstanceState: Bundle?) {
        title = `$`(R.id.tv_title)
        title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        content = `$`(R.id.tv_content)
        mPresenter!!.getRandomGirl()
    }

    override fun initInject(savedInstanceState: Bundle?) {
        DiHelper.getFragmentComponent(fragmentModule).inject(this)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultVerticalAnimator()
    }

    override fun success(data: List<GankItemBean>?) {
        content!!.text = getString(R.string.data_f, data?.get(0).toString())
    }
    override fun failure(code: String, msg: String?) {

    }
}