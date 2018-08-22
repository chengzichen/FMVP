package com.dhc.mvp.di.component;


import com.dhc.library.di.ActivityScope;
import com.dhc.library.di.component.AppComponent;
import com.dhc.library.di.module.ActivityModule;
import com.dhc.mvp.net.NetSampleActivity;

import dagger.Component;

/**
 * @author:
 * @createDate :
 * @description :The annotations to the activity are used to restrict the scope of the Context and the scope of the dependency injection
 */
@ActivityScope
@Component(dependencies =AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //TODO inject(IView)
    void  inject(NetSampleActivity netSampleActivity);
}
