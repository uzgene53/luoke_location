package com.luoke.location.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object MapAssetLoader {
    fun loadBitmap(context: Context, assetPath: String): Bitmap? {
        return runCatching {
            context.assets.open(assetPath).use { BitmapFactory.decodeStream(it) }
        }.getOrNull()
    }
}
