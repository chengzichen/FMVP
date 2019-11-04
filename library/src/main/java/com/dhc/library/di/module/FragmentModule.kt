package com.dhc.library.di.module

import android.app.Activity

import androidx.fragment.app.Fragment

import com.dhc.library.di.FragmentScope

import dagger.Module
import dagger.Provides


/**
 * 创建者：邓浩宸
 * 时间 ：2017/3/21 10:53
 * 描述 ：TODO 请描述该类职责
 */
@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    fun provideActivity(): Activity? {
        return fragment.activity
    }
}
