package com.dhc.mvp.presenter;

import com.dhc.library.base.XPresenter;
import com.dhc.library.data.net.NetError;
import com.dhc.mvp.App.SampleApiResponse;
import com.dhc.mvp.App.SampleSubscriber;
import com.dhc.mvp.App.SampleSubscriberListener;
import com.dhc.mvp.modle.CacheApi;
import com.dhc.mvp.modle.NetTestRemoteDataService;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.contract.INetTestContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.rx_cache2.internal.RxCache;

/**
 * @author
 * @createDate
 * @description
 */

public class RxCacheTestPresenter extends XPresenter<INetTestContract.IView> implements INetTestContract.IPresenter {

    private NetTestRemoteDataService mNetTestRemoteDataService;
    private CacheApi cacheApi;

    @Inject
    public RxCacheTestPresenter(NetTestRemoteDataService NetTestRemoteDataService, RxCache rxCache) {
        mNetTestRemoteDataService = NetTestRemoteDataService;
        cacheApi = rxCache.using(CacheApi.class);
    }


    @Override
    public void getRandomGirl() {
        cacheApi.getRandomGirl(mNetTestRemoteDataService.getRandomGirl())
                .compose(getV().<SampleApiResponse<List<GankItemBean>>>bindLifecycle())
                .flatMap(new Function<SampleApiResponse<List<GankItemBean>>,
                        Flowable<SampleApiResponse<List<GankItemBean>>>>() {//解决BUGhttps://github.com/VictorAlbertos/RxCache/issues/73
                    @Override
                    public Flowable<SampleApiResponse<List<GankItemBean>>> apply(SampleApiResponse<List<GankItemBean>> listSampleApiResponse) throws Exception {
                        Object data = listSampleApiResponse.getData();
                        if (data != null) {
                            Gson gson = new GsonBuilder().create();
                            String s = gson.toJson(data);
                            Type type = new TypeToken<List<GankItemBean>>() {}.getType();
                            List<GankItemBean> dataCopy = gson.fromJson(s, type);

                            listSampleApiResponse.setData(dataCopy);
                        }
                        return Flowable.just(listSampleApiResponse);
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
}