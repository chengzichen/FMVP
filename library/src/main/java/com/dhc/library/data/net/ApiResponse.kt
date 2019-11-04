package com.dhc.library.data.net

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 13:42
 * @description: A wrapper class that responds to the results.
 */
interface ApiResponse<D> {

    /**
     * isSuccess and   Data of type
     *
     * @return
     */
    val isSuccess: Boolean

    /**
     * Data
     *
     * @return
     */
    val data: D

    /**
     * is authentication failure
     *
     * @return
     */
    fun checkReLogin(): Boolean
}
