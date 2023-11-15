package org.noise_planet.noisecapture.signal

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sin

fun realIFFT(realArray: DoubleArray) : DoubleArray {
    require(isPowerOfTwo(realArray.size - 2))
    val outArray = realArray.copyOf()
    val m = log2((realArray.size - 2).toDouble()).toInt()
    val n = (2.0.pow(m) + 0.5).toInt()
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
            outArray[k2] * a[k2] + outArray[k2 + 1] * a[k2 + 1] + outArray[n - k2] * b[k2] - outArray[n - k2 + 1] * b[k2 + 1]
        val xi =
            -outArray[k2] * a[k2 + 1] + outArray[k2 + 1] * a[k2] - outArray[n - k2] * b[k2 + 1] - outArray[n - k2 + 1] * b[k2]
        val xrN =
            outArray[n - k2] * a[n - k2] + outArray[n - k2 + 1] * a[n - k2 + 1] + outArray[k2] * b[n - k2] - outArray[k2 + 1] * b[n - k2 + 1]
        val xiN =
            -outArray[n - k2] * a[n - k2 + 1] + outArray[n - k2 + 1] * a[n - k2] - outArray[k2] * b[n - k2 + 1] - outArray[k2 + 1] * b[n - k2]
        outArray[k2] = xr
        outArray[k2 + 1] = xi
        outArray[n - k2] = xrN
        outArray[n - k2 + 1] = xiN
    }

    val temp = outArray[0]
    outArray[0] = 0.5 * outArray[0] + 0.5 * outArray[n]
    outArray[1] = 0.5 * temp - 0.5 * outArray[n]
    iFFT(n/2, outArray)
    return outArray.copyOf(outArray.size - 2)
}
