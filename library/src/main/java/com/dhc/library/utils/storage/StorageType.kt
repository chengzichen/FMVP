package com.dhc.library.utils.storage

enum class StorageType private constructor(private val storageDirectoryName: DirectoryName, val storageMinSize: Long = StorageUtil.THRESHOLD_MIN_SPCAE) {
    TYPE_LOG(DirectoryName.LOG_DIRECTORY_NAME),
    TYPE_TEMP(DirectoryName.TEMP_DIRECTORY_NAME),
    TYPE_FILE(DirectoryName.FILE_DIRECTORY_NAME),
    TYPE_AUDIO(DirectoryName.AUDIO_DIRECTORY_NAME),
    TYPE_IMAGE(DirectoryName.IMAGE_DIRECTORY_NAME),
    TYPE_VIDEO(DirectoryName.VIDEO_DIRECTORY_NAME),
    TYPE_THUMB_IMAGE(DirectoryName.THUMB_DIRECTORY_NAME),
    TYPE_THUMB_VIDEO(DirectoryName.THUMB_DIRECTORY_NAME);

    val storagePath: String
        get() = storageDirectoryName.path

    internal enum class DirectoryName private constructor(val path: String) {
        AUDIO_DIRECTORY_NAME("audio/"),
        DATA_DIRECTORY_NAME("data/"),
        FILE_DIRECTORY_NAME("file/"),
        LOG_DIRECTORY_NAME("log/"),
        TEMP_DIRECTORY_NAME("temp/"),
        IMAGE_DIRECTORY_NAME("image/"),
        THUMB_DIRECTORY_NAME("thumb/"),
        VIDEO_DIRECTORY_NAME("video/")
    }
}
