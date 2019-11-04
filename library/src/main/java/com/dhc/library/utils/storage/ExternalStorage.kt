package com.dhc.library.utils.storage

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils

import java.io.File
import java.io.IOException

/** package  */
internal class ExternalStorage private constructor() {
    /**
     * 外部存储根目录
     */
    private var sdkStorageRoot: String? = null

    val isSdkStorageReady: Boolean
        get() {
            val externalRoot = Environment.getExternalStorageDirectory().absolutePath
            return if (this.sdkStorageRoot!!.startsWith(externalRoot)) {
                Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            } else {
                true
            }
        }

    /**
     * 获取外置存储卡剩余空间
     * @return
     */
    val availableExternalSize: Long
        get() = getResidualSpace(sdkStorageRoot)

    fun init(context: Context, sdkStorageRoot: String) {
        if (!TextUtils.isEmpty(sdkStorageRoot)) {
            val dir = File(sdkStorageRoot)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            if (dir.exists() && !dir.isFile) {
                this.sdkStorageRoot = sdkStorageRoot
                if (!sdkStorageRoot.endsWith("/")) {
                    this.sdkStorageRoot = "$sdkStorageRoot/"
                }
            }
        }

        if (TextUtils.isEmpty(this.sdkStorageRoot)) {
            loadStorageState(context)
        }

        createSubFolders()
    }

    private fun loadStorageState(context: Context) {
        val externalPath = Environment.getExternalStorageDirectory().path
        this.sdkStorageRoot = externalPath + "/" + context.packageName + "/"
    }

    private fun createSubFolders() {
        var result = true
        val root = File(sdkStorageRoot)
        if (root.exists() && !root.isDirectory) {
            root.delete()
        }
        for (storageType in StorageType.values()) {
            result = result and makeDirectory(sdkStorageRoot!! + storageType.storagePath)
        }
        if (result) {
            createNoMediaFile(sdkStorageRoot)
        }
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    private fun makeDirectory(path: String): Boolean {
        val file = File(path)
        var exist = file.exists()
        if (!exist) {
            exist = file.mkdirs()
        }
        return exist
    }

    private fun createNoMediaFile(path: String?) {
        val noMediaFile = File("$path/$NO_MEDIA_FILE_NAME")
        try {
            if (!noMediaFile.exists()) {
                noMediaFile.createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 文件全名转绝对路径（写）
     *
     * @param fileName
     * 文件全名（文件名.扩展名）
     * @return 返回绝对路径信息
     */
    fun getWritePath(fileName: String, fileType: StorageType): String {
        return pathForName(fileName, fileType, false, false)
    }

    private fun pathForName(fileName: String, type: StorageType, dir: Boolean,
                            check: Boolean): String {
        val directory = getDirectoryByDirType(type)
        val path = StringBuilder(directory)

        if (!dir) {
            path.append(fileName)
        }

        val pathString = path.toString()
        val file = File(pathString)

        if (check) {
            if (file.exists()) {
                if (dir && file.isDirectory || !dir && !file.isDirectory) {
                    return pathString
                }
            }

            return ""
        } else {
            return pathString
        }
    }

    /**
     * 返回指定类型的文件夹路径
     *
     * @param fileType
     * @return
     */
    fun getDirectoryByDirType(fileType: StorageType): String {
        return sdkStorageRoot!! + fileType.storagePath
    }

    /**
     * 根据输入的文件名和类型，找到该文件的全路径。
     * @param fileName
     * @param fileType
     * @return 如果存在该文件，返回路径，否则返回空
     */
    fun getReadPath(fileName: String, fileType: StorageType): String {
        return if (TextUtils.isEmpty(fileName)) {
            ""
        } else pathForName(fileName, fileType, false, true)

    }

    /**
     * 获取目录剩余空间
     * @param directoryPath
     * @return
     */
    private fun getResidualSpace(directoryPath: String?): Long {
        try {
            val sf = StatFs(directoryPath)
            val blockSize = sf.blockSize.toLong()
            val availCount = sf.availableBlocks.toLong()
            return availCount * blockSize
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    companion object {

        private var instance: ExternalStorage? = null

        @Synchronized
        fun getInstance(): ExternalStorage {
            if (instance == null) {
                instance = ExternalStorage()
            }
            return instance as ExternalStorage
        }

        protected var NO_MEDIA_FILE_NAME = ".nomedia"
    }
}
