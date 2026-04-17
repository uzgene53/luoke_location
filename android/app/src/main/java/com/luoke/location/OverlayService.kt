package com.luoke.location

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var textView: TextView

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ScreenCaptureService.ACTION_MATCH_RESULT -> {
                    val x = intent.getDoubleExtra(ScreenCaptureService.EXTRA_POS_X, 0.0)
                    val y = intent.getDoubleExtra(ScreenCaptureService.EXTRA_POS_Y, 0.0)
                    val conf = intent.getFloatExtra(ScreenCaptureService.EXTRA_CONFIDENCE, 0f)
                    textView.text = "X: ${x.toInt()}\nY: ${y.toInt()}\nConf: ${(conf * 100).toInt()}%"
                }
                ScreenCaptureService.ACTION_STATUS -> {
                    val msg = intent.getStringExtra(ScreenCaptureService.EXTRA_STATUS_MSG) ?: return
                    textView.text = msg
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        textView = TextView(this).apply {
            text = "Waiting..."
            textSize = 16f
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 50
        params.y = 100

        windowManager.addView(textView, params)

        val filter = IntentFilter().apply {
            addAction(ScreenCaptureService.ACTION_MATCH_RESULT)
            addAction(ScreenCaptureService.ACTION_STATUS)
        }
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        windowManager.removeView(textView)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
