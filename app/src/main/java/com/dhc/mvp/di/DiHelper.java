package com.dhc.mvp.di;


import com.dhc.mvp.di.component.DaggerActivityComponent;
import com.dhc.mvp.di.component.DaggerFragmentComponent;
import com.dhc.mvp.di.component.ActivityComponent;
import com.dhc.mvp.di.component.FragmentComponent;
import com.dhc.library.base.BaseApplication;
import com.dhc.library.di.module.ActivityModule;
import com.dhc.library.di.module.FragmentModule;
import com.dhc.library.utils.AppContext;

/**
 * @author:   邓浩宸
 * @createDate :
 * @description :  helper for inject
 */
public class DiHelper {

        public static ActivityComponent getActivityComponent(ActivityModule activityModule) {
            return DaggerActivityComponent.builder()
                    .appComponent(((BaseApplication) AppContext.get()).getAppComponent())
                    .activityModule(activityModule)
                    .build();
        }


    public static FragmentComponent getFragmentComponent(FragmentModule fragmentModule){
            return DaggerFragmentComponent.builder()
                    .appComponent(((BaseApplication)AppContext.get()).getAppComponent())
                    .fragmentModule(fragmentModule)
                    .build();
        }


}
