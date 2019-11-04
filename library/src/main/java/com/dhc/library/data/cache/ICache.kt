package com.dhc.library.data.cache

/**
 * Created by 邓浩宸 on 2016/11/27.
 */

interface ICache {
    /**
     * store key , value
     * @param key  must be String
     * @param value  Object
     */
    fun put(key: String, value: Any)

    /**
     * Get the value according to the key
     * @param key   String
     * @return  Object
     */
    operator fun get(key: String): Any?

    /**
     * delete data by key
     * @param key String
     */
    fun remove(key: String)

    /**
     * Contains the value
     * @param key  String
     * @return   Whether or not
     * Contains the value
     */
    operator fun contains(key: String): Boolean

    /**
     * Clean all
     */
    fun clear()

}
