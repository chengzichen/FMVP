package com.dhc.library.data.net

import java.io.IOException
import java.lang.reflect.Type

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 13:50
 * @description:  请求网络返回String的Goson处理工厂
 */
class StringConverterFactory : Converter.Factory() {


    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (String::class.java == type) {
            Converter<ResponseBody, String> { value -> value.string() }
        } else null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (String::class.java == type) {
            Converter<String, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value) }
        } else null
    }

    companion object {

        private val MEDIA_TYPE = MediaType.parse("text/plain")
    }
}
