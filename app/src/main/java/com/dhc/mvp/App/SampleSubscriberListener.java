package com.dhc.mvp.App;

import com.dhc.library.utils.rx.BaseSubscriberListener;

import retrofit2.HttpException;


/**
 * 创建者     邓浩宸
 * 创建时间   2018/4/23 10:29
 * 描述	        请求异常统一处理回调
 */
public abstract class SampleSubscriberListener<T>  extends BaseSubscriberListener<T> {
    //对应HTTP的状态码
    private static final int ERROR = 400;
    private static final int UNAUTHORIZED = 401;//没有权限
    private static final int FORBIDDEN = 403;//没有权限
    private static final int NOT_FOUND = 404;//
    private static final int INTERNAL_SERVER_ERROR = 500;//服务器错误
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;




    @Override
    public boolean isCheckReLogin(HttpException httpException) {
        return httpException.code() == UNAUTHORIZED;
    }
}
