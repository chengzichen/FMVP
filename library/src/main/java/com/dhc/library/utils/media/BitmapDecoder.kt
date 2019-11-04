package com.dhc.library.utils.media

import android.annotation.TargetApi
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.provider.MediaStore

import com.dhc.library.utils.sys.ScreenUtil
import com.dhc.library.utils.file.AttachmentStore
import com.dhc.library.utils.media.BitmapDecoder.decodeBound

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * 创建者：邓浩宸
 * 时间 ：2017/5/11 11:37
 * 描述 ：IM图片解码器
 */
object BitmapDecoder {
    fun decode(`is`: InputStream): Bitmap? {
        val options = BitmapFactory.Options()

        // RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565

        /**
         * 在4.4上，如果之前is标记被移动过，会导致解码失败
         */
        try {
            if (`is`.markSupported()) {
                `is`.reset()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            return BitmapFactory.decodeStream(`is`, null, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        return null
    }

    @JvmOverloads
    fun decodeSampledForDisplay(pathName: String, withTextureLimit: Boolean = true): Bitmap? {
        val ratio = ImageUtil.MAX_IMAGE_RATIO
        val reqBounds = arrayOf(intArrayOf(ScreenUtil.screenWidth * 2, ScreenUtil.screenHeight), intArrayOf(ScreenUtil.screenWidth, ScreenUtil.screenHeight * 2), intArrayOf((ScreenUtil.screenWidth * 1.414).toInt(), (ScreenUtil.screenHeight * 1.414).toInt()))

        // decode bound
        val bound = decodeBound(pathName)
        // pick request bound
        val reqBound = pickReqBoundWithRatio(bound, reqBounds, ratio)

        val width = bound[0]
        val height = bound[1]
        val reqWidth = reqBound[0]
        val reqHeight = reqBound[1]

        // calculate sample size
        var sampleSize = SampleSizeUtil.calculateSampleSize(width, height, reqWidth, reqHeight)

        if (withTextureLimit) {
            // adjust sample size
            sampleSize = SampleSizeUtil.adjustSampleSizeWithTexture(sampleSize, width, height)
        }

        var RETRY_LIMIT = 5
        var bitmap = decodeSampled(pathName, sampleSize)
        while (bitmap == null && RETRY_LIMIT > 0) {
            sampleSize++
            RETRY_LIMIT--
            bitmap = decodeSampled(pathName, sampleSize)
        }

        return bitmap
    }

    fun decodeBound(pathName: String): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(pathName, options)

        return intArrayOf(options.outWidth, options.outHeight)
    }

    fun decodeBound(res: Resources, resId: Int): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        return intArrayOf(options.outWidth, options.outHeight)
    }

    private fun pickReqBoundWithRatio(bound: IntArray, reqBounds: Array<IntArray>, ratio: Float): IntArray {
        val hRatio = if (bound[1] == 0) 0f else bound[0].toFloat() / bound[1].toFloat()
        val vRatio = if (bound[0] == 0) 0f else bound[1].toFloat() / bound[0].toFloat()

        return if (hRatio  >= ratio) {
            reqBounds[0]
        } else if (vRatio >= ratio) {
            reqBounds[1]
        } else {
            reqBounds[2]
        }
    }

    fun decodeSampled(pathName: String, sampleSize: Int): Bitmap? {
        val options = BitmapFactory.Options()

        // RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // sample size
        options.inSampleSize = sampleSize

        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeFile(pathName, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

        return checkInBitmap(bitmap, options, pathName)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun checkInBitmap(bitmap: Bitmap?,
                              options: BitmapFactory.Options, path: String): Bitmap? {
        var bitmap = bitmap
        val honeycomb = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        if (honeycomb && bitmap != options.inBitmap && options.inBitmap != null) {
            options.inBitmap.recycle()
            options.inBitmap = null
        }

        if (bitmap == null) {
            try {
                bitmap = BitmapFactory.decodeFile(path, options)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }

        }
        return bitmap
    }

    fun decodeBound(file: File): IntArray {
        var `is`: InputStream? = null
        try {
            `is` = FileInputStream(file)
            return decodeBound(`is`)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return intArrayOf(0, 0)
    }

    fun decodeBound(`is`: InputStream): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, options)

        return intArrayOf(options.outWidth, options.outHeight)
    }

    fun decodeSampled(`is`: InputStream, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()

        // RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // sample size
        options.inSampleSize = getSampleSize(`is`, reqWidth, reqHeight)

        try {
            return BitmapFactory.decodeStream(`is`, null, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        return null
    }

    fun decodeSampled(pathName: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        return decodeSampled(pathName, getSampleSize(pathName, reqWidth, reqHeight))
    }

    fun getSampleSize(`is`: InputStream, reqWidth: Int, reqHeight: Int): Int {
        // decode bound
        val bound = decodeBound(`is`)

        // calculate sample size

        return SampleSizeUtil.calculateSampleSize(bound[0], bound[1], reqWidth, reqHeight)
    }

    fun getSampleSize(pathName: String, reqWidth: Int, reqHeight: Int): Int {
        // decode bound
        val bound = decodeBound(pathName)

        // calculate sample size

        return SampleSizeUtil.calculateSampleSize(bound[0], bound[1], reqWidth, reqHeight)
    }

    /**
     * ******************************* decode resource ******************************************
     */

    fun decodeSampled(resources: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
        return decodeSampled(resources, resId, getSampleSize(resources, resId, reqWidth, reqHeight))
    }

    fun getSampleSize(resources: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Int {
        // decode bound
        val bound = decodeBound(resources, resId)

        // calculate sample size

        return SampleSizeUtil.calculateSampleSize(bound[0], bound[1], reqWidth, reqHeight)
    }


    fun decodeSampled(res: Resources, resId: Int, sampleSize: Int): Bitmap? {
        val options = BitmapFactory.Options()

        // RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // sample size
        options.inSampleSize = sampleSize

        try {
            return BitmapFactory.decodeResource(res, resId, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        return null
    }

    fun extractThumbnail(videoPath: String, thumbPath: String): Boolean {
        if (!AttachmentStore.isFileExist(thumbPath)) {
            val thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND)
            if (thumbnail != null) {
                AttachmentStore.saveBitmap(thumbnail, thumbPath, true)
                return true
            }
        }
        return false
    }
}
