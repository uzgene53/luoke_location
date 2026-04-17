package com.luoke.location.capture

import android.graphics.Bitmap
import android.media.ImageReader

/**
 * NOTE: This is a lightweight placeholder for real MediaProjection pipeline.
 * You still need to wire MediaProjection + VirtualDisplay.
 */
class ScreenImageProvider {

    private var latestBitmap: Bitmap? = null

    fun updateFrame(bitmap: Bitmap) {
        latestBitmap = bitmap
    }

    fun getLatestFrame(): Bitmap? {
        return latestBitmap
    }
}
