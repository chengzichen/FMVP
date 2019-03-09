package com.dhc.mvp.net;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dhc.library.base.XDaggerActivity;
import com.dhc.mvp.R;
import com.dhc.mvp.di.DiHelper;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.RxCacheTestPresenter;
import com.dhc.mvp.presenter.contract.INetTestContract;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午2:55
 * @description：使用 RXCACHE示例
 */
public class RxJavaSampleActivity extends XDaggerActivity{

    private TextView title;
    private TextView content;
    CompositeDisposable mDisposable= new CompositeDisposable();
    @Override
    public int getLayoutId() {
        return R.layout.activity_rxcache_sample;
    }

    @Override
    public void initEventAndData(Bundle savedInstanceState) {
        title = $(R.id.tv_title);
        content = $(R.id.tv_content);
        title.setText("点我");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable.clear();
            }
        });
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResourceSubscriber resourceSubscriber = new ResourceSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        Log.d("11111", String.valueOf(aLong));
                        content.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };

                Flowable.interval(2000, 2000, TimeUnit.MILLISECONDS)
                        .compose(RxJavaSampleActivity.this
                                .<Long>bindLifecycle())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resourceSubscriber);

                mDisposable.add(resourceSubscriber);
            }
        });
    }


    @Override
    public void initInject(Bundle savedInstanceState) {

    }


}
