package com.dhc.library.framework;


import com.dhc.library.data.IDataHelper;
import com.dhc.library.di.component.AppComponent;
import com.dhc.library.di.component.DaggerAppComponent;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30 10:55
 * @description：
 */
public interface ISupportApplication {


     AppComponent getAppComponent();

     DaggerAppComponent.Builder getAppComponentBuilder();

     IDataHelper.NetConfig getNetConfig();


}
