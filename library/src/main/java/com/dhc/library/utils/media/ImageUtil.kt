package com.dhc.library.utils.media

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.text.TextUtils
import android.util.Log

import com.dhc.library.R
import com.dhc.library.utils.AppContext
import com.dhc.library.utils.file.AttachmentStore
import com.dhc.library.utils.file.FileUtil
import com.dhc.library.utils.media.ImageUtil.getThumbnailDisplaySize
import com.dhc.library.utils.storage.StorageType
import com.dhc.library.utils.storage.StorageUtil
import com.dhc.library.utils.string.StringUtil
import com.dhc.library.utils.sys.DensityUtils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


/**
 * 创建者：邓浩宸
 * 时间 ：2017/5/11 11:37
 * 描述 ：IM处理图片的工具类
 */
object ImageUtil {

    val MAX_IMAGE_RATIO = 5f

    class ImageSize(width: Int, height: Int) {
        var width = 0
        var height = 0

        init {
            this.width = width
            this.height = height
        }
    }

    //    public static Bitmap getDefaultBitmapWhenGetFail() {
    //        try {
    //            return getBitmapImmutableCopy(AppContext.get().getResources(), R.mipmap.image_download_failed);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //    }

    fun getBitmapImmutableCopy(res: Resources, id: Int): Bitmap {
        return getBitmap(res.getDrawable(id))!!.copy(Config.RGB_565, false)
    }

    fun getBitmap(dr: Drawable?): Bitmap? {
        if (dr == null) {
            return null
        }

        return if (dr is BitmapDrawable) {
            dr.bitmap
        } else null

    }

    fun rotateBitmapInNeeded(path: String, srcBitmap: Bitmap?): Bitmap? {
        if (TextUtils.isEmpty(path) || srcBitmap == null) {
            return null
        }

        val localExifInterface: ExifInterface
        try {
            localExifInterface = ExifInterface(path)
            val rotateInt = localExifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            val rotate = getImageRotate(rotateInt)
            if (rotate != 0f) {
                val matrix = Matrix()
                matrix.postRotate(rotate)
                val dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                        srcBitmap.width, srcBitmap.height, matrix,
                        false)
                if (dstBitmap == null) {
                    return srcBitmap
                } else {
                    if (srcBitmap != null && !srcBitmap.isRecycled) {
                        srcBitmap.recycle()
                    }
                    return dstBitmap
                }
            } else {
                return srcBitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return srcBitmap
        }

    }

    /**
     * 获得旋转角度
     *
     * @param rotate
     * @return
     */
    fun getImageRotate(rotate: Int): Float {
        val f: Float
        if (rotate == 6) {
            f = 90.0f
        } else if (rotate == 3) {
            f = 180.0f
        } else if (rotate == 8) {
            f = 270.0f
        } else {
            f = 0.0f
        }

        return f
    }

    fun makeThumbnail(context: Context, imageFile: File): String? {
        val thumbFilePath = StorageUtil.getWritePath(imageFile.name,
                StorageType.TYPE_THUMB_IMAGE)
        val thumbFile = AttachmentStore.create(thumbFilePath) ?: return null

        val result = scaleThumbnail(
                imageFile,
                thumbFile,
                DensityUtils.imageMaxEdge,
                DensityUtils.imageMinEdge,
                Bitmap.CompressFormat.JPEG,
                60)!!
        if (!result) {
            AttachmentStore.delete(thumbFilePath)
            return null
        }

        return thumbFilePath
    }

    fun scaleThumbnail(srcFile: File, dstFile: File, dstMaxWH: Int, dstMinWH: Int, compressFormat: Bitmap.CompressFormat, quality: Int): Boolean? {
        var bRet: Boolean? = false
        var srcBitmap: Bitmap? = null
        var dstBitmap: Bitmap? = null
        var bos: BufferedOutputStream? = null

        try {
            val bound = BitmapDecoder.decodeBound(srcFile)
            val size = getThumbnailDisplaySize(bound[0].toFloat(), bound[1].toFloat(), dstMaxWH.toFloat(), dstMinWH.toFloat())
            srcBitmap = BitmapDecoder.decodeSampled(srcFile.path, size.width, size.height)

            // 旋转
            val localExifInterface = ExifInterface(srcFile.absolutePath)
            val rotateInt = localExifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            val rotate = getImageRotate(rotateInt)

            val matrix = Matrix()
            matrix.postRotate(rotate)

            var inSampleSize = 1f

            if (srcBitmap!!.width >= dstMinWH && srcBitmap.height <= dstMaxWH
                    && srcBitmap.width >= dstMinWH && srcBitmap.height <= dstMaxWH) {
                //如果第一轮拿到的srcBitmap尺寸都符合要求，不需要再做缩放
            } else {
                if (srcBitmap.width != size.width || srcBitmap.height != size.height) {
                    val widthScale = size.width.toFloat() / srcBitmap.width.toFloat()
                    val heightScale = size.height.toFloat() / srcBitmap.height.toFloat()

                    if (widthScale >= heightScale) {
                        size.width = srcBitmap.width
                        size.height /= widthScale.toInt()//必定小于srcBitmap.getHeight()
                        inSampleSize = widthScale
                    } else {
                        size.width /= heightScale.toInt()//必定小于srcBitmap.getWidth()
                        size.height = srcBitmap.height
                        inSampleSize = heightScale
                    }
                }
            }

            matrix.postScale(inSampleSize, inSampleSize)

            if (rotate == 0f && inSampleSize == 1f) {
                dstBitmap = srcBitmap
            } else {
                dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, size.width, size.height, matrix, true)
            }

            bos = BufferedOutputStream(FileOutputStream(dstFile))
            dstBitmap!!.compress(compressFormat, quality, bos)
            bos.flush()
            bRet = true
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            if (srcBitmap != null && !srcBitmap.isRecycled) {
                srcBitmap.recycle()
                srcBitmap = null
            }

            if (dstBitmap != null && !dstBitmap.isRecycled) {
                dstBitmap.recycle()
                dstBitmap = null
            }
        }
        return bRet
    }

    fun getThumbnailDisplaySize(srcWidth: Float, srcHeight: Float, dstMaxWH: Float, dstMinWH: Float): ImageSize {
        var srcWidth = srcWidth
        var srcHeight = srcHeight
        if (srcWidth <= 0 || srcHeight <= 0) { // bounds check
            return ImageSize(dstMinWH.toInt(), dstMinWH.toInt())
        }

        var shorter: Float
        var longer: Float
        val widthIsShorter: Boolean

        //store
        if (srcHeight < srcWidth) {
            shorter = srcHeight
            longer = srcWidth
            widthIsShorter = false
        } else {
            shorter = srcWidth
            longer = srcHeight
            widthIsShorter = true
        }

        if (shorter < dstMinWH) {
            val scale = dstMinWH / shorter
            shorter = dstMinWH
            if (longer * scale > dstMaxWH) {
                longer = dstMaxWH
            } else {
                longer *= scale
            }
        } else if (longer > dstMaxWH) {
            val scale = dstMaxWH / longer
            longer = dstMaxWH
            if (shorter * scale < dstMinWH) {
                shorter = dstMinWH
            } else {
                shorter *= scale
            }
        }

        //restore
        if (widthIsShorter) {
            srcWidth = shorter
            srcHeight = longer
        } else {
            srcWidth = longer
            srcHeight = shorter
        }

        return ImageSize(srcWidth.toInt(), srcHeight.toInt())
    }

    fun getScaledImageFileWithMD5(imageFile: File, mimeType: String): File? {
        val filePath = imageFile.path

        if (!isInvalidPictureFile(mimeType)) {
            Log.i("ImageUtil", "is invalid picture file")
            return null
        }

        val tempFilePath = getTempFilePath(FileUtil.getExtensionName(filePath))
        val tempImageFile = AttachmentStore.create(tempFilePath) ?: return null

        val compressFormat = Bitmap.CompressFormat.JPEG
        // 压缩数值由第三方开发者自行决定
        val maxWidth = 720
        val quality = 60

        return if (ImageUtil.scaleImage(imageFile, tempImageFile, maxWidth, compressFormat, quality)!!) {
            tempImageFile
        } else {
            null
        }
    }

    private fun getTempFilePath(extension: String): String? {
        return StorageUtil.getWritePath(
                AppContext.get(),
                "temp_image_" + StringUtil.get36UUID() + "." + extension,
                StorageType.TYPE_TEMP)
    }

    fun scaleImage(srcFile: File, dstFile: File, dstMaxWH: Int, compressFormat: Bitmap.CompressFormat, quality: Int): Boolean? {
        var success: Boolean? = false

        try {
            val inSampleSize = SampleSizeUtil.calculateSampleSize(srcFile.absolutePath, dstMaxWH * dstMaxWH)
            var srcBitmap: Bitmap? = BitmapDecoder.decodeSampled(srcFile.path, inSampleSize)
                    ?: return success

            // 旋转
            val localExifInterface = ExifInterface(srcFile.absolutePath)
            val rotateInt = localExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val rotate = getImageRotate(rotateInt)

            var dstBitmap: Bitmap?
            val scale = Math.sqrt((dstMaxWH.toFloat() * dstMaxWH.toFloat() / (srcBitmap!!.width.toFloat() * srcBitmap.height.toFloat())).toDouble()).toFloat()
            if (rotate == 0f && scale >= 1) {
                dstBitmap = srcBitmap
            } else {
                try {
                    val matrix = Matrix()
                    if (rotate != 0f) {
                        matrix.postRotate(rotate)
                    }
                    if (scale < 1) {
                        matrix.postScale(scale, scale)
                    }
                    dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
                } catch (e: OutOfMemoryError) {
                    val bos = BufferedOutputStream(FileOutputStream(dstFile))
                    srcBitmap.compress(compressFormat, quality, bos)
                    bos.flush()
                    bos.close()
                    success = true

                    if (!srcBitmap.isRecycled)
                        srcBitmap.recycle()
                    srcBitmap = null

                    return success
                }

            }

            val bos = BufferedOutputStream(FileOutputStream(dstFile))
            dstBitmap!!.compress(compressFormat, quality, bos)
            bos.flush()
            bos.close()
            success = true

            if (!srcBitmap.isRecycled)
                srcBitmap.recycle()
            srcBitmap = null

            if (!dstBitmap.isRecycled)
                dstBitmap.recycle()
            dstBitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        return success
    }

    fun getThumbnailDisplaySize(maxSide: Int, minSide: Int, imagePath: String): ImageSize {
        val bound = BitmapDecoder.decodeBound(imagePath)
        return getThumbnailDisplaySize(bound[0].toFloat(), bound[1].toFloat(), maxSide.toFloat(), minSide.toFloat())
    }

    fun getBoundWithLength(maxSide: Int, imageObject: Any, resizeToDefault: Boolean): IntArray {
        var width = -1
        var height = -1

        val bound: IntArray
        if (String::class.java.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound(imageObject as String)
            width = bound[0]
            height = bound[1]
        } else if (Int::class.java.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound(AppContext.get().resources, imageObject as Int)
            width = bound[0]
            height = bound[1]
        } else if (InputStream::class.java.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound(imageObject as InputStream)
            width = bound[0]
            height = bound[1]
        }

        if (width <= 0 || height <= 0) {
            width = maxSide
            height = maxSide
        } else if (resizeToDefault) {
            if (width > height) {
                height = (maxSide * (height.toFloat() / width.toFloat())).toInt()
                width = maxSide
            } else {
                width = (maxSide * (width.toFloat() / height.toFloat())).toInt()
                height = maxSide
            }
        }

        return intArrayOf(width, height)
    }

    /**
     * 下载失败与获取失败时都统一显示默认下载失败图片
     *
     * @return
     */
    fun getBitmapFromDrawableRes(res: Int): Bitmap? {
        try {
            return getBitmapImmutableCopy(AppContext.get().resources, res)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun isInvalidPictureFile(mimeType: String): Boolean {
        val lowerCaseFilepath = mimeType.toLowerCase()
        return (lowerCaseFilepath.contains("jpg") || lowerCaseFilepath.contains("jpeg")
                || lowerCaseFilepath.toLowerCase().contains("png") || lowerCaseFilepath.toLowerCase().contains("bmp") || lowerCaseFilepath
                .toLowerCase().contains("gif"))
    }

    fun getImageOrientation(mPaths: String): Int {
        var orientation = 0
        try {
            val ei = ExifInterface(mPaths)
            val exif = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (exif) {
                ExifInterface.ORIENTATION_ROTATE_90 -> orientation = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> orientation = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> orientation = 270
            }
        } catch (e: Exception) {
        }

        return orientation
    }


}
