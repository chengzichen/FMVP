package com.dhc.mvp.net

import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.dhc.library.base.XDaggerActivity
import com.dhc.library.framework.IBaseView
import com.dhc.mvp.R
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.NetTestPresenter
import com.dhc.mvp.presenter.contract.INetTestContract

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:55
 * @description：TODO 请描述该类职责
 */
class NetSampleActivity : XDaggerActivity<NetTestPresenter, INetTestContract.IView>(), INetTestContract.IView {


    private var isFrist: Boolean = false
    private var title: TextView? = null
    private var content: TextView? = null

    override val layoutId: Int
        get() = R.layout.activity_net_sample

    override fun initEventAndData(savedInstanceState: Bundle?) {

        initView()

        mPresenter!!.getRandomGirl()//调用方法请求接口
    }

    private fun initView() {
        `$`<View>(R.id.bt_fragment).setOnClickListener {
            if (!isFrist) {
                loadRootFragment(R.id.fl_content, NetSampleFragment(), true, true)
                isFrist = true
            }
        }
        title = `$`(R.id.tv_title)
        title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        content = `$`(R.id.tv_content)
    }

    override fun initInject(savedInstanceState: Bundle?) {
        DiHelper.getActivityComponent(activityModule).inject(this)
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }
    override fun success(data: List<GankItemBean>?) {
        content!!.text = getString(R.string.data_f, data!![0].toString())
    }

    override fun failure(code: String, msg: String?) {

    }
}
