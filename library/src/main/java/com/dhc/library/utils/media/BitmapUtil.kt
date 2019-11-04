package com.dhc.library.utils.media

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.text.TextUtils

import java.io.IOException

/**
 * 创建者：邓浩宸
 * 时间 ：2017/5/11 11:37
 * 描述 ：IM图片bitmap的工具类
 */
object BitmapUtil {

    /**
     * 获取压缩后的图片
     *
     * @param res
     * @param resId
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        /**
         * 1.获取图片的像素宽高(不加载图片至内存中,所以不会占用资源)
         * 2.计算需要压缩的比例
         * 3.按将图片用计算出的比例压缩,并加载至内存中使用
         */
        // 首先不加载图片,仅获取图片尺寸
        val options = BitmapFactory.Options()
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeResource(res, resId, options)

        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false
        options.inScaled = false
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * 计算压缩比例值
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // 保存图片原宽高值
        val height = options.outHeight
        val width = options.outWidth
        // 初始化压缩比例为1
        var inSampleSize = 1

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun reviewPicRotate(bitmap: Bitmap, path: String): Bitmap? {
        var bitmap = bitmap
        var degree = 0
        val mimeType = getImageType(path)
        if (!TextUtils.isEmpty(mimeType) && mimeType != "image/png") {
            degree = getPicRotate(path)
        }
        if (degree != 0) {
            try {
                val m = Matrix()
                val width = bitmap.width
                val height = bitmap.height
                m.setRotate(degree.toFloat())
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (err: Error) {
                err.printStackTrace()
            }

        }
        return bitmap
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     * 图片绝对路径
     * @return degree旋转的角度
     */
    fun getPicRotate(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    fun resizeBitmap(bitmap: Bitmap?, w: Int, h: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }

        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    fun getImageType(path: String): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        return options.outMimeType
    }
}
