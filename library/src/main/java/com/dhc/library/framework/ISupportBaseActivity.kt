package com.dhc.library.framework

import android.os.Bundle

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/7/30$ 12:52$
 * @description： BaseActivity support
 */
interface ISupportBaseActivity : ISupportBaseView {

    /**
     * Reload this Activity  (NoAnim)
     */
    fun reload()

    /**
     * Reload this Activity
     *
     * @param isNeedAnim IsNeed animation for reload
     */
    fun reload(isNeedAnim: Boolean)


}
