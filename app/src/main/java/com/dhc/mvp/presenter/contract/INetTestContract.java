package com.dhc.mvp.presenter.contract;

import com.dhc.library.framework.IBaseModel;
import com.dhc.library.framework.IBasePresenter;
import com.dhc.library.framework.IBaseView;
import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.modle.bean.GankItemBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author
 * @createDate
 * @description
 */
public interface INetTestContract {

    interface IView<T> extends IBaseView {

        void success(T data);

        void failure(String code, String msg);
    }

    interface IPresenter extends IBasePresenter<IView> {

        void getRandomGirl();

    }

    interface IModel extends IBaseModel {

        Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl();

    }

}