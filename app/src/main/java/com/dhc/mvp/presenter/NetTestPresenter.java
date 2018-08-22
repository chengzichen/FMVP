package com.dhc.mvp.presenter;

import android.graphics.Bitmap;

import com.dhc.library.data.net.NetError;
import com.dhc.library.utils.AppContext;
import com.dhc.library.utils.sys.ScreenUtil;
import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.App.SampleSubscriber;
import com.dhc.mvp.App.SampleSubscriberListener;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.contract.INetTestContract;
import com.dhc.mvp.modle.NetTestRemoteDataService;
import javax.inject.Inject;
import com.dhc.library.base.XPresenter;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author
 * @createDate
 * @description
 */

public class NetTestPresenter extends XPresenter<INetTestContract.IView> implements INetTestContract.IPresenter {

    private NetTestRemoteDataService mNetTestRemoteDataService;

    @Inject
    public NetTestPresenter(NetTestRemoteDataService NetTestRemoteDataService) {
        mNetTestRemoteDataService = NetTestRemoteDataService;
    }


    @Override
    public void getRandomGirl() {
        mNetTestRemoteDataService.getRandomGirl()
                .compose(getV().<SampleApiResponse<List<GankItemBean>>>bindLifecycle())
                .subscribe(new SampleSubscriber<SampleApiResponse<List<GankItemBean>>>(new SampleSubscriberListener<List<GankItemBean>>() {
                    @Override
                    public void onSuccess(List<GankItemBean> response) {
                        getV().success(response);
                    }

                    @Override
                    public void onFail(NetError errorMsg) {
                        super.onFail(errorMsg);
                        getV().failure("-1", errorMsg.getMessage());
                    }
                }));
    }
}