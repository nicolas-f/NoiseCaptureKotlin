package org.noise_planet.noisecapture.signal

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sin

fun realFFT(length: Int, realArray: DoubleArray) {
    val m = log2(length.toDouble()).toInt()
    val n = (2.0.pow(m) + 0.5).toInt()
    require(n <= realArray.size)
    fft(n/2, realArray)
    val a = DoubleArray(n)
    val b = DoubleArray(n)

    for (i in 0 until n / 2) {
        a[2 * i] = 0.5 * (1 - sin(2 * PI / n * i))
        a[2 * i + 1] = -0.5 * cos(2 * PI / n * i)
        b[2 * i] = 0.5 * (1 + sin(2 * PI / n * i))
        b[2 * i + 1] = 0.5 * cos(2 * PI / n * i)
    }

    for (k in 1 until n / 4 + 1) {
        val k2 = 2 * k
        val xr =
            realArray[k2] * a[k2] - realArray[k2 + 1] * a[k2 + 1] + realArray[n - k2] * b[k2] + realArray[n - k2 + 1] * b[k2 + 1]
        val xi =
            realArray[k2] * a[k2 + 1] + realArray[k2 + 1] * a[k2] + realArray[n - k2] * b[k2 + 1] - realArray[n - k2 + 1] * b[k2]
        val xrN =
            realArray[n - k2] * a[n - k2] - realArray[n - k2 + 1] * a[n - k2 + 1] + realArray[k2] * b[n - k2] + realArray[k2 + 1] * b[n - k2 + 1]
        val xiN =
            realArray[n - k2] * a[n - k2 + 1] + realArray[n - k2 + 1] * a[n - k2] + realArray[k2] * b[n - k2 + 1] - realArray[k2 + 1] * b[n - k2]
        realArray[k2] = xr
        realArray[k2 + 1] = xi
        realArray[n - k2] = xrN
        realArray[n - k2 + 1] = xiN
    }
}

