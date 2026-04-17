package com.luoke.location.capture

import android.graphics.Bitmap

object MiniMapCropper {

    fun crop(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val left = width * 3 / 4
        val top = 0
        val cropWidth = width / 4
        val cropHeight = height / 4

        return Bitmap.createBitmap(bitmap, left, top, cropWidth, cropHeight)
    }
}
