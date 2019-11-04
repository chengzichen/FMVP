package com.dhc.library.utils.media

import android.opengl.GLES10

/**
 * 创建者：邓浩宸
 * 时间 ：2017/5/11 11:37
 * 描述 ：IM计算图片size和采样率
 */
object SampleSizeUtil {

    private var textureSize = 0

    fun calculateSampleSize(imagePath: String, totalPixel: Int): Int {
        val bound = BitmapDecoder.decodeBound(imagePath)
        return calculateSampleSize(bound[0], bound[1], totalPixel)
    }

    fun calculateSampleSize(width: Int, height: Int, totalPixel: Int): Int {
        var ratio = 1

        if (width > 0 && height > 0) {
            ratio = Math.sqrt(((width * height).toFloat() / totalPixel).toDouble()).toInt()
            if (ratio < 1) {
                ratio = 1
            }
        }

        return ratio
    }

    /**
     * Calculate an inSampleSize for use in a [android.graphics.BitmapFactory.Options]
     * object when decoding bitmaps using the decode* methods from
     * [android.graphics.BitmapFactory]. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param width
     * @param height
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    fun calculateSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var reqWidth = reqWidth
        var reqHeight = reqHeight
        // can't proceed
        if (width <= 0 || height <= 0) {
            return 1
        }
        // can't proceed
        if (reqWidth <= 0 && reqHeight <= 0) {
            return 1
        } else if (reqWidth <= 0) {
            reqWidth = (width * reqHeight / height.toFloat() + 0.5f).toInt()
        } else if (reqHeight <= 0) {
            reqHeight = (height * reqWidth / width.toFloat() + 0.5f).toInt()
        }

        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            if (inSampleSize == 0) {
                inSampleSize = 1
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            val totalPixels = (width * height).toFloat()

            // Anything more than 2x the requested pixels we'll sample down
            // further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }

        return inSampleSize
    }

    fun adjustSampleSizeWithTexture(sampleSize: Int, width: Int, height: Int): Int {
        var sampleSize = sampleSize
        val textureSize = getTextureSize()

        if (textureSize > 0 && (width > sampleSize || height > sampleSize)) {
            while (width / sampleSize.toFloat() > textureSize || height / sampleSize.toFloat() > textureSize) {
                sampleSize++
            }

            // 2的指数对齐
            sampleSize = SampleSizeUtil.roundup2n(sampleSize)
        }

        return sampleSize
    }

    //存在第二次拿拿不到的情况，所以把拿到的数据用一个static变量保存下来
    fun getTextureSize(): Int {
        if (textureSize > 0) {
            return textureSize
        }

        val params = IntArray(1)
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, params, 0)
        textureSize = params[0]

        return textureSize
    }

    // 将x向上对齐到2的幂指数
    private fun roundup2n(x: Int): Int {
        var x = x
        if (x and x - 1 == 0) {
            return x
        }
        var pos = 0
        while (x > 0) {
            x = x shr 1
            ++pos
        }
        return 1 shl pos
    }
}
