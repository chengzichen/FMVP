package com.dhc.mvp.net

import android.os.Bundle
import android.widget.TextView

import com.dhc.library.base.XDaggerActivity
import com.dhc.mvp.R
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.NetTestPresenter
import com.dhc.mvp.presenter.RxCacheTestPresenter
import com.dhc.mvp.presenter.contract.INetTestContract

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午2:55
 * @description：使用 RXCACHE示例
 */
class RxCacheSampleActivity : XDaggerActivity<RxCacheTestPresenter, INetTestContract.IView>(), INetTestContract.IView {

    private var title: TextView? = null
    private var content: TextView? = null

    override val layoutId: Int
        get() = R.layout.activity_rxcache_sample

    override fun initEventAndData(savedInstanceState: Bundle?) {
        title = `$`(R.id.tv_title)
        title!!.text = "Url : http://gank.io/api/random/data/福利/1"
        content = `$`(R.id.tv_content)
        mPresenter!!.getRandomGirl()//调用方法请求接口
    }

    override fun initInject(savedInstanceState: Bundle?) {
        DiHelper.getActivityComponent(activityModule).inject(this)
    }


    override fun failure(code: String, msg: String?) {

    }

    override fun success(data: List<GankItemBean>?) {
        content!!.text = getString(R.string.data_f, data!![0].toString())
    }
}
