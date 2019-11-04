package com.dhc.library.utils.rx


import com.dhc.library.data.net.ApiResponse
import com.dhc.library.data.net.RetryWhenHandler

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * 创建者：邓浩宸
 * 时间 ：2016/11/15 16:01
 * 描述 ：Rxjava使用的工具类
 */
object RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
    </T> */
    @JvmStatic
    fun <T : ApiResponse<*>> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return rxSchedulerHelper(3, 3)
    }


    /**
     * 统一线程处理和失败重连
     * @param count   失败重连次数
     * @param delay  延迟时间
     * @param <T>      返回数据data实际的 数据
     * @return   返回数据data实际的 数据
    </T> */
    @JvmStatic
    fun <T : ApiResponse<*>> rxSchedulerHelper(count: Int, delay: Long): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .flatMap { t ->
                        //                                if (t != null && "40108".equals(t.getCode())) {
                        //                                    SPHelper.put(AppContext.get(), "access_token", t.getMessage());
                        //                                    throw new NullPointerException("token_is_need_refresh");
                        //                                } else {
                        //                                    return Flowable.just(t);
                        //                                }

                        // TODO: 2017/8/22 根据实际情况去处理
                        Flowable.just(t)
                    }
                    .retryWhen(RetryWhenHandler(count, delay))
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }


    //    /**
    //     * 统一返回结果处理
    //     * @param <T>
    //     * @return
    //     */
    //    public static <T> Observable.Transformer<MyHttpResponse<T>, T> handleMyResult() {   //compose判断结果
    //        return new Observable.Transformer<MyHttpResponse<T>, T>() {
    //            @Override
    //            public Observable<T> call(Observable<MyHttpResponse<T>> httpResponseObservable) {
    //                return httpResponseObservable.flatMap(new Func1<MyHttpResponse<T>, Observable<T>>() {
    //                    @Override
    //                    public Observable<T> call(MyHttpResponse<T> tMyHttpResponse) {
    //                        if(tMyHttpResponse.getCode() == 200) {
    //                            return createData(tMyHttpResponse.getData());
    //                        } else {
    //                            return Observable.error(new ApiException("服务器返回error"));
    //                        }
    //                    }
    //                });
    //            }
    //        };
    //    }

}
