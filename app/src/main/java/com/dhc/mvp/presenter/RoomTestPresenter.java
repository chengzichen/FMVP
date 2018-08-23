package com.dhc.mvp.presenter;

import com.dhc.library.base.XPresenter;
import com.dhc.library.data.DBHelper;
import com.dhc.library.data.net.NetError;
import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.App.SampleSubscriber;
import com.dhc.mvp.App.SampleSubscriberListener;
import com.dhc.mvp.dao.AppDatabase;
import com.dhc.mvp.modle.NetTestRemoteDataService;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.contract.INetTestContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author
 * @createDate
 * @description
 */

public class RoomTestPresenter extends XPresenter<INetTestContract.IView> implements INetTestContract.IPresenter {

    private NetTestRemoteDataService mNetTestRemoteDataService;
    private AppDatabase cacheApi;

    @Inject
    public RoomTestPresenter(NetTestRemoteDataService NetTestRemoteDataService, DBHelper dbHelper) {
        mNetTestRemoteDataService = NetTestRemoteDataService;
        cacheApi= dbHelper.getApi(AppDatabase
                .class,"gankitem");
    }


    @Override
    public void getRandomGirl() {
        mNetTestRemoteDataService.getRandomGirl()
                .compose(getV().<SampleApiResponse<List<GankItemBean>>>bindLifecycle())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<SampleApiResponse<List<GankItemBean>>>() {
                    @Override
                    public void accept(SampleApiResponse<List<GankItemBean>> listSampleApiResponse) throws Exception {
                        cacheApi.gankDao().insertAll(listSampleApiResponse.getData().get(0));
                    }
                })
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


    public Flowable<List<GankItemBean>> loadAllByIds(String[] ids){
      return cacheApi.gankDao().loadAllByIds(ids).subscribeOn(Schedulers.io());
    }
}