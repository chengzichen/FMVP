package com.dhc.library.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.annotation.CallSuper
import androidx.annotation.CheckResult

import com.dhc.library.R
import com.dhc.library.framework.ISupportBaseActivity
import me.yokeyword.fragmentation.SupportActivity

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 11:59
 * @description: BaseActivity by no mvp
 */
abstract class BaseActivity : SupportActivity(), ISupportBaseActivity {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit(savedInstanceState)
        onBaseSetContentView()
        Log.i(TAG, "activity: " + javaClass.simpleName + " onCreate()")
        initEventAndData(savedInstanceState)
    }

   open fun onBaseSetContentView(){
        if (layoutId > 0) {
            setContentView(layoutId)
        }
    }


    /**
     * replace  findViewById
     *
     * @param resId   layout resId
     * @param <T>   View
     * @return    View
    </T> */
    override fun <T : View> `$`(resId: Int): T {
        return super.findViewById<View>(resId) as T
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "activity: " + javaClass.simpleName + " onDestroy()")
    }

    /**
     * Reload this Activity  (NoAnim)
     */
    override fun reload() {
        reload(false)
    }


    /**
     * Reload this Activity
     *
     * @param isNeedAnim    IsNeed animation for reload
     */
    override fun reload(isNeedAnim: Boolean) {
        if (isNeedAnim) {
            window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
            recreate()
        } else {
            val intent = intent
            overridePendingTransition(0, 0)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            overridePendingTransition(R.anim.fade_in, 0)
            startActivity(intent)
        }
    }

    /**
     * Please try to override this method to avoid copying onBackPress(),
     * To ensure that the onBackPressedSupport() rewind event in the SupportFragment is executed normally
     */
    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
    }


   open fun beforeInit(savedInstanceState: Bundle?) {

    }



    companion object {

        private val TAG = BaseActivity::class.java.simpleName
    }

}
