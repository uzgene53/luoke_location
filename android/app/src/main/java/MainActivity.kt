package com.luoke.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.luoke.location.capture.ScreenCaptureManager
import com.luoke.location.data.CaptureSessionStore

class MainActivity : AppCompatActivity() {

    private lateinit var captureManager: ScreenCaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        button.text = "Start Capture"
        setContentView(button)

        captureManager = ScreenCaptureManager(this)

        button.setOnClickListener {
            startActivityForResult(captureManager.createCaptureIntent(), 1001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && captureManager.isCaptureResultOk(resultCode)) {
            CaptureSessionStore.save(resultCode, data)

            val intent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra(ScreenCaptureService.EXTRA_RESULT_CODE, resultCode)
                putExtra(ScreenCaptureService.EXTRA_RESULT_DATA, data)
            }

            startForegroundService(intent)
            startService(Intent(this, OverlayService::class.java))
        }
    }
}
