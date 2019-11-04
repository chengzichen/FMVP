package com.dhc.library.utils


import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList

/**
 * @creator denghc(desoce)
 * @updateTime 2018/7/30 13:55
 * @description 数字工具类
 */
object ArithmeticUtil {

    /**
     * 对一个数字取精度
     *
     * @param v
     * @param scale
     * @return
     */
    fun round(v: Float, scale: Int): Float {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b = BigDecimal(v.toDouble())
        val one = BigDecimal("1")
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toFloat()
    }

    /**
     * 精确加法
     *
     * @param v1
     * @param v2
     * @return
     */
    fun add(v1: Float, v2: Float): Float {
        val bigDecimal1 = BigDecimal(v1.toDouble())
        val bigDecimal2 = BigDecimal(v2.toDouble())
        return bigDecimal1.add(bigDecimal2).toFloat()
    }

    /**
     * 精确加法
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    fun addWithScale(v1: Float, v2: Float, scale: Int): Float {
        val bigDecimal1 = BigDecimal(v1.toDouble())
        val bigDecimal2 = BigDecimal(v2.toDouble())
        return bigDecimal1.add(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP).toFloat()
    }


    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    fun randomCommon(min: Int, max: Int, n: Int): IntArray? {
        if (n > max - min + 1 || max < min) {
            return null
        }
        val result = IntArray(n)
        var count = 0
        while (count < n) {
            val num = (Math.random() * (max - min)).toInt() + min
            var flag = true
            for (j in 0 until n) {
                if (num == result[j]) {
                    flag = false
                    break
                }
            }
            if (flag) {
                result[count] = num
                count++
            }
        }
        return result
    }

    /**
     * 精确减法
     *
     * @param v1
     * @param v2
     * @return
     */
    fun sub(v1: Float, v2: Float): Float {
        val b1 = BigDecimal(v1.toDouble())
        val b2 = BigDecimal(v2.toDouble())
        return b1.subtract(b2).toFloat()
    }

    /**
     * 精确乘法,默认保留一位小数
     *
     * @param v1
     * @param v2
     * @return
     */
    fun mul(v1: Float, v2: Float): Float {
        return mulWithScale(v1, v2, 1)
    }

    /**
     * 精确乘法，保留scale位小数
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    fun mulWithScale(v1: Float, v2: Float, scale: Int): Float {
        val b1 = BigDecimal(v1.toDouble())
        val b2 = BigDecimal(v2.toDouble())
        return round(b1.multiply(b2).toFloat(), scale)
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    fun div(v1: Float, v2: Float, scale: Int): Float {
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        val b1 = BigDecimal(v1.toDouble())
        val b2 = BigDecimal(v2.toDouble())
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toFloat()
    }

    /**
     * 取余数
     *
     * @param v1
     * @param v2
     * @return
     */
    fun remainder(v1: Float, v2: Float): Int {
        return Math.round(v1 * 100) % Math.round(v2 * 100)
    }

    /**
     * 比较大小 如果v1 大于v2 则 返回true 否则false
     *
     * @param v1
     * @param v2
     * @return
     */
    fun strCompareTo(v1: String, v2: String): Boolean {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        val bj = b1.compareTo(b2)
        val res: Boolean
        if (bj > 0)
            res = true
        else
            res = false
        return res
    }


    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     *
     * @param n
     * @return
     */
    private fun factorial(n: Int): Long {
        return if (n > 1) n * factorial(n - 1) else 1
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     *
     * @param n
     * @param m
     * @return
     */
    fun arrangement(n: Int, m: Int): Long {
        return if (n >= m) factorial(n) / factorial(n - m) else 0
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     *
     * @param n
     * @param m
     * @return
     */
    fun combination(n: Int, m: Int): Long {
        return if (n >= m) factorial(n) / factorial(n - m) / factorial(m) else 0
    }

    fun simpleCircle(num: Int): Int {//简单的循环计算的阶乘
        var sum = 1
        require(num >= 0) { //判断传入数是否为负数
            "必须为正整数!"//抛出不合理参数异常
        }
        for (i in 1..num) {//循环num
            sum *= i//每循环一次进行乘法运算
        }
        return sum//返回阶乘的值
    }


    fun recursion(num: Int): Int {//利用递归计算阶乘
        var sum = 1
        require(num >= 0) { "必须为正整数!" }//抛出不合理参数异常
        if (num == 1) {
            return 1//根据条件,跳出循环
        } else {
            sum = num * recursion(num - 1)//运用递归计算
            return sum
        }
    }

    fun addArray(num: Int): Long {//数组添加计算阶乘
        val arr = LongArray(21)//创建数组
        arr[0] = 1

        var last = 0
        require(num < arr.size) {
            "传入的值太大"//抛出传入的数太大异常
        }
        require(num >= 0) { "必须为正整数!" }//抛出不合理参数异常
        while (last < num) {//建立满足小于传入数的while循环
            arr[last + 1] = arr[last] * (last + 1)//进行运算
            last++//last先进行运算，再将last的值加1
        }
        return arr[num]
    }

    @Synchronized
    fun bigNumber(num: Int): BigInteger {//利用BigInteger类计算阶乘

        val list = arrayListOf<BigInteger>()//创建集合数组
        list.add(BigInteger.valueOf(1))//往数组里添加一个数值
        for (i in list.size..num) {
            val lastfact = list.get(i - 1) as BigInteger//获得第一个元素
            val nextfact = lastfact.multiply(BigInteger.valueOf(i.toLong()))//获得下一个数组
            list.add(nextfact)
        }
        return list.get(num)//返回数组中的下标为num的值

    }
}