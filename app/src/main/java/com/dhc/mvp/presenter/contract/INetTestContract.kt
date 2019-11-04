package com.dhc.mvp.presenter.contract

import com.dhc.library.framework.IBaseModel
import com.dhc.library.framework.IBasePresenter
import com.dhc.library.framework.IBaseView
import com.dhc.mvp.App.SampleApiResponse
import com.dhc.mvp.modle.bean.GankItemBean

import io.reactivex.Flowable

/**
 * @author
 * @createDate
 * @description
 */
interface INetTestContract {

    interface IView : IBaseView {

        fun success(data: List<GankItemBean>?)

        fun failure(code: String, msg: String?)
    }

    interface IPresenter {

        fun getRandomGirl()

    }

    interface IModel : IBaseModel {

        val randomGirl: Flowable<SampleApiResponse<List<GankItemBean>>>

    }

}