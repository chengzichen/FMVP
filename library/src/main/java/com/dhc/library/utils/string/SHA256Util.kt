package com.dhc.library.utils.string


import androidx.annotation.StringDef
import com.dhc.library.utils.string.SHA256Util.Encrypt

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * 创建者     邓浩宸
 * 创建时间   2016/11/25 14:30
 * 描述	      ${生成SHA256值}
 */
object SHA256Util {

    const val MD5 = "MD5"
    const val SHA_1 = "SHA-1"
    const val SHA_256 = "SHA-256"
    const val SHA_384 = "SHA-384"


    fun sign(str: String, @SignTypeChecker type: String): String? {
        return Encrypt(str, type)
    }

    fun Encrypt(strSrc: String, encName: String): String? {
        var md: MessageDigest? = null
        var strDes: String? = null
        val bt = strSrc.toByteArray()
        try {
            md = MessageDigest.getInstance(encName)
            md!!.update(bt)
            strDes = bytes2Hex(md.digest()) // to HexString
        } catch (e: NoSuchAlgorithmException) {
            return null
        }

        return strDes
    }

    fun bytes2Hex(bts: ByteArray): String {
        var des = ""
        var tmp: String? = null
        for (i in bts.indices) {
            tmp = Integer.toHexString((bts[i] and 0xFF.toByte()).toInt())
            if (tmp!!.length == 1) {
                des += "0"
            }
            des += tmp
        }
        return des
    }


    /**
     * 替代枚举的方案，使用StringDef保证类型安全
     */
    @StringDef(MD5, SHA_1, SHA_256, SHA_384)
    @Retention(RetentionPolicy.SOURCE)
    annotation class SignTypeChecker

}

