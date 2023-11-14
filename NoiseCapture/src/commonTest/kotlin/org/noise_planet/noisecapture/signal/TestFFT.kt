package org.noise_planet.noisecapture.signal

import kotlin.test.Test
import kotlin.math.*
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TestFFT {
    private fun generateSinusoidalSignal(frequency: Double, sampleRate: Double, duration: Double): DoubleArray {
        val numSamples = (duration * sampleRate).toInt()
        val signal = DoubleArray(numSamples)

        val angularFrequency = 2.0 * PI * frequency / sampleRate

        for (i in 0 until numSamples) {
            signal[i] = sin(i * angularFrequency)
        }

        return signal
    }
    @Test
    fun testRFFTSinus() {
        val frequency = 8.0 // Hz
        val sampleRate = 64.0 // Hz
        val duration = 2.0.pow(ceil(log2(sampleRate))) / sampleRate
        val samples = generateSinusoidalSignal(frequency, sampleRate, duration)
        val fftResult = samples.copyOf()
        realFFT(samples.size, fftResult)
        realIFFT(samples.size, fftResult)
        fftResult.forEachIndexed {
            index, value -> assertEquals(value, samples[index], 1e-8)
        }
    }
}