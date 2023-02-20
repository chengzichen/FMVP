package com.dhc.library.base.livedata

import androidx.lifecycle.MutableLiveData


/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的Short类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class ByteLiveData : MutableLiveData<Byte>() {
    override fun getValue(): Byte {
        return super.getValue() ?: 0
    }
}