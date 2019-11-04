package com.dhc.library.utils.string

import android.graphics.Paint
import android.text.TextUtils
import android.util.Log

import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by baixiaokang on 16/12/10.
 */

object BaseUtils {

    val oldWeekDays: List<String>
        get() {
            val c = Calendar.getInstance()
            val months = arrayOfNulls<String>(8)
            for (i in 0..7) {
                months[i] = SimpleDateFormat("MM.dd").format(Date(c
                        .timeInMillis))
                c.add(Calendar.DAY_OF_MONTH, -1)
            }
            return Arrays.asList<String>(*months)
        }

    fun getPaint(style: Paint.Style, color: Int): Paint {
        val mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = style
        mPaint.color = color
        mPaint.textSize = 30f
        return mPaint
    }

    /**
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    fun evaluate(fraction: Float, startValue: Any, endValue: Any): Int {
        val startInt = startValue as Int
        val startA = startInt shr 24 and 0xff
        val startR = startInt shr 16 and 0xff
        val startG = startInt shr 8 and 0xff
        val startB = startInt and 0xff
        val endInt = endValue as Int
        val endA = endInt shr 24 and 0xff
        val endR = endInt shr 16 and 0xff
        val endG = endInt shr 8 and 0xff
        val endB = endInt and 0xff
        return (startA + (fraction * (endA - startA)).toInt() shl 24
                or (startR + (fraction * (endR - startR)).toInt() shl 16)
                or (startG + (fraction * (endG - startG)).toInt() shl 8)
                or startB + (fraction * (endB - startB)).toInt())
    }

    /**
     * 验证手机格式
     */
    fun phoneVerification(phoneNum: String): Boolean {
        /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        val telRegex = "[1][3578]\\d{9}"//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(phoneNum))
            false
        else
            phoneNum.matches(telRegex.toRegex())
    }

    /**
     * 验证密码格式
     */
    fun pwVerification(pwString: String): Boolean {
        val PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$"//判断6-16字母数字组合
        //        String PATTERN="'/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/'";//判断6-16字母数字组合
        return if (TextUtils.isEmpty(pwString))
            false
        else
            pwString.matches(PATTERN.toRegex())
    }

    fun toURLEncoded(paramString: String?): String {
        if (paramString == null || paramString == "") {
            Log.e("URLEncoded", "toURLEncoded error:" + paramString!!)
            return ""
        }

        try {

            var str = String(paramString.toByteArray(),  Charset.forName("UTF-8"))
            str = URLEncoder.encode(str, "UTF-8")
            return str
        } catch (localException: Exception) {
            Log.e("URLEncoded", "toURLEncoded error:$paramString", localException)
        }

        return ""
    }

    /**
     * 通过正则表达式来判断。下面的例子只允许显示字母、数字和汉字。
     */
    fun stringFilter(str: String): Boolean {
        // 只允许字母、数字和汉字
        val regex = "^[a-zA-Z\u4E00-\u9FA5]+$"
        val pattern = Pattern.compile(regex)
        val match = pattern.matcher(str)
        return match.matches()
    }
}
