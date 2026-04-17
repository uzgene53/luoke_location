package com.luoke.location.capture

import kotlin.random.Random

object FakeLocator {
    fun nextCoordinateText(): String {
        val x = Random.nextInt(0, 5000)
        val y = Random.nextInt(0, 5000)
        return "X: $x\nY: $y"
    }
}
