package com.luoke.location.match

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.DMatch
import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc

class SiftMatcher {

    private var fullMapGray: Mat? = null
    private var keypointsMap = MatOfKeyPoint()
    private var descriptorsMap = Mat()
    private var opencvReady = false

    init {
        opencvReady = runCatching {
            System.loadLibrary("opencv_java4120")
            true
        }.getOrElse { false }
    }

    fun isReady(): Boolean = opencvReady

    fun loadMap(bitmap: Bitmap) {
        if (!opencvReady) return

        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2GRAY)
        fullMapGray = mat

        val orb = ORB.create()
        keypointsMap = MatOfKeyPoint()
        descriptorsMap = Mat()
        orb.detectAndCompute(mat, Mat(), keypointsMap, descriptorsMap)
    }

    fun match(minimap: Bitmap): Pair<Int, Int>? {
        if (!opencvReady) return null
        val map = fullMapGray ?: return null
        if (map.empty() || descriptorsMap.empty()) return null

        val miniMat = Mat()
        Utils.bitmapToMat(minimap, miniMat)
        Imgproc.cvtColor(miniMat, miniMat, Imgproc.COLOR_RGBA2GRAY)

        val orb = ORB.create()
        val kpMini = MatOfKeyPoint()
        val descMini = Mat()
        orb.detectAndCompute(miniMat, Mat(), kpMini, descMini)

        if (descMini.empty()) return null

        val matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING, false)
        val matches = ArrayList<MatOfDMatch>()
        matcher.knnMatch(descMini, descriptorsMap, matches, 2)

        val good = ArrayList<DMatch>()
        for (m in matches) {
            val d = m.toArray()
            if (d.size >= 2 && d[0].distance < 0.75 * d[1].distance) {
                good.add(d[0])
            }
        }

        if (good.size < 8) return null

        var sumX = 0.0
        var sumY = 0.0
        val kpMapList = keypointsMap.toArray()

        for (g in good) {
            val idx = g.trainIdx
            if (idx in kpMapList.indices) {
                val pt = kpMapList[idx].pt
                sumX += pt.x
                sumY += pt.y
            }
        }

        val cx = (sumX / good.size).toInt()
        val cy = (sumY / good.size).toInt()
        return Pair(cx, cy)
    }
}
