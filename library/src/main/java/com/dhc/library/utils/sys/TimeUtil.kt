package com.dhc.library.utils.sys

import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

object TimeUtil {

    val tsTimes: LongArray
        get() {
            val times = LongArray(2)

            val calendar = Calendar.getInstance()

            times[0] = calendar.timeInMillis / 1000

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            times[1] = calendar.timeInMillis / 1000

            return times
        }

    val nowDatetime: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date())
        }

    val now: Int
        get() = (Date().time / 1000).toInt()

    fun isEarly(days: Int, time: Long): Boolean {
        return currentTimeMillis() - time > days * 24 * 3600 * 1000
    }

    fun currentTimeSecond(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    fun getFormatDatetime(year: Int, month: Int, day: Int): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(GregorianCalendar(year, month, day).time)
    }

    fun getDateFromFormatString(formatDate: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            return sdf.parse(formatDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun getNowDateTime(format: String): String {
        val date = Date()

        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(date)
    }

    fun getDateString(milliseconds: Long): String {
        return getDateTimeString(milliseconds, "yyyyMMdd")
    }


    fun getTimeString(milliseconds: Long): String {
        return getDateTimeString(milliseconds, "HHmmss")
    }

    fun getBeijingNowTimeString(format: String): String {
        val timezone = TimeZone.getTimeZone("Asia/Shanghai")

        val date = Date(currentTimeMillis())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = timezone

        val gregorianCalendar = GregorianCalendar()
        gregorianCalendar.timeZone = timezone
        val prefix = if (gregorianCalendar.get(Calendar.AM_PM) == Calendar.AM) "上午" else "下午"

        return prefix + formatter.format(date)
    }

    fun getBeijingNowTime(format: String): String {
        val timezone = TimeZone.getTimeZone("Asia/Shanghai")

        val date = Date(currentTimeMillis())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = timezone

        return formatter.format(date)
    }

    fun getDateTimeString(milliseconds: Long, format: String): String {
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }


    fun getFavoriteCollectTime(milliseconds: Long): String {
        var showDataString = ""
        val today = Date()
        val date = Date(milliseconds)
        val firstDateThisYear = Date(today.year, 0, 0)
        if (!date.before(firstDateThisYear)) {
            val dateformatter = SimpleDateFormat("MM-dd", Locale.getDefault())
            showDataString = dateformatter.format(date)
        } else {
            val dateformatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            showDataString = dateformatter.format(date)
        }
        return showDataString
    }

    fun getTimeShowString(milliseconds: Long, abbreviate: Boolean): String {
        val dataString: String
        val timeStringBy24: String

        val currentTime = Date(milliseconds)
        val today = Date()
        val todayStart = Calendar.getInstance()
        todayStart.set(Calendar.HOUR_OF_DAY, 0)
        todayStart.set(Calendar.MINUTE, 0)
        todayStart.set(Calendar.SECOND, 0)
        todayStart.set(Calendar.MILLISECOND, 0)
        val todaybegin = todayStart.time
        val yesterdaybegin = Date(todaybegin.time - 3600 * 24 * 1000)
        val preyesterday = Date(yesterdaybegin.time - 3600 * 24 * 1000)

        if (!currentTime.before(todaybegin)) {
            dataString = "今天"
        } else if (!currentTime.before(yesterdaybegin)) {
            dataString = "昨天"
        } else if (!currentTime.before(preyesterday)) {
            dataString = "前天"
        } else if (isSameWeekDates(currentTime, today)) {
            dataString = getWeekOfDate(currentTime)
        } else {
            val dateformatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dataString = dateformatter.format(currentTime)
        }

        val timeformatter24 = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeStringBy24 = timeformatter24.format(currentTime)

        return if (abbreviate) {
            if (!currentTime.before(todaybegin)) {
                getTodayTimeBucket(currentTime)
            } else {
                dataString
            }
        } else {
            "$dataString $timeStringBy24"
        }
    }

    /**
     * 根据不同时间段，显示不同时间
     *
     * @param date
     * @return
     */
    fun getTodayTimeBucket(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val timeformatter0to11 = SimpleDateFormat("KK:mm", Locale.getDefault())
        val timeformatter1to12 = SimpleDateFormat("hh:mm", Locale.getDefault())
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        if (hour >= 0 && hour < 5) {
            return "凌晨 " + timeformatter0to11.format(date)
        } else if (hour >= 5 && hour < 12) {
            return "上午 " + timeformatter0to11.format(date)
        } else if (hour >= 12 && hour < 18) {
            return "下午 " + timeformatter1to12.format(date)
        } else if (hour >= 18 && hour < 24) {
            return "晚上 " + timeformatter1to12.format(date)
        }
        return ""
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    fun getWeekOfDate(date: Date): String {
        val weekDaysName = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        val calendar = Calendar.getInstance()
        calendar.time = date
        val intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return weekDaysName[intWeek]
    }

    fun isSameDay(time1: Long, time2: Long): Boolean {
        return isSameDay(Date(time1), Date(time2))
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    fun isSameWeekDates(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        val subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true
        }
        return false
    }

    fun getSecondsByMilliseconds(milliseconds: Long): Long {
// if (seconds == 0) {
        // seconds = 1;
        // }
        return BigDecimal((milliseconds.toFloat() / 1000.toFloat()).toDouble()).setScale(0,
                BigDecimal.ROUND_HALF_UP).toInt().toLong()
    }

    fun secToTime(time: Int): String {
        var timeStr: String? = null
        var hour = 0
        var minute = 0
        var second = 0
        if (time <= 0)
            return "00:00"
        else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute = minute % 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    fun unitFormat(i: Int): String {
        var retStr: String? = null
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i)
        else
            retStr = "" + i
        return retStr
    }

    fun getElapseTimeForShow(milliseconds: Int): String {
        val sb = StringBuilder()
        var seconds = milliseconds / 1000
        if (seconds < 1)
            seconds = 1
        val hour = seconds / (60 * 60)
        if (hour != 0) {
            sb.append(hour).append("小时")
        }
        val minute = (seconds - 60 * 60 * hour) / 60
        if (minute != 0) {
            sb.append(minute).append("分")
        }
        val second = seconds - 60 * 60 * hour - 60 * minute
        if (second != 0) {
            sb.append(second).append("秒")
        }
        return sb.toString()
    }
}
