package com.dhc.library.data

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.tencent.mmkv.MMKV
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * 创建者：邓浩宸
 * 时间 ：2016/10/9 11:26
 * 描述 ：SharedPreferences统一管理类
 */
object SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    @JvmStatic
    val FILE_NAME = "ZhiFuData"
    @JvmStatic
    val VERSION_FILE_NAME = "version_file_name"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     * @param fileName
     */
    @JvmOverloads
    @JvmStatic
    fun put(context: Context, key: String, `object`: Any?, fileName: String = FILE_NAME) {

        val preferences = MMKV.mmkvWithID(fileName)
        val editor = preferences.edit()
        if (`object` is String) {
            editor.putString(key, `object` as String?)
        } else if (`object` is Int) {
            editor.putInt(key, (`object` as Int?)!!)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, (`object` as Boolean?)!!)
        } else if (`object` is Float) {
            editor.putFloat(key, (`object` as Float?)!!)
        } else if (`object` is Long) {
            editor.putLong(key, (`object` as Long?)!!)
        }
        else {
            if (`object` == null)
                return
            editor.putString(key, `object`.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    @JvmOverloads
    @JvmStatic
    operator fun  get(context: Context, key: String, defaultObject: Any, fileName: String =
        FILE_NAME
    ): Any? {
        val sp = MMKV.mmkvWithID(fileName)
        if (defaultObject is String) {
            return sp.getString(key, defaultObject)
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)
        }
        return null
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    @JvmOverloads
    @JvmStatic
    fun remove(fileName: String =
                    FILE_NAME,context: Context, key: String) {
        val sp = MMKV.mmkvWithID(fileName)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    @JvmOverloads
    @JvmStatic
    fun clear(context: Context,fileName: String =
        FILE_NAME) {
        val sp = MMKV.mmkvWithID(fileName)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    @JvmOverloads
    @JvmStatic
    fun contains(context: Context, key: String,fileName: String =
        FILE_NAME): Boolean {
        val sp = MMKV.mmkvWithID(fileName)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    @JvmOverloads
    @JvmStatic
    fun getAll(context: Context,fileName: String =
        FILE_NAME): Map<String, *> {
        val sp = MMKV.mmkvWithID(fileName)
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @JvmStatic
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        @JvmStatic
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }


    /**
     * desc:保存对象
     *
     * @param context
     * @param key
     * @param obj     要保存的对象，只能保存实现了serializable的对象
     * modified:
     */
    @JvmOverloads
    @JvmStatic
    fun saveObject(context: Context, key: String, obj: Any,fileName: String =
        FILE_NAME) {
        try {
            // 保存对象
            val sharedata = MMKV.mmkvWithID(fileName).edit()
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            val bos = ByteArrayOutputStream()
            val os = ObjectOutputStream(bos)
            //将对象序列化写入byte缓存
            os.writeObject(obj)
            //将序列化的数据转为16进制保存
            val bytesToHexString = bytesToHexString(bos.toByteArray())
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString)
            sharedata.commit()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("", "保存obj失败")
        }

    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    @JvmStatic
    fun bytesToHexString(bArray: ByteArray?): String? {
        if (bArray == null) {
            return null
        }
        if (bArray.size == 0) {
            return ""
        }
        val sb = StringBuffer(bArray.size)
        var sTemp: String
        for (i in bArray.indices) {
            sTemp = Integer.toHexString(0xFF and bArray[i].toInt())
            if (sTemp.length < 2)
                sb.append(0)
            sb.append(sTemp.toUpperCase())
        }
        return sb.toString()
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param context
     * @param key
     * @return modified:
     */
    @JvmStatic
    @JvmOverloads
    fun readObject(context: Context, key: String,fileName: String =
        FILE_NAME): Any? {
        try {
            val sharedata = MMKV.mmkvWithID(fileName)
            if (sharedata.contains(key)) {
                val string = sharedata.getString(key, "")
                if (TextUtils.isEmpty(string)) {
                    return null
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    val stringToBytes = StringToBytes(string!!)
                    val bis = ByteArrayInputStream(stringToBytes)
                    val `is` = ObjectInputStream(bis)
                    //返回反序列化得到的对象
                    return `is`.readObject()
                }
            }
        } catch (e: StreamCorruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        //所有异常返回null
        return null

    }

    /**
     * desc:将16进制的数据转为数组
     *
     * 创建人：聂旭阳 , 2014-5-25 上午11:08:33
     *
     * @param data
     * @return modified:
     */
    @JvmStatic
    fun StringToBytes(data: String): ByteArray? {
        val hexString = data.toUpperCase().trim { it <= ' ' }
        if (hexString.length % 2 != 0) {
            return null
        }
        val retData = ByteArray(hexString.length / 2)
        var i = 0
        while (i < hexString.length) {
            val int_ch: Int  // 两位16进制数转化后的10进制数
            val hex_char1 = hexString[i] ////两位16进制数中的第一位(高位*16)
            val int_ch1: Int
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1.toInt() - 48) * 16   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1.toInt() - 55) * 16 //// A 的Ascll - 65
            else
                return null
            i++
            val hex_char2 = hexString[i] ///两位16进制数中的第二位(低位)
            val int_ch2: Int
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = hex_char2.toInt() - 48 //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2.toInt() - 55 //// A 的Ascll - 65
            else
                return null
            int_ch = int_ch1 + int_ch2
            retData[i / 2] = int_ch.toByte()//将转化后的数放入Byte里
            i++
        }
        return retData
    }


}