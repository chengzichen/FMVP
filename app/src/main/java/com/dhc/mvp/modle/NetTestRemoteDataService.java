package com.dhc.mvp.modle;

import javax.inject.Inject;
import com.dhc.library.data.HttpHelper;
import com.dhc.library.utils.rx.RxUtil;
import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.contract.INetTestContract;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author
 * @createDate
 * @description
 */

public class NetTestRemoteDataService  implements INetTestContract.IModel {

    private HttpHelper mHttpHelper;

    @Inject
    public NetTestRemoteDataService(HttpHelper httpHelper) {
        this.mHttpHelper = httpHelper;
    }


    @Override
    public Flowable<SampleApiResponse<List<GankItemBean>>> getRandomGirl() {
        return mHttpHelper.createApi(Api.class).getRandomGirl(1).compose(RxUtil.<SampleApiResponse<List<GankItemBean>>>rxSchedulerHelper());
    }

}