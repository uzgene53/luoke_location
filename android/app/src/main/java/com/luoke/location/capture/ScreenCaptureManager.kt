package com.luoke.location.capture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager

class ScreenCaptureManager(private val context: Context) {

    fun createCaptureIntent(): Intent {
        val manager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        return manager.createScreenCaptureIntent()
    }

    fun isCaptureResultOk(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
