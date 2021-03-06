package com.dhc.library.base

import android.util.Log

import com.dhc.library.data.net.ApiResponse
import com.dhc.library.data.net.NetError
import com.dhc.library.framework.ProgressCancelListener
import com.dhc.library.data.net.SubscriberListener
import io.reactivex.internal.util.HalfSerializer.onComplete

import io.reactivex.subscribers.ResourceSubscriber


/**
 * 创建者：邓浩宸
 * 时间 ：2017/6/20 12:16
 * 描述 ：用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
open class BaseSubscriber<R : ApiResponse<D>,D>
/**
 * 使用该构造方法没有LoadingDialog
 *
 * @param mSubscriberOnNextListener
 */
(protected var mSubscriberOnNextListener: SubscriberListener<D>?) : ResourceSubscriber<R>(),
        ProgressCancelListener {

    override fun onStart() {
        super.onStart()
        if (mSubscriberOnNextListener != null && mSubscriberOnNextListener!!.isShowLoading)
            onBegin()
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    fun onBegin() {
        Log.i(TAG, "onBegin")
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener!!.onBegin()
        }
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    override fun onError(e: Throwable) {
        Log.i(TAG, "onError:$e")
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener!!.onError(e)
        }
        onComplete()
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    override fun onComplete() {
        Log.i(TAG, "onCompleted")
        if (mSubscriberOnNextListener != null && mSubscriberOnNextListener!!.isShowLoading)
            if (mSubscriberOnNextListener != null) {
                mSubscriberOnNextListener!!.onCompleted()
            }
        if (!this.isDisposed) {
            this.dispose()
        }
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理,可以根据实际情况再封装
     *
     * @param response 创建Subscriber时的泛型类型
     */
    override fun onNext(response: R?) {
        Log.i(TAG, "onNext")
        if (mSubscriberOnNextListener != null ) {
            if (response != null&& response.isSuccess) {
                mSubscriberOnNextListener!!.onSuccess(response.data)
            } else {
                mSubscriberOnNextListener!!.onFail(NetError("数据为空", NetError.NoDataError))
            }
        }
    }


    override fun onCancelProgress() {
        if (isDisposed)
            this.dispose()
    }

    companion object {

        private val TAG = "BaseSubscriber"
    }
}