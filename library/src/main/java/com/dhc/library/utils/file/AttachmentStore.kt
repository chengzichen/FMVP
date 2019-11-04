package com.dhc.library.utils.file

import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log


import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


/**
 * 用于把附件保存到文件系统中
 */
object AttachmentStore {
    fun copy(srcPath: String, dstPath: String): Long {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            return -1
        }

        val source = File(srcPath)
        if (!source.exists()) {
            return -1
        }

        if (srcPath == dstPath) {
            return source.length()
        }

        var fcin: FileChannel? = null
        var fcout: FileChannel? = null
        try {
            fcin = FileInputStream(source).channel
            fcout = FileOutputStream(create(dstPath)!!).channel
            val tmpBuffer = ByteBuffer.allocateDirect(4096)
            while (fcin!!.read(tmpBuffer) != -1) {
                tmpBuffer.flip()
                fcout!!.write(tmpBuffer)
                tmpBuffer.clear()
            }
            return source.length()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fcin?.close()
                fcout?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return -1
    }

    fun getFileLength(srcPath: String): Long {
        if (TextUtils.isEmpty(srcPath)) {
            return -1
        }

        val srcFile = File(srcPath)
        return if (!srcFile.exists()) {
            -1
        } else srcFile.length()

    }

    fun save(path: String, content: String): Long {
        return save(content.toByteArray(), path)
    }

    /**
     * 把数据保存到文件系统中，并且返回其大小
     *
     * @param data
     * @param filePath
     * @return 如果保存失败,则返回-1
     */
    fun save(data: ByteArray, filePath: String): Long {
        if (TextUtils.isEmpty(filePath)) {
            return -1
        }

        val f = File(filePath)
        if (f.parentFile == null) {
            return -1
        }

        if (!f.parentFile.exists()) {// 如果不存在上级文件夹
            f.parentFile.mkdirs()
        }
        try {
            f.createNewFile()
            val fout = FileOutputStream(f)
            fout.write(data)
            fout.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }

        return f.length()
    }

    fun move(srcFilePath: String, dstFilePath: String): Boolean {
        if (TextUtils.isEmpty(srcFilePath) || TextUtils.isEmpty(dstFilePath)) {
            return false
        }

        val srcFile = File(srcFilePath)
        if (!srcFile.exists() || !srcFile.isFile) {
            return false
        }

        val dstFile = File(dstFilePath)
        if (dstFile.parentFile == null) {
            return false
        }

        if (!dstFile.parentFile.exists()) {// 如果不存在上级文件夹
            dstFile.parentFile.mkdirs()
        }

        return srcFile.renameTo(dstFile)
    }

    fun create(filePath: String?): File? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }

        val f = File(filePath)
        if (!f.parentFile.exists()) {// 如果不存在上级文件夹
            f.parentFile.mkdirs()
        }
        try {
            f.createNewFile()
            return f
        } catch (e: IOException) {
            if (f != null && f.exists()) {
                f.delete()
            }
            return null
        }

    }

    /**
     * @param is
     * @param filePath
     * @return 保存失败，返回-1
     */
    fun save(inputStream: InputStream, filePath: String): Long {
        val f = File(filePath)
        if (!f.parentFile.exists()) {// 如果不存在上级文件夹
            f.parentFile.mkdirs()
        }
        var fos: FileOutputStream? = null
        try {
            f.createNewFile()
            fos = FileOutputStream(f)
            var read = 0
            val bytes = ByteArray(8091)
            while ({ read = inputStream.read(bytes);read }() != -1) {
                fos.write(bytes, 0, read)
            }
            return f.length()
        } catch (e: IOException) {
            if (f != null && f.exists()) {
                f.delete()
            }
            Log.i("file", "save is to " + filePath + " failed: " + e.message)
            return -1
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 把文件从文件系统中读取出来
     *
     * @param path
     * @return 如果无法读取,则返回null
     */
    fun load(path: String): ByteArray? {
        try {
            val f = File(path)
            var unread = f.length().toInt()
            var read = 0
            val buf = ByteArray(unread) // 读取文件长度
            val fin = FileInputStream(f)
            do {
                val count = fin.read(buf, read, unread)
                read += count
                unread -= count
            } while (unread != 0)
            fin.close()
            return buf
        } catch (e: FileNotFoundException) {
            return null
        } catch (e: IOException) {
            return null
        }

    }

    fun loadAsString(path: String): String? {
        if (isFileExist(path)) {
            val content = load(path)
            return String(content!!)
        } else {
            return null
        }
    }

    /**
     * 删除指定路径文件
     *
     * @param path
     */
    fun delete(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        var f = File(path)
        if (f.exists()) {
            f = renameOnDelete(f)
            return f.delete()
        } else {
            return false
        }
    }

    fun deleteOnExit(path: String) {
        if (TextUtils.isEmpty(path)) {
            return
        }
        val f = File(path)
        if (f.exists()) {
            f.deleteOnExit()
        }
    }

    fun deleteDir(path: String): Boolean {
        return deleteDir(path, true)
    }

    private fun deleteDir(path: String, rename: Boolean): Boolean {
        var success = true
        var file = File(path)
        if (file.exists()) {
            if (rename) {
                file = renameOnDelete(file)
            }

            val list = file.listFiles()
            if (list != null) {
                val len = list.size
                for (i in 0 until len) {
                    if (list[i].isDirectory) {
                        deleteDir(list[i].path, false)
                    } else {
                        val ret = list[i].delete()
                        if (!ret) {
                            success = false
                        }
                    }
                }
            }
        } else {
            success = false
        }
        if (success) {
            file.delete()
        }
        return success
    }

    // rename before delete to avoid lingering filesystem lock of android
    private fun renameOnDelete(file: File): File {
        val tmpPath = file.parent + "/" + System.currentTimeMillis() + "_tmp"
        val tmpFile = File(tmpPath)
        return if (file.renameTo(tmpFile)) {
            tmpFile
        } else {
            file
        }
    }

    fun isFileExist(path: String): Boolean {
        return if (!TextUtils.isEmpty(path) && File(path).exists()) {
            true
        } else {
            false
        }
    }

    fun saveBitmap(bitmap: Bitmap?, path: String, recyle: Boolean): Boolean {
        if (bitmap == null || TextUtils.isEmpty(path)) {
            return false
        }

        var bos: BufferedOutputStream? = null
        try {
            val fos = FileOutputStream(path)
            bos = BufferedOutputStream(fos)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            return true

        } catch (e: FileNotFoundException) {
            return false
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                }

            }
            if (recyle) {
                bitmap.recycle()
            }
        }
    }
}
