package com.dhc.library.utils.string

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.UnsupportedEncodingException
import java.security.MessageDigest

object MD5 {

    fun getStringMD5(value: String?): String? {
        if (value == null || value.trim { it <= ' ' }.length < 1) {
            return null
        }
        try {
            return getMD5(value.toByteArray(charset("UTF-8")))
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e.message, e)
        }

    }

    fun getMD5(source: ByteArray): String {
        try {
            val md5 = MessageDigest.getInstance("MD5")
            return HexDump.toHex(md5.digest(source))
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }

    }

    fun getStreamMD5(filePath: String): String? {
        var hash: String? = null
        val buffer = ByteArray(4096)
        var `in`: BufferedInputStream? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            `in` = BufferedInputStream(FileInputStream(filePath))
            var numRead = 0
            while ({numRead = `in`.read(buffer);numRead}() > 0) {
                md5.update(buffer, 0, numRead)
            }
            `in`.close()
            hash = HexDump.toHex(md5.digest())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        }
        return hash
    }
}
