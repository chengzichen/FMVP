package com.dhc.mvp.di.component;

import com.dhc.library.di.FragmentScope;
import com.dhc.library.di.component.AppComponent;
import com.dhc.library.di.module.FragmentModule;
import com.dhc.mvp.net.NetSampleActivity;
import com.dhc.mvp.net.NetSampleFragment;

import dagger.Component;


/**
 * @author:
 * @createDate :
 * @description :The annotation in Fragment, defines the scope of Activity, and the scope of dependent injection is Fragment
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    //TODO inject(IView)
    void  inject(NetSampleFragment netSampleFragment);
}
