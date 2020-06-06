package com.dhc.library.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.annotation.CallSuper
import androidx.annotation.CheckResult

import com.dhc.library.R
import com.dhc.library.framework.ISupportBaseActivity
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.RxLifecycleAndroid

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import me.yokeyword.fragmentation.SupportActivity

/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 11:59
 * @description: BaseActivity by no mvp
 */
abstract class BaseActivity : SupportActivity(), LifecycleProvider<ActivityEvent>, ISupportBaseActivity {

    /**
     * Rewrite RxLife to control the life cycle
     */
    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(ActivityEvent.CREATE)
        beforeInit(savedInstanceState)
        if (layoutId > 0) {
            setContentView(layoutId)
        }
        Log.i(TAG, "activity: " + javaClass.simpleName + " onCreate()")
        initEventAndData(savedInstanceState)
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
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
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

    /**------------------------             Rxlife start               ------------------------ */

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    companion object {

        private val TAG = BaseActivity::class.java.simpleName
    }

    /**------------------------             Rxlife    end          ------------------------ */
}
