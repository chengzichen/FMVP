package com.dhc.mvp.net

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

import com.dhc.library.base.BaseApplication
import com.dhc.library.base.XDaggerActivity
import com.dhc.library.utils.AppContext
import com.dhc.mvp.R
import com.dhc.mvp.dao.AppDatabase
import com.dhc.mvp.di.DiHelper
import com.dhc.mvp.modle.bean.GankItemBean
import com.dhc.mvp.presenter.NetTestPresenter
import com.dhc.mvp.presenter.RoomTestPresenter
import com.dhc.mvp.presenter.RxCacheTestPresenter
import com.dhc.mvp.presenter.contract.INetTestContract
import org.w3c.dom.Text

import io.reactivex.functions.Consumer

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午2:55
 * @description：使用 Room示例
 */
class RoomSampleActivity : XDaggerActivity<RoomTestPresenter, INetTestContract.IView>(), INetTestContract.IView {

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
        mPresenter!!.loadAllByIds(arrayOf(data[0]._id))
                .subscribe { gankItemBeans -> (`$`<View>(R.id.tv_database) as TextView).text = "数据库数据: " + gankItemBeans[0].toString() }
    }
}
