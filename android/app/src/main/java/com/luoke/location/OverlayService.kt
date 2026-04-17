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
            val msg = intent?.getStringExtra(TrackingService.EXTRA_MESSAGE) ?: return
            textView.text = msg
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

        registerReceiver(receiver, IntentFilter(TrackingService.ACTION_TRACKING_TICK))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        windowManager.removeView(textView)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
