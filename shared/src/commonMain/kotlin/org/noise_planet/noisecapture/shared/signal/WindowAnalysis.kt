package org.noise_planet.noisecapture.shared.signal

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.yield
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min

const val SPECTRUM_REPLAY = 10
const val SPECTRUM_CACHE = 10

/**
 * Computation of STFT (Short Time Fourier Transform)
 * @sampleRate Sample rate to compute epoch
 * @windowSize Size of the window
 * @windowHop Run a new analysis each windowHop samples
 */
class WindowAnalysis(val sampleRate : Int, val windowSize : Int, val windowHop : Int) {
    val circularSamplesBuffer = FloatArray(windowSize)
    var circularBufferCursor = 0
    var samplesUntilWindow = windowSize
    val hannWindow = FloatArray(windowSize) {(0.5 * (1 - cos(2 * PI * it / (windowSize - 1)))).toFloat()}

    /**
     * Process the provided samples and run a STFFT analysis when a window is complete
     */
    fun pushSamples(epoch: Long, samples: FloatArray, processedWindows: MutableList<Window>? = null)
    : Sequence<SpectrumData> = sequence {
        var processed = 0
        while(processed < samples.size) {
            var toFetch = min(samples.size - processed, samplesUntilWindow)
            // fill the circular buffer
            while(toFetch > 0) {
                val copySize = min(circularSamplesBuffer.size - circularBufferCursor, toFetch)
                samples.copyInto(circularSamplesBuffer, circularBufferCursor, processed,
                    processed + copySize)
                circularBufferCursor += copySize
                processed += copySize
                toFetch -= copySize
                samplesUntilWindow -= copySize
                if(circularBufferCursor == circularSamplesBuffer.size) {
                    circularBufferCursor = 0
                }
            }
            if(samplesUntilWindow == 0) {
                // window complete push it
                val windowSamples = FloatArray(windowSize)
                circularSamplesBuffer.copyInto(
                    windowSamples,
                    windowSize - circularBufferCursor,
                    0,
                    circularBufferCursor
                )
                circularSamplesBuffer.copyInto(
                    windowSamples,
                    0,
                    circularBufferCursor,
                    circularSamplesBuffer.size
                )
                // apply window function
                for (i in windowSamples.indices) {
                    windowSamples[i] *= hannWindow[i]
                }
                val window = Window(
                    (epoch - ((samples.size - processed) / sampleRate.toDouble()) * 1000.0).toLong(),
                    windowSamples
                )
                yield(processWindow(window))
                processedWindows?.add(window)
                samplesUntilWindow = windowHop
            }
        }
    }

    fun reconstructOriginalSignal(processedWindows : List<Window>) : FloatArray{
        val sum = FloatArray(processedWindows.size + processedWindows.size * windowHop)
        for(i in processedWindows.indices) {
            for(j in 0..< windowSize) {
                val to = i * windowHop + j
                if(to < sum.size) {
                    sum[to] += processedWindows[i].samples[j]
                }
            }
        }
        return sum
    }

    /**
     * @see <a href="https://www.dsprelated.com/freebooks/sasp/Filling_FFT_Input_Buffer.html">Filling the FFT Input Buffer</a>
     */
    private fun processWindow(window: Window) : SpectrumData{
        return SpectrumData(window.epoch, processWindowFloat(window))
    }

    fun processWindowFloat(window: Window) : FloatArray {
        val fftWindowSize = nextPowerOfTwo(windowSize)
        val fftWindow = FloatArray(fftWindowSize)
        window.samples.copyInto(fftWindow, 0, windowSize/2, windowSize)
        window.samples.copyInto(fftWindow, fftWindowSize - (windowSize/2),
            0, windowSize/2)
        return realFFTFloat(fftWindow)
    }

    fun processWindowDouble(window: Window) : DoubleArray {
        val fftWindowSize = nextPowerOfTwo(windowSize)
        val fftWindow = DoubleArray(fftWindowSize)
        val startIndex = windowSize/2
        for(i in  startIndex..< windowSize) {
            fftWindow[i-startIndex] = window.samples[i].toDouble()
        }
        val destinationOffset = fftWindowSize - (windowSize/2)
        for(i in  0..< startIndex) {
            fftWindow[i+destinationOffset] = window.samples[i].toDouble()
        }
        return realFFT(fftWindow)
    }
}

data class Window(val epoch : Long, val samples : FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Window

        if (epoch != other.epoch) return false
        if (!samples.contentEquals(other.samples)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = epoch.hashCode()
        result = 31 * result + samples.contentHashCode()
        return result
    }
}

data class SpectrumData(val epoch : Long, val spectrum : FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SpectrumData

        if (epoch != other.epoch) return false
        if (!spectrum.contentEquals(other.spectrum)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = epoch.hashCode()
        result = 31 * result + spectrum.contentHashCode()
        return result
    }
}