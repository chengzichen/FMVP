package com.dhc.library.data.net

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 13:50
 * @description:  network anomaly
 */
class NetError : Exception {
    var exception: Throwable? = null
    var type = NoConnectError
        private set

    constructor(exception: Throwable, type: String) {
        this.exception = exception
        this.type = type
    }

    constructor(detailMessage: String, type: String) : super(detailMessage) {
        this.type = type
    }


    override val message: String?
        get() {
            if (exception != null) {
                return if (exception!!.message == null) {
                    exception!!.toString()
                } else {
                    exception!!.message
                }
            }
            return if (super.message != null) {
                super.message
            } else {
                "未知错误"
            }
        }

    companion object {

        val ParseError = "0"   //数据解析异常
        val NoConnectError = "-1"   //无连接异常
        val AuthError = "-2"   //用户验证异常
        val NoDataError = "-3"   //无数据返回异常
        val BusinessError = "-4"   //业务异常
        val SocketError = "-6"   //连接超时异常
        val OtherError = "-5"   //其他异常
        val NetError = "-7"   //请求错误
    }
}
