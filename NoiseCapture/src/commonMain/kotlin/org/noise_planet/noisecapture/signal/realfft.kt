package org.noise_planet.noisecapture.signal

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sin

/**
 * The bitwise AND operation of the number and its predecessor (number - 1) should result in 0.
 * This is because powers of two in binary have only one bit set, and subtracting 1 from them flips
 * that bit and sets all the lower bits to 1. So, the bitwise AND with the original number should
 * be 0 if it is a power of two.
 */
fun isPowerOfTwo(number: Int): Boolean {
    return number > 0 && (number and (number - 1)) == 0
}

fun realFFT(realArray: DoubleArray): DoubleArray {
    require(isPowerOfTwo(realArray.size))
    val outArray = DoubleArray(realArray.size + 2)
    realArray.copyInto(outArray)
    val m = log2(realArray.size.toDouble()).toInt()
    val n = (2.0.pow(m) + 0.5).toInt()
    fft(n/2, outArray)
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
            outArray[k2] * a[k2] - outArray[k2 + 1] * a[k2 + 1] + outArray[n - k2] * b[k2] + outArray[n - k2 + 1] * b[k2 + 1]
        val xi =
            outArray[k2] * a[k2 + 1] + outArray[k2 + 1] * a[k2] + outArray[n - k2] * b[k2 + 1] - outArray[n - k2 + 1] * b[k2]
        val xrN =
            outArray[n - k2] * a[n - k2] - outArray[n - k2 + 1] * a[n - k2 + 1] + outArray[k2] * b[n - k2] + outArray[k2 + 1] * b[n - k2 + 1]
        val xiN =
            outArray[n - k2] * a[n - k2 + 1] + outArray[n - k2 + 1] * a[n - k2] + outArray[k2] * b[n - k2 + 1] - outArray[k2 + 1] * b[n - k2]
        outArray[k2] = xr
        outArray[k2 + 1] = xi
        outArray[n - k2] = xrN
        outArray[n - k2 + 1] = xiN
    }

    val tmp = outArray[0]
    outArray[n] = outArray[0] - outArray[1]
    outArray[0] = tmp + outArray[1]
    outArray[1] = 0.0
    outArray[n+1] = 0.0

    return outArray
}

