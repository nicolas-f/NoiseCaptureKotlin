package org.noise_planet.noisecapture.signal

import kotlin.math.*

class FFT {
    fun fft(length: Int, riArray: DoubleArray) {
        val n = (2.0.pow(length) + 0.5).toInt()
        var wCos: Double
        var wSin: Double
        var uCos: Double
        var uSin: Double
        val n2 = n shr 1
        var l1 = n
        var l2: Int

        for (l in 0 until length) {
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

    fun iFFT(length: Int, riArray: DoubleArray) {
        val n = (2.0.pow(length) + 0.5).toInt()
        var wCos: Double
        var wSin: Double
        var uCos: Double
        var uSin: Double
        val n2 = n shr 1
        var l1 = n
        var l2: Int

        for (l in 0 until length) {
            l2 = l1 shr 1
            wCos = 1.0
            wSin = 0.0
            uCos = cos(PI / l2)
            uSin = sin(PI / l2)

            for (j in 0 until l2) {
                for (i in j until n step l1) {
                    val ir = 2 * i
                    val ii = ir + 1
                    val l22 = 2 * l2
                    val sumRe = 0.5 * (riArray[ir] + riArray[ir + l22])
                    val sumIm = 0.5 * (riArray[ii] + riArray[ii + l22])
                    val diffRe = 0.5 * (riArray[ir] - riArray[ir + l22])
                    val diffIm = 0.5 * (riArray[ii] - riArray[ii + l22])
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

    fun realFFT(length: Int, realArray: DoubleArray) {
        fft(length - 1, realArray)
        val n = (2.0.pow(length) + 0.5).toInt()
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

        val temp = realArray[0]
        realArray[0] = realArray[0] + realArray[1]
        realArray[n] = temp - realArray[1]
        realArray[1] = 0.0
        realArray[n + 1] = 0.0
    }

    fun realIFFT(length: Int, realArray: DoubleArray) {
        val n = (2.0.pow(length) + 0.5).toInt()
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
                realArray[k2] * a[k2] + realArray[k2 + 1] * a[k2 + 1] + realArray[n - k2] * b[k2] - realArray[n - k2 + 1] * b[k2 + 1]
            val xi =
                -realArray[k2] * a[k2 + 1] + realArray[k2 + 1] * a[k2] - realArray[n - k2] * b[k2 + 1] - realArray[n - k2 + 1] * b[k2]
            val xrN =
                realArray[n - k2] * a[n - k2] + realArray[n - k2 + 1] * a[n - k2 + 1] + realArray[k2] * b[n - k2] - realArray[k2 + 1] * b[n - k2 + 1]
            val xiN =
                -realArray[n - k2] * a[n - k2 + 1] + realArray[n - k2 + 1] * a[n - k2] - realArray[k2] * b[n - k2 + 1] - realArray[k2 + 1] * b[n - k2]
            realArray[k2] = xr
            realArray[k2 + 1] = xi
            realArray[n - k2] = xrN
            realArray[n - k2 + 1] = xiN
        }

        val temp = realArray[0]
        realArray[0] = 0.5 * realArray[0] + 0.5 * realArray[n]
        realArray[1] = 0.5 * temp - 0.5 * realArray[n]
        iFFT(length - 1, realArray)
    }

}