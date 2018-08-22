package com.dhc.mvp.net;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhc.library.base.XDaggerActivity;
import com.dhc.mvp.R;
import com.dhc.mvp.di.DiHelper;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.NetTestPresenter;
import com.dhc.mvp.presenter.contract.INetTestContract;

import java.util.List;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:55
 * @description：TODO 请描述该类职责
 */
public class NetSampleActivity extends XDaggerActivity<NetTestPresenter> implements INetTestContract.IView<List<GankItemBean>> {

    private boolean isFrist;
    private TextView title;
    private TextView content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_net_sample;
    }

    @Override
    public void initEventAndData(Bundle savedInstanceState) {

        initView();

        mPresenter.getRandomGirl();//调用方法请求接口
    }

    private void initView() {
        $(R.id.bt_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFrist) {
                    loadRootFragment(R.id.fl_content, new NetSampleFragment(), true, true);
                    isFrist = true;
                }
            }
        });
        title=  $(R.id.tv_title);
        title.setText("Url : http://gank.io/api/random/data/福利/1");
        content=$(R.id.tv_content);
    }

    @Override
    public void initInject(Bundle savedInstanceState) {
        DiHelper.getActivityComponent(getActivityModule()).inject(this);
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public void success(List<GankItemBean> data) {
        content.setText(getString(R.string.data_f,data.get(0).toString()));
    }

    @Override
    public void failure(String code, String msg) {

    }
}
