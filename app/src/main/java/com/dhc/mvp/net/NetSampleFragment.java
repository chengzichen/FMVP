package com.dhc.mvp.net;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhc.library.base.XDaggerFragment;
import com.dhc.mvp.R;
import com.dhc.mvp.di.DiHelper;
import com.dhc.mvp.modle.bean.GankItemBean;
import com.dhc.mvp.presenter.NetTestPresenter;
import com.dhc.mvp.presenter.contract.INetTestContract;

import java.util.List;

import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午2:56
 * @description：TODO 请描述该类职责
 */
public class NetSampleFragment extends XDaggerFragment<NetTestPresenter> implements INetTestContract.IView<List<GankItemBean>>  {

    private TextView title;
    private TextView content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_net_sample;
    }

    @Override
    public void initEventAndData(Bundle savedInstanceState) {
        title=  $(R.id.tv_title);
        title.setText("Url : http://gank.io/api/random/data/福利/1");
        content=$(R.id.tv_content);
            mPresenter.getRandomGirl();
    }

    @Override
    public void initInject(Bundle savedInstanceState) {
        DiHelper.getFragmentComponent(getFragmentModule()).inject(this);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }

    @Override
    public void success(List<GankItemBean> data) {
        content.setText(getString(R.string.data_f,data.get(0).toString()));
    }

    @Override
    public void failure(String code, String msg) {

    }
}