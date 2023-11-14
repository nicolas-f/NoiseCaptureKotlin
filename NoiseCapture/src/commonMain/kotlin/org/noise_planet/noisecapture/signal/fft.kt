package org.noise_planet.noisecapture.signal

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.log2


fun fft(length: Int, riArray: DoubleArray) {
    val m = log2(length.toDouble()).toInt()
    val n = (2.0.pow(m) + 0.5).toInt()
    require(n <= riArray.size)
    var wCos: Double
    var wSin: Double
    var uCos: Double
    var uSin: Double
    val n2 = n shr 1
    var l1 = n
    var l2: Int

    for (l in 0 until m) {
        l2 = l1 shr 1
        wCos = 1.0
        wSin = 0.0
        uCos = cos(PI / l2)
        uSin = -sin(PI / l2)

        for (j in 0 until l2) {
            for (i in j until n step l1) {
                val ir = 2 * i
                val ii = ir + 1
                val l22 = 2 * l2
                val sumRe = riArray[ir] + riArray[ir + l22]
                val sumIm = riArray[ii] + riArray[ii + l22]
                val diffRe = riArray[ir] - riArray[ir + l22]
                val diffIm = riArray[ii] - riArray[ii + l22]
                riArray[ir + l22] = diffRe * wCos - diffIm * wSin
                riArray[ii + l22] = diffRe * wSin + diffIm * wCos
                riArray[ir] = sumRe
                riArray[ii] = sumIm
            }
            val w = wCos * uCos - wSin * uSin
            wSin = wCos * uSin + wSin * uCos
            wCos = w
        }
        l1 = l1 shr 1
    }

    var k: Int
    var j = 0
    val n1 = n - 1

    for (i in 0 until n1) {
        if (i < j) {
            val jr = 2 * j
            val ji = jr + 1
            val ir = 2 * i
            val ii = ir + 1
            val tre = riArray[jr]
            val tim = riArray[ji]
            riArray[jr] = riArray[ir]
            riArray[ji] = riArray[ii]
            riArray[ir] = tre
            riArray[ii] = tim
        }
        k = n2
        while (k <= j) {
            j -= k
            k = k shr 1
        }
        j += k
    }
}
