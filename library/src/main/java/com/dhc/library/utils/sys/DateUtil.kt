package com.dhc.library.utils.sys

import android.text.TextUtils

import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * 创建者：zp
 * 时间 ：2017/3/10 0010 下午 4:07
 * 描述 ：日期操作工具类.
 */

object DateUtil {

    /**
     * 英文简写如：2016
     */
    var FORMAT_Y = "yyyy"

    /**
     * 英文简写如：12:01
     */
    var FORMAT_HM = "HH:mm"

    /**
     * 英文简写如：1-12 12:01
     */
    var FORMAT_MDHM = "MM-dd HH:mm"

    /**
     * 英文简写（默认）如：2016-12-01
     */
    var FORMAT_YMD = "yyyy-MM-dd"

    /**
     * 英文全称  如：2016-12-01 23:15
     */
    var FORMAT_YMDHM = "yyyy-MM-dd HH:mm"

    /**
     * 英文全称  如：2016-12-01 23:15:06
     */
    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    var datePattern = "yyyy-MM-dd HH:mm:ss"

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S"

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    var FORMAT_FULL_SN = "yyyyMMddHHmmssS"

    /**
     * 中文简写  如：2016年12月01日
     */
    var FORMAT_YMD_CN = "yyyy年MM月dd日"

    /**
     * 中文简写  如：2016年12月01日  12时
     */
    var FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时"

    /**
     * 中文简写  如：2016年12月01日  12时12分
     */
    var FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分"

    /**
     * 中文全称  如：2016年12月01日  23时15分06秒
     */
    var FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒"

    /**
     * 精确到毫秒的完整中文时间
     */
    var FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒"

    var calendar: Calendar? = null
    private val FORMAT = "yyyy-MM-dd HH:mm:ss"


    val curDateStr: String
        get() {
            val c = Calendar.getInstance()
            c.time = Date()
            return c.get(Calendar.YEAR).toString() + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
                    c.get(Calendar.DAY_OF_MONTH) + "-" +
                    c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) +
                    ":" + c.get(Calendar.SECOND)
        }


    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    val timeString: String
        get() {
            val df = SimpleDateFormat(FORMAT_FULL)
            val calendar = Calendar.getInstance()
            return df.format(calendar.time)

        }

    /*获取星期几*/
    val week: String
        get() {
            val cal = Calendar.getInstance()
            val i = cal.get(Calendar.DAY_OF_WEEK)
            when (i) {
                1 -> return "星期日"
                2 -> return "星期一"
                3 -> return "星期二"
                4 -> return "星期三"
                5 -> return "星期四"
                6 -> return "星期五"
                7 -> return "星期六"
                else -> return ""
            }
        }


    val amOrPm: String
        get() {
            val time = System.currentTimeMillis()
            calendar = Calendar.getInstance()
            calendar!!.timeInMillis = time

            val apm = calendar!!.get(Calendar.AM_PM)
            return if (apm == 1) {
                "下午"
            } else {
                "上午"
            }
        }


    @JvmOverloads
    fun str2Date(str: String?, format: String? = null): Date? {
        var format = format
        if (str == null || str.length == 0) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT
        }
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat(format)
            date = sdf.parse(str)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }


    @JvmOverloads
    fun str2Calendar(str: String, format: String? = null): Calendar? {

        val date = str2Date(str, format) ?: return null
        val c = Calendar.getInstance()
        c.time = date

        return c
    }


    @JvmOverloads
    fun date2Str(c: Calendar?, format: String? = null): String? {
        return if (c == null) {
            null
        } else date2Str(c.time, format)
    }


    @JvmOverloads
    fun date2Str(d: Date?, format: String? = null): String? {// yyyy-MM-dd HH:mm:ss
        var format = format
        if (d == null) {
            return null
        }
        if (format == null || format.length == 0) {
            format = FORMAT
        }
        val sdf = SimpleDateFormat(format)
        return sdf.format(d)
    }


    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    fun getCurDateStr(format: String): String? {
        val c = Calendar.getInstance()
        return date2Str(c, format)
    }


    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    fun strToDate(strDate: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val pos = ParsePosition(0)
        return formatter.parse(strDate, pos)
    }

    /**
     * @param time 当前的时间
     * @return 格式到秒
     */

    fun getMillon(time: Long): String {

        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time)

    }

    /**
     * @param time 当前的时间
     * @return 格式到秒
     */

    fun getMillon(time: Long, format: String): String {
        return SimpleDateFormat(format).format(time)

    }


    /**
     * @param time 当前的时间
     * @return 当前的天
     */
    fun getDay(time: Long): String {

        return SimpleDateFormat("yyyy-MM-dd").format(time)

    }


    /**
     * @param time 时间
     * @return 返回一个毫秒
     */
    // 格式到毫秒
    fun getSMillon(time: Long): String {

        return SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time)

    }


    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    fun addMonth(date: Date, n: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, n)
        return cal.time

    }


    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    fun addDay(date: Date, n: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, n)
        return cal.time

    }


    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    fun getNextHour(format: String, h: Int): String {
        val sdf = SimpleDateFormat(format)
        val date = Date()
        date.time = date.time + h * 60 * 60 * 1000
        return sdf.format(date)

    }

    /**
     * 字符串转时间戳
     */
    fun getTimeString(time: String): Long {
        val df = SimpleDateFormat(FORMAT_YMD)
        try {
            return df.parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * 功能描述：返回年
     *
     * @param date Date 日期
     * @return 返回年数
     */
    fun getYear(date: Date): Int {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.get(Calendar.YEAR)
    }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    fun getMonth(date: Date): Int {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.get(Calendar.MONTH) + 1
    }


    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    fun getDay(date: Date): Int {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.get(Calendar.DAY_OF_MONTH)
    }


    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    fun getHour(date: Date): Int {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.get(Calendar.HOUR_OF_DAY)
    }


    fun getFormatYear(data: String): String? {
        val df = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = df.parse(data)
            return df.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */

    fun getMinute(date: Date): Int {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.get(Calendar.MINUTE)
    }


    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    fun getSecond(date: Date): Int {
        calendar = Calendar.getInstance()

        calendar!!.time = date
        return calendar!!.get(Calendar.SECOND)
    }


    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    fun getMillis(date: Date?): Long {
        calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar!!.timeInMillis
    }


    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    fun countDays(date: String): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        c.time = parse(date)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24

    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    @JvmOverloads
    fun parse(strDate: String, pattern: String = datePattern): Date? {
        val df = SimpleDateFormat(pattern)
        try {
            return df.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }


    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    fun countDays(date: String, format: String): Int {
        val t = Calendar.getInstance().time.time
        val c = Calendar.getInstance()
        c.time = parse(date, format)
        val t1 = c.time.time
        return (t / 1000 - t1 / 1000).toInt() / 3600 / 24
    }


    /**
     * 将毫秒转化为分钟与秒数
     *
     * @param duration 音乐时长
     * @return
     */
    fun timeParse(duration: Long): String {
        var time = ""
        val minute = duration / 60000
        val seconds = duration % 60000
        val second = Math.round(seconds.toFloat() / 1000).toLong()
        if (minute < 10) {
            time += "0"
        }
        time += minute.toString() + "分"
        if (second < 10) {
            time += "0"
        }
        time += second.toString() + "秒"
        return time
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    fun getWeek(sdate: String): String {
        // 再转换为时间
        val date = DateUtil.parse(sdate)
        val c = Calendar.getInstance()
        c.time = date
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return SimpleDateFormat("EEEE").format(c.time)
    }

    fun getWeekStr(sdate: String): String {
        var str = ""
        str = DateUtil.getWeek(sdate)
        if ("1" == str) {
            str = "星期日"
        } else if ("2" == str) {
            str = "星期一"
        } else if ("3" == str) {
            str = "星期二"
        } else if ("4" == str) {
            str = "星期三"
        } else if ("5" == str) {
            str = "星期四"
        } else if ("6" == str) {
            str = "星期五"
        } else if ("7" == str) {
            str = "星期六"
        }
        return str
    }

    fun getAmOrPmStr(s: String): String {
        val date = DateUtil.parse(s)
        val time = getMillis(date)
        calendar = Calendar.getInstance()
        calendar!!.timeInMillis = time

        val apm = calendar!!.get(Calendar.AM_PM)
        return if (apm == 1) {
            "下午"
        } else {
            "上午"
        }
    }

    /**
     * 分割字符串
     * @param time 时间字符串
     * @param split 分隔符
     * @return
     */
    fun splitTime(time: String, split: String): Array<String>? {
        return if (!TextUtils.isEmpty(time)) {
            time.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        } else null
    }

    /**
     * 根据当前时间判断传入的时间大小，获取传入时间属于那一天 ‘今天’，‘昨天’
     * @param time 时间字符串
     * @return
     */
    fun getDayString(time: String): String {
        //        if (time == null || time.length() == 0){
        //            return "";
        //        }
        //
        //        // 获取当前时间的天数
        //        int currentDay = DateUtil.getDay(new Date());
        //        // 获取当前时间的月数
        //        int currentMonth = DateUtil.getMonth(new Date());
        //        // 获取当前时间的年数
        //        int currentYear = DateUtil.getFormatYear()
        //
        //        // 获取传入时间的天数
        //        int day = Integer.parseInt(splitTime(time," ")[0].split("-")[2]);
        //
        //        int result = currentDay - day;
        //
        //        switch (result){
        //            case 0:
        //                return "今天";
        //            case 1:
        //                return "昨天";
        //            default:
        //                return splitTime(time," ")[0];
        //        }
        return time
    }


    fun dayStart(date: Date): String? {
        val c1 = Calendar.getInstance()
        c1.time = date
        c1.set(Calendar.HOUR_OF_DAY, 0)
        c1.set(Calendar.MINUTE, 0)
        c1.set(Calendar.SECOND, 0)
        return date2Str(c1, datePattern)
    }

    fun dayEnd(date: Date): String? {
        val c1 = Calendar.getInstance()
        c1.time = date
        c1.set(Calendar.HOUR_OF_DAY, 23)
        c1.set(Calendar.MINUTE, 59)
        c1.set(Calendar.SECOND, 59)
        return date2Str(c1, datePattern)
    }
}// yyyy-MM-dd HH:mm:ss
// yyyy-MM-dd HH:mm:ss
/**
 * 使用预设格式提取字符串日期
 *
 * @param strDate 日期字符串
 * @return 提取字符串的日期
 */
