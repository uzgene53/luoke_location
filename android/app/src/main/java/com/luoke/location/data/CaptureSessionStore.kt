package com.luoke.location.data

import android.content.Intent

object CaptureSessionStore {
    var resultCode: Int? = null
    var dataIntent: Intent? = null

    fun save(code: Int, data: Intent?) {
        resultCode = code
        dataIntent = data
    }

    fun clear() {
        resultCode = null
        dataIntent = null
    }
}
