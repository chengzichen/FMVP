package com.dhc.library.utils.string

import java.io.IOException
import java.io.StringReader

class HexDump {
    internal inner class HexTablifier {
        private var m_row = 8

        private var m_pre = ""

        private var m_post = "\n"

        constructor() {}

        @JvmOverloads
        constructor(row: Int, pre: String = "", post: String = "\n") {
            m_row = row
            m_pre = pre
            m_post = post
        }

        fun format(hex: String): String {
            val reader = StringReader(hex)
            val builder = StringBuilder(hex.length * 2)

            try {
                while (getHexLine(builder, reader)) {
                }
            } catch (e: IOException) {
                // 不应该有异常出现。
            }

            return builder.toString()
        }

        @Throws(IOException::class)
        private fun getHexLine(builder: StringBuilder, reader: StringReader): Boolean {
            val lineBuilder = StringBuilder()
            var result = true

            for (i in 0 until m_row) {
                result = getHexByte(lineBuilder, reader)

                if (result == false)
                    break
            }

            if (lineBuilder.length > 0) {
                builder.append(m_pre)
                builder.append(lineBuilder)
                builder.append(m_post)
            }

            return result
        }

        @Throws(IOException::class)
        private fun getHexByte(builder: StringBuilder, reader: StringReader): Boolean {
            val hexByte = CharArray(4)
            val bytesRead = reader.read(hexByte)

            if (bytesRead == -1)
                return false

            builder.append(hexByte, 0, bytesRead)
            builder.append(" ")

            return bytesRead == 4
        }
    }

    companion object {

        private val m_hexCodes = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

        private val m_shifts = intArrayOf(60, 56, 52, 48, 44, 40, 36, 32, 28, 24, 20, 16, 12, 8, 4, 0)

        fun tablify(bytes: ByteArray): String {
            return HexDump().HexTablifier().format(HexDump.toHex(bytes))
        }

        fun tablify(bytes: ByteArray, row: Int): String {
            return HexDump().HexTablifier(row).format(HexDump
                    .toHex(bytes))
        }

        fun tablify(bytes: ByteArray, row: Int, pre: String): String {
            return HexDump().HexTablifier(row, pre).format(HexDump
                    .toHex(bytes))
        }

        fun tablify(hex: String, row: Int, pre: String, post: String): String {
            return HexDump().HexTablifier(row, pre, post).format(hex)
        }

        private fun toHex(value: Long, digitNum: Int): String {
            val result = StringBuilder(digitNum)

            for (j in 0 until digitNum) {
                val index = (value shr m_shifts[j + (16 - digitNum)] and 15).toInt()
                result.append(m_hexCodes[index])
            }

            return result.toString()
        }

        fun toHex(value: Byte): String {
            return toHex(value.toLong(), 2)
        }

        fun toHex(value: Short): String {
            return toHex(value.toLong(), 4)
        }

        fun toHex(value: Int): String {
            return toHex(value.toLong(), 8)
        }

        fun toHex(value: Long): String {
            return toHex(value, 16)
        }

        @JvmOverloads
        fun toHex(value: ByteArray, offset: Int = 0,
                  length: Int = value.size): String {
            val retVal = StringBuilder()

            val end = offset + length
            for (x in offset until end)
                retVal.append(toHex(value[x]))

            return retVal.toString()
        }

        fun restoreBytes(hex: String): ByteArray? {
            val bytes = ByteArray(hex.length / 2)
            for (i in bytes.indices) {
                val c1 = charToNumber(hex[2 * i])
                val c2 = charToNumber(hex[2 * i + 1])
                if (c1 == -1 || c2 == -1) {
                    return null
                }
                bytes[i] = ((c1 shl 4) + c2).toByte()
            }

            return bytes
        }

        private fun charToNumber(c: Char): Int {
            return if (c >= '0' && c <= '9') {
                c - '0'
            } else if (c >= 'a' && c <= 'f') {
                c - 'a' + 0xa
            } else if (c >= 'A' && c <= 'F') {
                c - 'A' + 0xA
            } else {
                -1
            }
        }
    }
}
