package com.luoke.location.match

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc

class SiftMatcher {

    private var fullMapGray: Mat? = null
    private var keypointsMap = MatOfKeyPoint()
    private var descriptorsMap = Mat()

    init {
        try {
            System.loadLibrary("opencv_java4")
        } catch (_: Exception) {}
    }

    fun loadMap(bitmap: Bitmap) {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2GRAY)
        fullMapGray = mat

        val orb = ORB.create()
        orb.detectAndCompute(mat, Mat(), keypointsMap, descriptorsMap)
    }

    fun match(minimap: Bitmap): Pair<Int, Int>? {
        val map = fullMapGray ?: return null

        val miniMat = Mat()
        Utils.bitmapToMat(minimap, miniMat)
        Imgproc.cvtColor(miniMat, miniMat, Imgproc.COLOR_RGBA2GRAY)

        val orb = ORB.create()
        val kpMini = MatOfKeyPoint()
        val descMini = Mat()
        orb.detectAndCompute(miniMat, Mat(), kpMini, descMini)

        if (descMini.empty() || descriptorsMap.empty()) return null

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
            val pt = kpMapList[g.trainIdx].pt
            sumX += pt.x
            sumY += pt.y
        }

        val cx = (sumX / good.size).toInt()
        val cy = (sumY / good.size).toInt()

        return Pair(cx, cy)
    }
}
