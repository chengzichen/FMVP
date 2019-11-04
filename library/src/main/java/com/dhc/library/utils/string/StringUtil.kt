package com.dhc.library.utils.string

import android.text.TextUtils

import java.text.DecimalFormat
import java.util.Locale
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtil {

    fun get36UUID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    fun get32UUID(): String {
        return  UUID.randomUUID().toString().replace("-".toRegex(), "")
    }
    fun getPercentString(percent: Float): String {
        return String.format(Locale.US, "%d%%", (percent * 100).toInt())
    }

    /**
     * 删除字符串中的空白符
     *
     * @param content
     * @return String
     */
    fun removeBlanks(content: String?): String? {
        if (content == null) {
            return null
        }
        val buff = StringBuilder()
        buff.append(content)
        for (i in buff.length - 1 downTo 0) {
            if (' ' == buff[i] || '\n' == buff[i] || '\t' == buff[i]
                    || '\r' == buff[i]) {
                buff.deleteCharAt(i)
            }
        }
        return buff.toString()
    }

    fun isEmpty(input: String): Boolean {
        return TextUtils.isEmpty(input)
    }

    fun makeMd5(source: String): String? {
        return MD5.getStringMD5(source)
    }

    fun filterUCS4(str: String): String? {
        if (TextUtils.isEmpty(str)) {
            return str
        }

        if (str.codePointCount(0, str.length) == str.length) {
            return str
        }

        val sb = StringBuilder()

        var index = 0
        while (index < str.length) {
            val codePoint = str.codePointAt(index)
            index += Character.charCount(codePoint)
            if (Character.isSupplementaryCodePoint(codePoint)) {
                continue
            }

            sb.appendCodePoint(codePoint)
        }

        return sb.toString()
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    fun counterChars(str: String): Int {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0
        }
        var count = 0
        for (i in 0 until str.length) {
            val tmp = str[i].toInt()
            if (tmp > 0 && tmp < 127) {
                count += 1
            } else {
                count += 2
            }
        }
        return count
    }

    /**
     * 是否符合邮箱格式
     *
     * @param src 源字符串
     * @return 是否符合邮箱格式
     */
    fun isEmail(src: String): Boolean {
        val pattern = Pattern.compile("\\w+@(\\w+\\.){1,3}\\w+")
        return pattern.matcher(src).find()
    }

    /**
     * 是否包含特殊字符（除字母、数字外的字符为特殊字符）
     *
     * @param str 源字符串
     * @return 是否包含特殊字符
     */
    fun hasSpecialLetter(str: String): Boolean {
        val pattern = Pattern.compile("[^a-zA-Z0-9]")
        val mat = pattern.matcher(str)
        return !mat.find()
    }

    /**
     * 是否是手机号码，只做1开头和位数判断
     *
     * @param src 源字符串
     * @return 是否是手机号码
     */
    fun isPhoneNumber(src: String): Boolean {
        //        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        //        Matcher m = p.matcher(src);
        val p = Pattern.compile("^[1]\\d{10}$")
        val m = p.matcher(src)
        return m.matches()
    }

    /**
     * 是否是中国移动手机号码(134 135 136 137 138 139 151 150 158 159)
     *
     * @param src 源字符串
     * @return 是否是中国移动手机号码
     */
    fun isCmccNumber(src: String): Boolean {
        val p = Pattern.compile("^((13[4-9])|(15[0-1])|(15[8-9]))\\d{8}$")
        val m = p.matcher(src)
        return m.matches()
    }

    /**
     * 是否是密码
     *
     * @param src 源字符串
     * @return 是否是密码
     */
    fun isPassword(src: String): Boolean {
        val p = Pattern.compile("^[A-Za-z0-9]*$")
        val m = p.matcher(src)
        return m.matches()
    }

    /**
     * 是否是昵称和手机号
     *
     * @param nickNameID
     * @return
     */
    fun isNumberFormat(nickNameID: String): Boolean {
        val p = Pattern.compile("^[0-9]*$")
        val m = p.matcher(nickNameID)
        return m.matches()
    }

    /**
     * 中英文数字正则表达式(4-16位)
     *
     * @param str
     * @return
     */
    fun isChineseEnglishFormat(str: String): Boolean {
        var str = str
        val p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2DA-Za-z0-9]+$")
        val m = p.matcher(str)
        str = str.replace("[^\\x00-\\xff]".toRegex(), "**")    //匹配双字节字符
        val length = str.length
        return m.matches() && length >= 4 && length <= 16
    }

    /**
     * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
     *
     * @param str                原始字符串
     * @param specialCharsLength 截取长度(汉、日、韩文字符长度为2)
     * @return
     */
    fun trim(str: String?, specialCharsLength: Int): String {
        if (str == null || "" == str || specialCharsLength < 1) {
            return ""
        }
        val chars = str.toCharArray()
        val charsLength = getCharsLength(chars, specialCharsLength)
        return String(chars, 0, charsLength)
    }

    /**
     * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
     *
     * @param chars              一段字符
     * @param specialCharsLength 输入长度，汉、日、韩文字符长度为2
     * @return 输出长度，所有字符均长度为1
     */
    private fun getCharsLength(chars: CharArray, specialCharsLength: Int): Int {
        var count = 0
        var normalCharsLength = 0
        for (i in chars.indices) {
            val specialCharLength = getSpecialCharLength(chars[i])
            if (count <= specialCharsLength - specialCharLength) {
                count += specialCharLength
                normalCharsLength++
            } else {
                break
            }
        }
        return normalCharsLength
    }


    /**
     * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
     *
     * @param str 一段字符
     * @return 输出长度，所有字符均长度为1
     */
    fun getStringLength(str: String): Int {
        if (TextUtils.isEmpty(str)) {
            return 0
        }
        var normalCharsLength = 0
        val chars = str.toCharArray()
        for (i in chars.indices) {
            normalCharsLength += getSpecialCharLength(chars[i])
        }
        return normalCharsLength
    }

    /**
     * 字符串截取，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1，超过长度尾部加上**...**
     *
     * @param str         一段字符
     * @param limitLength 长度限制，超出截取尾部加上**...**
     * @return 输出长度，所有字符均长度为1
     */
    fun subLength(str: String, limitLength: Int): String {
        val length = getStringLength(str)
        var content = str
        if (length > limitLength) {
            content = StringUtil.trim(str, limitLength) + "..."
        }
        return content
    }

    /**
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
     *
     * @param c 字符
     * @return 字符长度
     */
    private fun getSpecialCharLength(c: Char): Int {
        return if (isAscill(c)) {
            1
        } else {
            2
        }
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param c, 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    private fun isAscill(c: Char): Boolean {
        val k = 0x80
        return if (c.toInt() / k == 0) true else false
    }

    /**
     * 为字符串添加双引号
     *
     * @param obj 字符串
     * @return 加入双引号后的完整字符串
     */
    fun addQuotationMarks(obj: String): String {
        return "\"" + obj + "\""
    }

    /**
     * 保留1位小数
     *
     * @param num
     * @return
     */
    fun onlyOneFloat(num: Double): String {
        val decimalFormat = DecimalFormat("0.0")
        return decimalFormat.format(num)
    }

    /**
     * 连接多个字符串标签
     *
     * @param tags                  所有字符标签
     * @param linker                标签之间的连接字符
     * @param isLastTagAppendLinker 最后一个标签末尾是否也需要追加linker
     * @return 连接得到的字符串
     */
    fun concatTags(tags: List<String>?, linker: String, isLastTagAppendLinker: Boolean): String? {
        if (tags != null) {
            val size = tags.size
            if (size > 0) {
                val sb = StringBuilder()
                for (i in 0 until size) {
                    sb.append(tags[i])
                    if (i != size - 1) {
                        sb.append(linker)
                    } else if (isLastTagAppendLinker) {
                        sb.append(linker)
                    }
                }
                return sb.toString()
            } else {
                return ""
            }
        }
        return null
    }

    //转码

    /**
     * 将unicode转编码utf-8格式
     *
     * @param theString
     * @return
     */
    fun decodeUnicode(theString: String): String {
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                val stringBuffer = if (aChar == 'u') {
                    // Read the xxxx
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> value = (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> value = (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> value = (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> throw IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.")
                        }

                    }
                    outBuffer.append(value.toChar())
                } else {
                    if (aChar == 't')
                        aChar = '\t'
                    else if (aChar == 'r')
                        aChar = '\r'
                    else if (aChar == 'n')
                        aChar = '\n'
                    else if (aChar == 'f')
                        aChar = '\u0046'
                    outBuffer.append(aChar)
                }
            } else
                outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }

    /**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    fun ToSBC(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i] == ' ') {
                c[i] = '\u3000'
            } else if (c[i] < '\u007f') {
                c[i] = (c[i].toInt() + 65248).toChar()

            }
        }
        return String(c)
    }


    /**
     * 转半角的函数(DBC case)<br></br><br></br>
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input 任意字符串
     * @return 半角字符串
     */
    fun ToDBC(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i].toInt() == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = 32.toChar()
                continue
            }
            if (c[i].toInt() > 65280 && c[i].toInt() < 65375)
            //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (c[i].toInt() - 65248).toChar()
        }
        return String(c)
    }

    /**
     * 检验字符串只能为纯数字，正小数、正整数均可
     */
    fun isNumber(str: String): Boolean {
        val p = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])")
        val m = p.matcher(str)
        return m.matches()
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    fun isValidTagAndAlias(s: String): Boolean {
        val p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$")
        val m = p.matcher(s)
        return m.matches()
    }
}
