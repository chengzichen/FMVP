package com.dhc.library.base

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

import com.dhc.library.framework.ISupportBaseFragment



/**
 * @creator:denghc(desoce)
 * @updateTime:2018/7/30 12:02
 * @description: BaseFragment by no mvp
 */
abstract class BaseFragment : Fragment(),  ISupportBaseFragment {

    protected var mRootView: View? = null

    protected var mContext: Context? = null



    /**
     * The activity is onAttach to the Fragment
     */
    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }


   open fun beforeInit(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {

    }
    /**
     * get Handler for thread scheduling
     *
     * @return
     */
    fun getHandler(): Handler {
        return handler
    }

    /**
     * replace  findViewById
     *
     * @param resId   layout resId
     * @param <T>   View
     * @return    View
    </T> */
    override fun <T : View> `$`(resId: Int): T {
        return mRootView!!.findViewById<View>(resId) as T
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        beforeInit(inflater, container, savedInstanceState)
        return getRootView(inflater,container,savedInstanceState)
    }

    open fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = layoutId
        if (layoutId > 0)
            mRootView = inflater.inflate(layoutId, null)
        return mRootView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!useLazy()) {
            initEventAndData(savedInstanceState)
        }
        Log.i(TAG, this.javaClass.name + "onViewCreated")
    }


    /**
     * useLazy
     * @return
     */
    open fun useLazy(): Boolean {
        return true
    }

    /**
     * Delayed popup keyboard
     *
     * @param focus Keyboard focus view
     */
    protected fun showKeyboardDelayed(focus: View?) {
        focus?.requestFocus()
        getHandler().postDelayed({
            if (focus == null || focus.isFocused) {
                showKeyboard(true)
            }
        }, 300)
    }

    /**
     * Pop up or close the keyboard
     * @param isShow  Pop up or close
     */
    protected fun showKeyboard(isShow: Boolean) {
        val activity = activity ?: return

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return

        if (isShow) {
            if (activity.currentFocus == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            } else {
                imm.showSoftInput(activity.currentFocus, 0)
            }
        } else {
            if (activity.currentFocus != null) {
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
            }

        }
    }

    /**
     * When the Fragment state changes
     *
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }






    companion object {

        internal val handler = Handler()

        private val TAG = BaseFragment::class.java.simpleName
    }

}
