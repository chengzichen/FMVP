package com.dhc.mvp.App;

import com.dhc.library.base.BaseApplication;
import com.dhc.library.data.IDataHelper;

import org.jetbrains.annotations.Nullable;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/14 下午1:59
 * @description： APP
 */
public class SampleApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IDataHelper.NetConfig getNetConfig() {
        return new IDataHelper.NetConfig().configBaseURL("http://gank.io/api/");
    }
}
