package com.dhc.library.data.net

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

import io.reactivex.Flowable
import io.reactivex.functions.Function
import retrofit2.HttpException

/**
 * 创建者：邓浩宸
 * 时间 ：2017/5/24 10:27
 * 描述 ：重连机制
 */
class RetryWhenHandler : Function<Flowable<out Throwable>, Flowable<*>> {

    private var mCount = 3
    private var mDelay: Long = 3 //s

    private var counter = 0

    constructor() {}

    constructor(count: Int) {
        this.mCount = count
    }

    constructor(count: Int, delay: Long) : this(count) {
        this.mCount = count
        this.mDelay = delay
    }


    @Throws(Exception::class)
    override fun apply(flowable: Flowable<out Throwable>): Flowable<*> {
        return flowable
                .flatMap<Any>(Function<Throwable, Flowable<*>> { throwable ->
                    if (counter < mCount && (throwable is UnknownHostException
                                    || throwable is SocketException
                                    || throwable is HttpException
                                    || throwable is SocketTimeoutException)) {
                        counter++
                        return@Function Flowable.timer(mDelay, TimeUnit.SECONDS)
                    } else if (counter < mCount && throwable is NullPointerException
                            && throwable.message != null && "token_is_need_refresh" == throwable.message) {
                        counter++
                        return@Function Flowable.timer(0, TimeUnit.SECONDS)
                    }
                    return@Function Flowable.error<Any>(throwable)
                })
    }
}
