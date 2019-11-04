package com.dhc.library.utils.file

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.text.TextUtils

import com.dhc.library.utils.string.StringUtil

import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.HashMap
import java.util.Properties

/**
 * 文件工具类
 */
object FileUtil {


    val ASSETS_PREFIX = "file://android_assets/"
    val ASSETS_PREFIX2 = "file://android_asset/"
    val ASSETS_PREFIX3 = "assets://"
    val ASSETS_PREFIX4 = "asset://"
    val RAW_PREFIX = "file://android_raw/"
    val RAW_PREFIX2 = "raw://"
    val FILE_PREFIX = "file://"
    val DRAWABLE_PREFIX = "drawable://"

    private val TAG = "FileUtil"

    /**
     * 判断手机SDCard是否已安装并可读写
     *
     * @return
     */
    val isSDCardAvailable: Boolean
        get() = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(), ignoreCase = true)

    enum class SizeUnit {
        Byte,
        KB,
        MB,
        GB,
        TB,
        Auto
    }

    fun hasExtentsion(filename: String): Boolean {
        val dot = filename.lastIndexOf('.')
        return if (dot > -1 && dot < filename.length - 1) {
            true
        } else {
            false
        }
    }

    // 获取文件扩展名
    fun getExtensionName(filename: String?): String {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return ""
    }

    // 获取文件名
    fun getFileNameFromPath(filepath: String?): String? {
        if (filepath != null && filepath.length > 0) {
            val sep = filepath.lastIndexOf('/')
            if (sep > -1 && sep < filepath.length - 1) {
                return filepath.substring(sep + 1)
            }
        }
        return filepath
    }

    // 获取不带扩展名的文件名
    fun getFileNameNoEx(filename: String?): String? {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length) {
                return filename.substring(0, dot)
            }
        }
        return filename
    }


    /**
     * 读取Property文件
     */
    fun simpleProperty2HashMap(context: Context, path: String): HashMap<String, String> {
        try {
            val `is` = getStream(context, path)
            return simpleProperty2HashMap(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return HashMap()
    }

    @Throws(IOException::class)
    private fun simpleProperty2HashMap(`in`: InputStream): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        val properties = Properties()
        properties.load(`in`)
        `in`.close()
        val keyValue = properties.keys
        val it = keyValue.iterator()
        while (it.hasNext()) {
            val key = it.next() as String
            hashMap[key] = properties[key] as String
        }

        return hashMap
    }


    @Throws(IOException::class)
    fun getStream(context: Context, url: String): InputStream {
        val lowerUrl = url.toLowerCase()
        val `is`: InputStream
        if (lowerUrl.startsWith(ASSETS_PREFIX)) {
            val assetPath = url.substring(ASSETS_PREFIX.length)
            `is` = getAssetsStream(context, assetPath)
        } else if (lowerUrl.startsWith(ASSETS_PREFIX2)) {
            val assetPath = url.substring(ASSETS_PREFIX2.length)
            `is` = getAssetsStream(context, assetPath)
        } else if (lowerUrl.startsWith(ASSETS_PREFIX3)) {
            val assetPath = url.substring(ASSETS_PREFIX3.length)
            `is` = getAssetsStream(context, assetPath)
        } else if (lowerUrl.startsWith(ASSETS_PREFIX4)) {
            val assetPath = url.substring(ASSETS_PREFIX4.length)
            `is` = getAssetsStream(context, assetPath)
        } else if (lowerUrl.startsWith(RAW_PREFIX)) {
            val rawName = url.substring(RAW_PREFIX.length)
            `is` = getRawStream(context, rawName)
        } else if (lowerUrl.startsWith(RAW_PREFIX2)) {
            val rawName = url.substring(RAW_PREFIX2.length)
            `is` = getRawStream(context, rawName)
        } else if (lowerUrl.startsWith(FILE_PREFIX)) {
            val filePath = url.substring(FILE_PREFIX.length)
            `is` = getFileStream(filePath)
        } else if (lowerUrl.startsWith(DRAWABLE_PREFIX)) {
            val drawableName = url.substring(DRAWABLE_PREFIX.length)
            `is` = getDrawableStream(context, drawableName)
        } else {
            throw IllegalArgumentException(String.format("Unsupported url: %s \n" + "Supported: \n%sxxx\n%sxxx\n%sxxx", url, ASSETS_PREFIX, RAW_PREFIX, FILE_PREFIX))
        }
        return `is`
    }

    @Throws(IOException::class)
    private fun getAssetsStream(context: Context, path: String): InputStream {
        return context.assets.open(path)
    }

    @Throws(IOException::class)
    private fun getFileStream(path: String): InputStream {
        return FileInputStream(path)
    }

    @Throws(IOException::class)
    private fun getRawStream(context: Context, rawName: String): InputStream {
        val id = context.resources.getIdentifier(rawName, "raw", context.packageName)
        if (id != 0) {
            try {
                return context.resources.openRawResource(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        throw IOException(String.format("raw of id: %s from %s not found", id, rawName))
    }

    @Throws(IOException::class)
    private fun getDrawableStream(context: Context, rawName: String): InputStream {
        val id = context.resources.getIdentifier(rawName, "drawable", context.packageName)
        if (id != 0) {
            val drawable = context.resources.getDrawable(id) as BitmapDrawable
            val bitmap = drawable.bitmap

            val os = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, os)
            return ByteArrayInputStream(os.toByteArray())
        }

        throw IOException(String.format("bitmap of id: %s from %s not found", id, rawName))
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun getString(context: Context, url: String, encoding: String = "UTF-8"): String {
        var result = readStreamString(getStream(context, url), encoding)
        if (result.startsWith("\ufeff")) {
            result = result.substring(1)
        }

        return result
    }

    @Throws(IOException::class)
    fun readStreamString(`is`: InputStream, encoding: String): String {
        return String(readStream(`is`),  Charset.forName(encoding))
    }

    @Throws(IOException::class)
    fun readStream(`is`: InputStream): ByteArray {
        val baos = ByteArrayOutputStream()
        val buf = ByteArray(1024 * 10)
        var readlen: Int=0
        while ({readlen = `is`.read(buf);readlen}() >= 0) {
            baos.write(buf, 0, readlen)
        }
        baos.close()

        return baos.toByteArray()
    }


    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param fileName
     * @return
     */
    fun write(b: ByteArray, fileName: String): File? {
        var stream: BufferedOutputStream? = null
        var file: File? = null
        try {
            file = File(fileName)
            stream = BufferedOutputStream(FileOutputStream(file))
            stream.write(b)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return file
    }

    /**
     * 文件/目录是否存在
     *
     * @param fileName
     * @return
     */
    fun exist(fileName: String): Boolean {
        return File(fileName).exists()
    }

    /**
     * 创建目录
     *
     * @param path  目录
     * @param cover 是否覆盖
     * @return
     */
    fun createFolder(path: String, cover: Boolean) {
        try {
            val file = File(path)
            if (file.exists()) {
                if (cover) {
                    FileUtil.deleteFile(path, true)
                    file.mkdirs()
                }
            } else {
                file.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 创建空的文件
     *
     * @param fileName 文件
     * @param cover    是否覆盖
     * @return
     */
    fun createFile(fileName: String, cover: Boolean): Boolean {
        if (TextUtils.isEmpty(fileName)) {
            return false
        }
        try {
            val file = File(fileName)
            if (file.exists()) {
                if (cover) {
                    file.delete()
                    file.createNewFile()
                }
            } else {
                // 如果路径不存在，先创建路
                val mFile = file.parentFile
                if (!mFile.exists()) {
                    mFile.mkdirs()
                }
                file.createNewFile()
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @param deleteParent 是否删除父目录
     */
    fun deleteFile(filePath: String?, deleteParent: Boolean) {
        if (filePath == null) {
            return
        }
        try {
            val f = File(filePath)
            if (f.exists() && f.isDirectory) {
                val delFiles = f.listFiles()
                if (delFiles != null) {
                    for (delFile in delFiles) {
                        deleteFile(delFile.absolutePath, deleteParent)
                    }
                }
            }
            if (deleteParent) {
                f.delete()
            } else if (f.isFile) {
                f.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    fun copyFile(oldPath: String, newPath: String) {
        try {
            //            int bytesum = 0;
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //文件存在时
                val inStream = FileInputStream(oldPath) //读入原文件
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                val length: Int
                while ({byteread = inStream.read(buffer);byteread}() != -1) {
                    //                    bytesum += byteread; //字节数 文件大小
                    //                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread)
                }
                inStream.close()
            }
        } catch (e: Exception) {
            //            System.out.println("复制单个文件操作出错");
            e.printStackTrace()

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    fun copyFolder(oldPath: String, newPath: String) {

        try {
            File(newPath).mkdirs() //如果文件夹不存在 则建立新文件夹
            val a = File(oldPath)
            val file = a.list()
            var temp: File? = null
            for (i in file.indices) {
                if (oldPath.endsWith(File.separator)) {
                    temp = File(oldPath + file[i])
                } else {
                    temp = File(oldPath + File.separator + file[i])
                }

                if (temp.isFile) {
                    val input = FileInputStream(temp)
                    val output = FileOutputStream(newPath + "/" + temp.name)
                    val b = ByteArray(1024 * 5)
                    var len: Int=0
                    while ({len = input.read(b);len}() != -1) {
                        output.write(b, 0, len)
                    }
                    output.flush()
                    output.close()
                    input.close()
                }
                if (temp.isDirectory) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i])
                }
            }
        } catch (e: Exception) {
            //            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace()

        }

    }

    /**
     * 读取文件
     *
     * @param fileName
     * @return
     */
    fun readFile(fileName: String): String? {
        try {
            return readFromStream(FileInputStream(File(fileName)))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 读取assets文件
     *
     * @param context
     * @param fileName
     * @return
     */
    fun readAsset(context: Context, fileName: String): String? {
        try {
            val descriptor = context.applicationContext.assets.openFd(fileName)
            return readFromStream(descriptor.createInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 读取raw文件
     *
     * @param context
     * @param resId
     * @return
     */
    fun readRaw(context: Context, resId: Int): String? {
        return readFromStream(context.resources.openRawResource(resId))
    }

    private fun readFromStream(`in`: InputStream?): String? {
        if (`in` == null) {
            return null
        }

        try {
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int=0
            while ({len = `in`.read(buffer);len}() != -1) {
                out.write(buffer, 0, len)
            }
            `in`.close()
            out.close()
            return String(out.toByteArray(), Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun getSavePath(context: Context, FileName: String): String {
        return (getCacheDirectory(context)!!.absolutePath + File.separator
                + FileName)
    }

    fun getCacheDirectory(context: Context): File? {
        var appCacheDir: File? = null
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            appCacheDir = getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        return appCacheDir
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(File(
                Environment.getExternalStorageDirectory(), "Android"), "data")
        val appCacheDir = File(
                File(dataDir, context.packageName), "cache")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
            }

        }
        return appCacheDir
    }

    fun createFile(path: String?): File? {
        if (path != null) {
            val file = File(path)
            if (!file.exists()) {
                val file2 = File(file.parent)
                file2.mkdir()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    return null
                }

            }
            return file
        }
        return null

    }


    /**
     * 获取文件或文件夹大小
     *
     * @param fileOrDir 文件或文件夹对象
     * @return 字节数
     */
    fun getFileOrDirSize(fileOrDir: File?): Long {
        if (fileOrDir == null || !fileOrDir.exists()) {
            return 0L
        }

        if (fileOrDir.isDirectory) {
            val children = fileOrDir.listFiles()
            var length = 0L
            for (child in children) {
                length += getFileOrDirSize(child)
            }
            return length
        } else {
            return getFileSize(fileOrDir)
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 文件对象
     * @return 字节数
     */
    private fun getFileSize(file: File?): Long {
        return if (file == null || !file.exists() || file.isDirectory) {
            0L
        } else file.length()
    }


    /**
     * 获取文件后缀名
     *
     * @param fileNameOrPath 文件名或文件路径
     * @return 后缀名（含“.”）
     */
    fun getSuffix(fileNameOrPath: String): String {
        if (StringUtil.isEmpty(fileNameOrPath)) {
            return ""
        }
        val dot = "."
        return if (!fileNameOrPath.contains(dot)) {
            ""
        } else fileNameOrPath.substring(fileNameOrPath.lastIndexOf(dot)).toLowerCase()
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    fun formatFileSize(size: Long): String {
        val formater = DecimalFormat("####.00")
        if (size == 0L) {
            return "0KB"
        } else if (size < 1024) {
            return size.toString() + "bytes"
        } else if (size < 1024 * 1024) {
            val kbsize = size / 1024f
            return formater.format(kbsize.toDouble()) + "KB"
        } else if (size < 1024 * 1024 * 1024) {
            val mbsize = size.toFloat() / 1024f / 1024f
            return formater.format(mbsize.toDouble()) + "MB"
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            val gbsize = size.toFloat() / 1024f / 1024f / 1024f
            return formater.format(gbsize.toDouble()) + "GB"
        } else {
            return "size: error"
        }
    }


    fun cleanCache(context: Context) {
        deleteFile(getCacheDirectory(context)!!.path, false)
    }


    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    fun makeDirs(file: File): File {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


}
