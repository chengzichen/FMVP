package com.dhc.library.data.cache

import android.app.Activity
import android.text.TextUtils

import androidx.collection.LruCache
import com.dhc.library.utils.AppManager
import java.util.*

/**
 * Created by 邓浩宸 on 2016/11/28.
 */

class MemoryCache private constructor() : ICache {

    private val cache: LruCache<String, Any>

    init {
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        val cacheSize = maxMemory / 8
        cache = LruCache(cacheSize)

    }


    @Synchronized
    override fun put(key: String, value: Any) {
        if (TextUtils.isEmpty(key)) return

        if (cache.get(key) != null) {
            cache.remove(key)
        }
        cache.put(key, value)
    }

    override fun get(key: String): Any? {
        return cache.get(key)
    }

    @Synchronized
    operator fun <T> get(key: String, clazz: Class<T>): T? {
        try {
            return cache.get(key) as T?
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun remove(key: String) {
        if (cache.get(key) != null) {
            cache.remove(key)
        }
    }

    override fun contains(key: String): Boolean {
        return key != null && cache.get(key) != null
    }

    override fun clear() {
        cache.evictAll()
    }

    companion object {
        val instance: MemoryCache by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MemoryCache()
        }
    }
}
